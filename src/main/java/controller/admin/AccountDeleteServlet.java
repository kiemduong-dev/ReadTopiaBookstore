package controller.admin;

import dao.AccountDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDeleteServlet – Handles soft deletion of accounts by Admin. Admin can
 * only delete Staff Manager (role = 1) and Customer (role = 4). Cannot delete
 * Admin (role = 0), Seller Staff (2), Warehouse Staff (3), or self.
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountDeleteServlet", urlPatterns = {"/admin/account/delete"})
public class AccountDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles account deletion request via GET/POST method.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // ❌ Must be Admin (role = 0)
        if (loginUser == null || loginUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Only Admin can delete accounts.");
            return;
        }

        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("message", "⚠️ Username is required.");
            response.sendRedirect("list");
            return;
        }

        try {
            username = username.trim();
            AccountDTO target = accountDAO.getAccountByUsername(username);

            if (target == null) {
                session.setAttribute("message", "Account not found.");
                response.sendRedirect("list");
                return;
            }

            int targetRole = target.getRole();

            // ❌ Cannot delete Admin, Seller, Warehouse
            if (targetRole == 0 || targetRole == 2 || targetRole == 3) {
                session.setAttribute("message", "You cannot delete Admin, Seller, or Warehouse Staff.");
                response.sendRedirect("list");
                return;
            }

            // ❌ Cannot delete self
            if (username.equals(loginUser.getUsername())) {
                session.setAttribute("message", "You cannot delete your own account.");
                response.sendRedirect("list");
                return;
            }

            // ✅ If Staff Manager → delete from Staff table too
            if (targetRole == 1) {
                staffDAO.deleteByUsername(username);
            }

            // ✅ Soft delete (accStatus = 0)
            target.setAccStatus(0);
            boolean deleted = accountDAO.updateAccountStatus(target);

            if (deleted) {
                session.setAttribute("message", "Account \"" + username + "\" deleted successfully.");
            } else {
                session.setAttribute("message", "Failed to delete account \"" + username + "\".");
            }

        } catch (Exception e) {
            session.setAttribute("message", "Unexpected error: " + e.getMessage());
        }

        response.sendRedirect("list");
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
        return "Handles soft deletion of Staff Manager or Customer accounts by Admin.";
    }
}
