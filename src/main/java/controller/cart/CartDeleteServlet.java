
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart/delete")
public class CartDeleteServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idsParam = request.getParameter("ids");
        CartDAO cartDAO = new CartDAO();

        try {
            if (idsParam != null && !idsParam.trim().isEmpty()) {
                // Delete multiple items
                String[] idsArray = idsParam.split(",");
                List<Integer> cartIDs = new ArrayList<>();
                for (String id : idsArray) {
                    cartIDs.add(Integer.parseInt(id));
                }

                boolean deleted = cartDAO.deleteMultipleFromCart(cartIDs, username);
                response.sendRedirect(request.getContextPath() + "/cart/view?msg=" + (deleted ? "deleted" : "delete_failed"));

            } else {
                // Delete one item
                String cartIDParam = request.getParameter("cartID");
                if (cartIDParam == null || cartIDParam.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart ID is required");
                    return;
                }

                int cartID = Integer.parseInt(cartIDParam);
                CartDTO cart = cartDAO.findByCartID(cartID);

                if (cart == null || !cart.getUsername().equals(username)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access to cart item");
                    return;
                }

                boolean success = cartDAO.deleteFromCart(cartID, username);
                response.sendRedirect(request.getContextPath() + "/cart/view?msg=" + (success ? "deleted" : "delete_failed"));
            }

        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid cartID format: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid cart ID format");
        } catch (Exception e) {
            System.err.println("❌ CartDeleteServlet error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cart/view?msg=delete_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        processRequest(req, res);
    }
}
