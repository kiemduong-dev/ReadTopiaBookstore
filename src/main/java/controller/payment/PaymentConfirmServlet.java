package controller.payment;

import dao.BookDAO;
import dao.CartDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.BookDTO;
import dto.CartDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/payment/confirm")
public class PaymentConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String action = request.getParameter("action");
            String type = request.getParameter("type");
            String orderAddress = request.getParameter("orderAddress");
            String bookIdRaw = request.getParameter("bookId");
            String quantityRaw = request.getParameter("quantity");
            String amountRaw = request.getParameter("amount");
            String[] selectedIds = request.getParameterValues("selectedCartIDs");

            String transferCode = (String) session.getAttribute("transferCode");
            if (transferCode == null) {
                transferCode = "DH" + System.currentTimeMillis();
                session.setAttribute("transferCode", transferCode);
            }

            double amount = Double.parseDouble(amountRaw.replaceAll("[^\\d.]", ""));
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
            } else {
                for (String idStr : selectedIds) {
                    int cartId = Integer.parseInt(idStr);
                    CartDTO cartItem = cartDAO.findByCartID(cartId);
                    if (cartItem != null) {
                        selectedItems.add(cartItem);
                        selectedCartIDs.add(cartId);
                    }
                }
            }

            if ("confirm".equals(action)) {
                BookDAO bookDAO = new BookDAO();
                OrderDAO orderDAO = new OrderDAO();
                OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

                OrderDTO order = new OrderDTO();
                order.setUsername(username);
                order.setOrderDate(new Timestamp(new Date().getTime()));
                order.setTotalAmount(amount);
                order.setOrderStatus(5);
                order.setOrderAddress(orderAddress);

                int orderID = orderDAO.createOrder(order);

                for (CartDTO cart : selectedItems) {
                    BookDTO book = bookDAO.getBookByID(cart.getBookID());
                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderID);
                    detail.setBookID(book.getBookID());
                    detail.setQuantity(cart.getQuantity());
                    detail.setTotalPrice(book.getBookPrice() * cart.getQuantity());
                    orderDetailDAO.addOrderDetail(detail);
                    bookDAO.updateBookQuantity(book.getBookID(), book.getBookQuantity() - cart.getQuantity());
                }

                if (!"buynow".equals(type)) {
                    cartDAO.deleteMultipleFromCart(selectedCartIDs, username);
                }

                session.removeAttribute("transferCode");

                request.setAttribute("orderID", orderID);
                request.getRequestDispatcher("/WEB-INF/view/order/confirmation.jsp").forward(request, response);
                return;
            }

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xử lý thanh toán thất bại.");
        }

    }
}
