package controller.admin;

import dao.AccountDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDeleteServlet – Handles soft-deletion of user accounts from the admin
 * dashboard. Only Admin role can perform this action. Deletes corresponding
 * staff record if role = 1. Author: CE181518 Dương An Kiếm
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
        HttpSession session = request.getSession();
        AccountDTO currentUser = (AccountDTO) session.getAttribute("account");

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            session.setAttribute("message", "Invalid username. Deletion aborted.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        username = username.trim();
        AccountDTO account = accountDAO.getAccountByUsername(username);

        if (account == null) {
            session.setAttribute("message", "Account not found.");
        } else {
            if (username.equals(currentUser.getUsername())) {
                session.setAttribute("message", "You cannot delete your own account.");
                response.sendRedirect(request.getContextPath() + "/admin/account/list");
                return;
            }

            if (account.getRole() == 1) {
                staffDAO.deleteByUsername(username);
            }

            account.setAccStatus(0);
            boolean success = accountDAO.updateAccountStatus(account);

            if (success) {
                session.setAttribute("message", "Account \"" + username + "\" was deactivated successfully.");
            } else {
                session.setAttribute("message", "Failed to deactivate account \"" + username + "\".");
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/account/list");
    }

    @Override
    public String getServletInfo() {
        return "Handles soft-deletion of user accounts from Admin Dashboard.";
    }
}
