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
 * Handles soft-deletion of user accounts from the admin dashboard.
 * If the account role is not a customer (role != 1), the corresponding Staff record will also be deleted.
 * 
 * URL mapping: /admin/account/delete
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountDeleteServlet", urlPatterns = {"/admin/account/delete"})
public class AccountDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AccountDAO accountDAO = new AccountDAO();
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles GET request for soft-deleting an account.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    /**
     * Handles POST request for soft-deleting an account.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processDelete(request, response);
    }

    /**
     * Process the deletion logic (soft delete by setting accStatus = 0).
     * If the account is not a customer, the associated staff record is removed.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException if redirection fails
     */
    private void processDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        HttpSession session = request.getSession();

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
            if (account.getRole() != 1) {
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

    /**
     * Returns servlet description.
     *
     * @return a string describing the servlet
     */
    @Override
    public String getServletInfo() {
        return "Handles soft-deletion of user accounts from Admin Dashboard.";
    }
}
