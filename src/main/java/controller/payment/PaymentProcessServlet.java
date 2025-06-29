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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/payment/process")
public class PaymentProcessServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String paymentMethod = request.getParameter("paymentMethod");
        String orderAddress = request.getParameter("orderAddress");
        String type = request.getParameter("type");

        if (paymentMethod == null || orderAddress == null || orderAddress.trim().isEmpty()) {
            request.setAttribute("error", "Thiếu thông tin thanh toán.");
            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
            return;
        }

        try {
            CartDAO cartDAO = new CartDAO();
            BookDAO bookDAO = new BookDAO();
            OrderDAO orderDAO = new OrderDAO();
            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();

            List<CartDTO> cartItems = new ArrayList<>();
            List<Integer> selectedCartIDs = new ArrayList<>();
            double totalAmount = 0;

            if ("buynow".equals(type)) {
                // Trường hợp "Buy Now"
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                BookDTO book = bookDAO.getBookByID(bookId);
                if (book == null || quantity > book.getBookQuantity()) {
                    request.setAttribute("error", "Sách không còn đủ số lượng.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }

                CartDTO temp = new CartDTO();
                temp.setBookID(bookId);
                temp.setQuantity(quantity);
                cartItems.add(temp);

                totalAmount = quantity * book.getBookPrice();

            } else {
                // Trường hợp thanh toán từ giỏ hàng
                String[] selectedCartIdsStr = request.getParameterValues("selectedCartIDs");

                if (selectedCartIdsStr != null && selectedCartIdsStr.length > 0) {
                    for (String idStr : selectedCartIdsStr) {
                        int cartId = Integer.parseInt(idStr);
                        CartDTO cartItem = cartDAO.findByCartID(cartId);
                        if (cartItem != null) {
                            BookDTO book = bookDAO.getBookByID(cartItem.getBookID());
                            if (book != null) {
                                totalAmount += cartItem.getQuantity() * book.getBookPrice();
                                cartItems.add(cartItem);
                                selectedCartIDs.add(cartId);
                            }
                        }
                    }
                } else {
                    // Không chọn → thanh toán toàn bộ giỏ
                    cartItems = cartDAO.getCartByUsername(username);
                    for (CartDTO cartItem : cartItems) {
                        BookDTO book = bookDAO.getBookByID(cartItem.getBookID());
                        if (book != null) {
                            totalAmount += cartItem.getQuantity() * book.getBookPrice();
                            selectedCartIDs.add(cartItem.getCartID());
                        }
                    }
                }

                // Nếu không chọn gì và giỏ rỗng
                if (cartItems.isEmpty()) {
                    request.setAttribute("error", "Không có sản phẩm nào để thanh toán.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }
            }

            // Tạo đơn hàng
            OrderDTO order = new OrderDTO();
            order.setUsername(username);
            order.setOrderDate(new Timestamp(new Date().getTime()));
            order.setTotalAmount(totalAmount);
            order.setOrderStatus(0); // Đang xử lý
            order.setOrderAddress(orderAddress);
            int orderID = orderDAO.createOrder(order);

            // Lưu chi tiết đơn hàng
            for (CartDTO cart : cartItems) {
                BookDTO book = bookDAO.getBookByID(cart.getBookID());
                if (book != null) {
                    // 1. Tạo chi tiết đơn hàng
                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderID);
                    detail.setBookID(book.getBookID());
                    detail.setQuantity(cart.getQuantity());
                    detail.setTotalPrice(book.getBookPrice());
                    orderDetailDAO.addOrderDetail(detail);

                    // 2. Trừ kho
                    int newQuantity = book.getBookQuantity() - cart.getQuantity();
                    if (newQuantity < 0) {
                        newQuantity = 0; // đảm bảo không âm
                    }
                    bookDAO.updateBookQuantity(book.getBookID(), newQuantity);
                }
            }

            // Xóa giỏ nếu không phải "Buy Now"
            cartDAO.deleteMultipleFromCart(selectedCartIDs, username);

            // Hiển thị trang xác nhận
            request.setAttribute("orderID", orderID);
            request.getRequestDispatcher("/WEB-INF/view/order/confirmation.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình xử lý thanh toán.");
            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
