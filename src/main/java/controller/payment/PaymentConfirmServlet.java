package controller.payment;

import dao.BookDAO;
import dao.CartDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.VoucherDAO;
import dto.BookDTO;
import dto.CartDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@WebServlet("/payment/confirm")
public class PaymentConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String type = request.getParameter("type");
            String orderAddress = request.getParameter("orderAddress");
            String bookIdRaw = request.getParameter("bookId");
            String quantityRaw = request.getParameter("quantity");
            String finalAmountRaw = request.getParameter("finalAmount");
            String[] selectedIds = request.getParameterValues("selectedCartIDs");

            // ✅ Xử lý Voucher
            String vouIDRaw = request.getParameter("voucherID");
            Integer vouID = null;
            if (vouIDRaw != null && !vouIDRaw.isEmpty()) {
                try {
                    vouID = Integer.parseInt(vouIDRaw);
                } catch (NumberFormatException ignored) {
                }
            }

            double finalAmount = Double.parseDouble(finalAmountRaw.replaceAll("[^\\d.]", ""));

            List<CartDTO> selectedItems = new ArrayList<>();
            List<Integer> selectedCartIDs = new ArrayList<>();
            CartDAO cartDAO = new CartDAO();

            if ("buynow".equals(type)) {
                int bookId = Integer.parseInt(bookIdRaw);
                int quantity = Integer.parseInt(quantityRaw);
                CartDTO temp = new CartDTO();
                temp.setBookID(bookId);
                temp.setQuantity(quantity);
                selectedItems.add(temp);
            } else if (selectedIds != null) {
                for (String idStr : selectedIds) {
                    int cartId = Integer.parseInt(idStr);
                    CartDTO cartItem = cartDAO.findByCartID(cartId);
                    if (cartItem != null) {
                        selectedItems.add(cartItem);
                        selectedCartIDs.add(cartId);
                    }
                }
            }

            if (selectedItems.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No items found for confirmation.");
                return;
            }

            BookDAO bookDAO = new BookDAO();
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

            OrderDTO order = new OrderDTO();
            order.setUsername(username);
            order.setOrderDate(new Timestamp(new Date().getTime()));
            order.setTotalAmount(finalAmount);
            order.setOrderStatus(5); // Bank transfer pending
            order.setOrderAddress(orderAddress);
            order.setVouID(vouID); // ✅ Gán voucher nếu có

            int orderID = orderDAO.createOrder(order);

            if (vouID != null) {
                new VoucherDAO().decreaseVoucherQuantity(vouID);
            }

            for (CartDTO cart : selectedItems) {
                BookDTO book = bookDAO.getBookByID(cart.getBookID());
                if (book != null) {
                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderID);
                    detail.setBookID(book.getBookID());
                    detail.setQuantity(cart.getQuantity());
                    detail.setTotalPrice(book.getBookPrice() * cart.getQuantity());
                    orderDetailDAO.addOrderDetail(detail);

                    int remaining = book.getBookQuantity() - cart.getQuantity();
                    if (remaining < 0) {
                        remaining = 0;
                    }
                    bookDAO.updateBookQuantity(book.getBookID(), remaining);
                }
            }

            if (!"buynow".equals(type) && !selectedCartIDs.isEmpty()) {
                cartDAO.deleteMultipleFromCart(selectedCartIDs, username);
            }

            session.removeAttribute("transferCode");

            request.setAttribute("orderID", orderID);
            request.getRequestDispatcher("/WEB-INF/view/order/confirmation.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Order confirmation failed.");
        }
    }
}
