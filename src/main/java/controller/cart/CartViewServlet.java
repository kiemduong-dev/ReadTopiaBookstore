package controller.cart;

import dao.CartDAO;
import dao.BookDAO;
import dto.CartDTO;
import dto.BookDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart/view")
public class CartViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Handle messages
        String msg = request.getParameter("msg");
        if ("updated".equals(msg)) {
            request.setAttribute("success", "Giỏ hàng đã được cập nhật.");
        } else if ("removed".equals(msg)) {
            request.setAttribute("success", "Sản phẩm đã được xóa khỏi giỏ hàng.");
        }

        String error = request.getParameter("error");

        try {
            CartDAO cartDAO = new CartDAO();
            BookDAO bookDAO = new BookDAO();
            List<CartDTO> cartItems = cartDAO.getCartByUsername(username);

            List<CartItemWithBook> enrichedItems = new ArrayList<>();
            double totalAmount = 0;

            for (CartDTO cartItem : cartItems) {
                BookDTO book = bookDAO.getBookByID(cartItem.getBookID());
                if (book != null) {
                    double itemTotal = book.getBookPrice() * cartItem.getQuantity();
                    CartItemWithBook item = new CartItemWithBook(cartItem, book, itemTotal);
                    enrichedItems.add(item);
                    totalAmount += itemTotal;
                }
            }

            if (enrichedItems.isEmpty() || "empty-cart".equals(error)) {
                request.setAttribute("error", "Giỏ hàng của bạn đang trống.");
            }

            request.setAttribute("cartItemsWithBooks", enrichedItems); 
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("itemCount", enrichedItems.size());

            request.getRequestDispatcher("/WEB-INF/view/cart/view.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("❌ CartViewServlet error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải giỏ hàng.");
            request.getRequestDispatcher("/WEB-INF/view/cart/view.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public static class CartItemWithBook {

        private CartDTO cartItem;
        private BookDTO book;
        private double itemTotal;

        public CartItemWithBook(CartDTO cartItem, BookDTO book, double itemTotal) {
            this.cartItem = cartItem;
            this.book = book;
            this.itemTotal = itemTotal;
        }

        public CartDTO getCartItem() {
            return cartItem;
        }

        public BookDTO getBook() {
            return book;
        }

        public double getItemTotal() {
            return itemTotal;
        }
    }
}
