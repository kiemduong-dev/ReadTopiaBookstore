package controller.admin;

import dao.AccountDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDeleteServlet
 *
 * Xử lý yêu cầu xóa mềm tài khoản từ Admin Dashboard.
 * Nếu role != 1 (không phải Customer), sẽ xóa bản ghi Staff liên quan.
 */
@WebServlet(name = "AccountDeleteServlet", urlPatterns = {"/admin/account/delete"})
public class AccountDeleteServlet extends HttpServlet {

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

        String username = request.getParameter("username");
        HttpSession session = request.getSession();

        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("message", "⚠️ Invalid username. Deletion aborted.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        username = username.trim();
        AccountDTO account = accountDAO.getAccountByUsername(username);

        if (account == null) {
            session.setAttribute("message", "⚠️ Account not found.");
        } else {
            // Nếu không phải customer → xóa bản ghi staff liên quan
            if (account.getRole() != 1) {
                staffDAO.deleteByUsername(username);
            }

            // Cập nhật trạng thái tài khoản thành accStatus = 0 (soft delete)
            account.setAccStatus(0);
            boolean success = accountDAO.updateAccountStatus(account);

            if (success) {
                session.setAttribute("message", "✅ Account \"" + username + "\" deleted (deactivated) successfully.");
            } else {
                session.setAttribute("message", "❌ Failed to delete account \"" + username + "\".");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/account/list");
    }

    @Override
    public String getServletInfo() {
        return "Handles soft-deletion of user accounts from Admin Dashboard.";
    }
}
