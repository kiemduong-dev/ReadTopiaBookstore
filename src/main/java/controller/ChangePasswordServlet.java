package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;

/**
 * ChangePasswordServlet – Allows authenticated users to change password. GET:
 * Display form. POST: Validate and update password.
 *
 * URL Mapping: /change-password Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handle GET and POST requests for changing password
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // Step 1: Must be logged in
        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Step 2: GET → Show form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        // Step 3: POST → Handle password change
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Step 4: Validate fields
        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {

            request.setAttribute("error", "❌ All fields are required.");
        } else if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "❌ Confirm password does not match.");
        } else if (!ValidationUtil.isValidPassword(newPassword)) {
            request.setAttribute("error", "❌ Password must be at least 8 characters and include uppercase, lowercase, digit and special character.");
        } else {
            // Step 5: Business logic – update password if old password is valid
            AccountDAO dao = new AccountDAO();
            boolean updated = dao.updatePasswordByOld(account.getUsername(), oldPassword, newPassword);

            if (updated) {
                session.setAttribute("account", account); // Optional: you can mask password here
                request.setAttribute("success", "✅ Password changed successfully.");
            } else {
                request.setAttribute("error", "❌ Old password is incorrect.");
            }
        }

        // Step 6: Return to change password form
        request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
    }

    /**
     * Handle GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handle POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @return Description
     */
    @Override
    public String getServletInfo() {
        return "Handles password change for authenticated users.";
    }
}
