package controller.admin;

import dao.AccountDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDeleteServlet – Handles soft deletion of accounts by Admin.
 * Admin can only delete Staff Manager (role = 1) and Customer (role = 4).
 * Cannot delete Admin (role = 0), Seller Staff (2), Warehouse Staff (3), or self.
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountDeleteServlet", urlPatterns = {"/admin/account/delete"})
public class AccountDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AccountDAO accountDAO = new AccountDAO();
    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("message", "⚠️ Invalid username.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        username = username.trim();
        AccountDTO target = accountDAO.getAccountByUsername(username);

        if (target == null) {
            session.setAttribute("message", "❌ Account not found.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        if (username.equals(currentUser.getUsername())) {
            session.setAttribute("message", "⛔ You cannot delete your own account.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        int targetRole = target.getRole();
        if (targetRole == 0 || targetRole == 2 || targetRole == 3) {
            session.setAttribute("message", "🚫 Cannot delete Admin, Seller Staff or Warehouse Staff.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        // ✅ Nếu là Staff Manager thì xóa dữ liệu trong bảng Staff
        if (targetRole == 1) {
            staffDAO.deleteByUsername(username);
        }

        // ✅ Soft delete: accStatus = 0
        target.setAccStatus(0);
        boolean deleted = accountDAO.updateAccountStatus(target);

        if (deleted) {
            session.setAttribute("message", "✅ Account \"" + username + "\" deleted successfully.");
        } else {
            session.setAttribute("message", "❌ Failed to delete account \"" + username + "\".");
        }

        response.sendRedirect(request.getContextPath() + "/admin/account/list");
    }

    @Override
    public String getServletInfo() {
        return "Handles soft-deletion of Staff Manager or Customer accounts by Admin.";
    }
}
