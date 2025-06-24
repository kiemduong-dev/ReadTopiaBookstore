package controller.cart;

import dao.BookDAO;
import dao.CartDAO;
import dao.OrderDAO;
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
import java.util.Date;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Check authentication
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            CartDAO cartDAO = new CartDAO();
            List<CartDTO> cartItems = cartDAO.getCartByUsername(username);

            if (cartItems == null || cartItems.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart/view?error=empty-cart");
                return;
            }

            // Validate stock availability for all items
            BookDAO bookDAO = new BookDAO();
            for (CartDTO item : cartItems) {
                if (!cartDAO.isStockAvailable(item.getBookID(), item.getQuantity())) {
                    response.sendRedirect(request.getContextPath() + "/cart/view?error=insufficient_stock");
                    return;
                }
            }

            // Create order
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = new OrderDTO();
            order.setUsername(username);
            order.setOrderDate(new Date());
            order.setOrderStatus(0); // Processing
            order.setProID(null);
            // No promotion
            order.setStaffID(null);
// No staff assigned yet
            order.setOrderAddress("Địa chỉ chưa cung cấp"); // Sẽ cập nhật sau khi người dùng điền

            int orderID = orderDAO.createOrder(order);
            if (orderID <= 0) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create order");
                return;
            }

            // Add order details and calculate total
            double totalAmount = 0;
            for (CartDTO item : cartItems) {
                BookDTO book = bookDAO.getBookByID(item.getBookID());
                if (book != null) {
                    int quantity = item.getQuantity();
                    double unitPrice = book.getBookPrice();
                    double itemTotal = quantity * unitPrice;

                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setOrderID(orderID);
                    detail.setBookID(book.getBookID());
                    detail.setQuantity(quantity);
                    detail.setTotalPrice(itemTotal);

                    boolean detailAdded = orderDAO.addOrderDetail(detail);
                    if (detailAdded) {
                        totalAmount += itemTotal;

                        // Decrease book stock (using existing BookDAO updateBook method)
                        int currentQuantity = book.getBookQuantity();
                        book.setBookQuantity(currentQuantity - quantity);
                        bookDAO.updateBook(book);
                    }
                }
            }

            // Clear cart after successful order creation
            cartDAO.clearCartByUsername(username);

            // Set attributes for checkout page
            request.setAttribute("orderId", orderID);
            request.setAttribute("amount", totalAmount);
            request.setAttribute("orderItems", cartItems.size());

            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ CheckoutServlet error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Checkout process failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles checkout process with stock validation and order creation";
    }
}   
