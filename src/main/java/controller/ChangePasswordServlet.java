package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ChangePasswordServlet
 *
 * This servlet handles the logic for authenticated users to change their
 * password. - GET request shows the password change form. - POST request
 * processes the password update.
 *
 * URL: /change-password
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    /**
     * Handles both GET and POST requests for password change.
     *
     * Flow: 1. Validates session and login status. 2. GET → shows change
     * password form. 3. POST → validates inputs, compares old password, updates
     * if valid.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO acc = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        // Handle GET: Show change password form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        // Handle POST: Process password change
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Input validation
        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "❌ All fields are required.");
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "❌ Confirm password does not match.");
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        // Business logic: verify old password and update
        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByOld(acc.getUsername(), oldPassword, newPassword);

        if (updated) {
            acc.setPassword("********"); // Hide real password
            session.setAttribute("account", acc);
            request.setAttribute("success", "✅ Password changed successfully.");
        } else {
            request.setAttribute("error", "❌ Old password is incorrect.");
        }

        request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
    }

    /**
     * Handles GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return servlet info
     */
    @Override
    public String getServletInfo() {
        return "Handles password change request for authenticated users.";
    }
}
