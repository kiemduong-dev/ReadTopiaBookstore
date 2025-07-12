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
            request.setAttribute("error", "Missing payment information.");
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
                // Case: "Buy Now"
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                BookDTO book = bookDAO.getBookByID(bookId);
                if (book == null || quantity > book.getBookQuantity()) {
                    request.setAttribute("error", "Not enough quantity available for this book.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }

                CartDTO temp = new CartDTO();
                temp.setBookID(bookId);
                temp.setQuantity(quantity);
                cartItems.add(temp);

                totalAmount = quantity * book.getBookPrice();

            } else {
                // Case: Checkout from cart
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
                    // No selection → checkout entire cart
                    cartItems = cartDAO.getCartByUsername(username);
                    for (CartDTO cartItem : cartItems) {
                        BookDTO book = bookDAO.getBookByID(cartItem.getBookID());
                        if (book != null) {
                            totalAmount += cartItem.getQuantity() * book.getBookPrice();
                            selectedCartIDs.add(cartItem.getCartID());
                        }
                    }
                }

                // Cart is empty
                if (cartItems.isEmpty()) {
                    request.setAttribute("error", "There are no items to checkout.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }
            }

            // Create order
            OrderDTO order = new OrderDTO();
            order.setUsername(username);
            order.setOrderDate(new Timestamp(new Date().getTime()));
            order.setTotalAmount(totalAmount);
            int status = "CASH".equalsIgnoreCase(request.getParameter("paymentMethod")) ? 0 : 5;
            order.setOrderStatus(status); // Set status: 2 = Delivered (for Cash), 0 = Processing
            order.setOrderAddress(orderAddress);
            int orderID = orderDAO.createOrder(order);

            // Save order details
            for (CartDTO cart : cartItems) {
                BookDTO book = bookDAO.getBookByID(cart.getBookID());
                if (book != null) {
                    // 1. Create order detail
                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderID);
                    detail.setBookID(book.getBookID());
                    detail.setQuantity(cart.getQuantity());
                    detail.setTotalPrice(book.getBookPrice());
                    orderDetailDAO.addOrderDetail(detail);

                    // 2. Update stock
                    int newQuantity = book.getBookQuantity() - cart.getQuantity();
                    if (newQuantity < 0) {
                        newQuantity = 0; // ensure stock is not negative
                    }
                    bookDAO.updateBookQuantity(book.getBookID(), newQuantity);
                }
            }

            // Clear cart if not "Buy Now"
            cartDAO.deleteMultipleFromCart(selectedCartIDs, username);

            // Redirect to confirmation page
            request.setAttribute("orderID", orderID);
            request.getRequestDispatcher("/WEB-INF/view/order/confirmation.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during the payment process.");
            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
