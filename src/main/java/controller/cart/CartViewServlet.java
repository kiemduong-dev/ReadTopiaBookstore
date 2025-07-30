package controller.cart;

import dao.CartDAO;
import dao.BookDAO;
import dao.PromotionDAO;
import dto.CartDTO;
import dto.BookDTO;
import dto.PromotionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.DBContext;

import java.io.IOException;
import java.sql.Connection;
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

        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String msg = request.getParameter("msg");
        if ("updated".equals(msg)) {
            request.setAttribute("success", "Cart updated successfully.");
        } else if ("removed".equals(msg)) {
            request.setAttribute("success", "Item removed from cart.");
        }

        String error = request.getParameter("error");

        try ( Connection conn = DBContext.getConnection()) {

            CartDAO cartDAO = new CartDAO();
            BookDAO bookDAO = new BookDAO();
            PromotionDAO promotionDAO = new PromotionDAO(conn);

            // Lấy giỏ hàng
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
                request.setAttribute("error", "Your cart is empty.");
            }

            // Lấy promotion còn hạn
            List<PromotionDTO> promotions = promotionDAO.getValidPromotions(); // 🔥 Sửa ở đây
            request.setAttribute("promotions", promotions);

            // Forward
            request.setAttribute("cartItemsWithBooks", enrichedItems);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("itemCount", enrichedItems.size());

            request.getRequestDispatcher("/WEB-INF/view/cart/view.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while loading your cart.");
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
