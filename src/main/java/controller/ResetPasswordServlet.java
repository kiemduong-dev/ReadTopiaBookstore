package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;

/**
 * ResetPasswordServlet – Handles password reset after OTP verification. Accepts
 * POST requests only. GET access is redirected to forgot-password.
 *
 * URL mapping: /reset-password Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes POST requests to reset the user's password.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Redirects GET requests to the forgot-password page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/forgot-password");
    }

    /**
     * Core logic for resetting user password after OTP verification.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // Step 1: Validate session state
        if (session == null
                || session.getAttribute("resetUser") == null
                || !Boolean.TRUE.equals(session.getAttribute("verifiedReset"))) {

            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        // Step 2: Retrieve parameters
        String username = (String) session.getAttribute("resetUser");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Step 3: Validate input
        if (newPassword == null || confirmPassword == null
                || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {

            request.setAttribute("error", "Password fields cannot be empty.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Confirm password does not match.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidPassword(newPassword)) {
            request.setAttribute("error", "Password must be at least 8 characters and include uppercase, lowercase, number, and special character.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        // Step 4: Update password in database
        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByUsername(username, newPassword);

        if (updated) {
            // Step 5: Clean up session and OTP
            dao.clearOTP(username);
            session.removeAttribute("resetUser");
            session.removeAttribute("resetEmail");
            session.removeAttribute("otp");
            session.removeAttribute("otpPurpose");
            session.removeAttribute("verifiedReset");

            // Step 6: Redirect to login with success message
            request.setAttribute("success", "Password reset successful. Please log in.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to reset password. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
        }
    }
}
