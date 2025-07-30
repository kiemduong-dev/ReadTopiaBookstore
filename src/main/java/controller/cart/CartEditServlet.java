package controller.cart;

import dao.CartDAO;
import dto.CartDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cart/edit")
public class CartEditServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        // Kiểm tra quyền đăng nhập
        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String cartIDStr = request.getParameter("cartID");
        String quantityStr = request.getParameter("quantity");

        if (cartIDStr == null || quantityStr == null) {
            response.sendRedirect(request.getContextPath() + "/cart/view?error=missing_parameters");
            return;
        }

        try {
            int cartID = Integer.parseInt(cartIDStr);
            int quantity = Integer.parseInt(quantityStr);

            if (quantity <= 0) {
                response.sendRedirect(request.getContextPath() + "/cart/view?error=invalid_quantity");
                return;
            }

            CartDAO cartDAO = new CartDAO();
            CartDTO cartItem = cartDAO.findByCartID(cartID);

            // Debug log
            System.out.println("🔍 Session username: " + username);
            System.out.println("🛒 Cart item found: " + (cartItem != null));
            if (cartItem != null) {
                System.out.println("🛒 Cart item username: " + cartItem.getUsername());
                System.out.println("🛒 Book ID: " + cartItem.getBookID());
            }

            // Kiểm tra quyền sở hữu giỏ hàng
            if (cartItem == null || !username.equals(cartItem.getUsername())) {
                response.sendRedirect(request.getContextPath() + "/cart/view?error=unauthorized");
                return;
            }

            // Kiểm tra tồn kho
            if (!cartDAO.isStockAvailable(cartItem.getBookID(), quantity)) {
                response.sendRedirect(request.getContextPath() + "/cart/view?error=insufficient_stock");
                return;
            }

            // Cập nhật giỏ hàng
            cartItem.setQuantity(quantity);
            boolean success = cartDAO.updateCart(cartItem);

            if (success) {
                System.out.println("✅ Cập nhật giỏ thành công");
                response.sendRedirect(request.getContextPath() + "/cart/view?msg=updated");
            } else {
                System.err.println("❌ Cập nhật giỏ thất bại");
                response.sendRedirect(request.getContextPath() + "/cart/view?error=update_failed");
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ Lỗi định dạng số: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cart/view?error=invalid_format");
        } catch (Exception e) {
            System.err.println("❌ Lỗi server CartEditServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart/view?error=server_error");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles editing cart item quantities with validation and stock check";
    }
}
