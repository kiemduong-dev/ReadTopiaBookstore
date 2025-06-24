package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.SecurityUtil;

import java.io.IOException;

/**
 * ResetPasswordServlet
 *
 * Handles password reset after OTP verification. Supports POST only. GET access
 * is blocked.
 *
 * URL mapping: /reset-password
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles POST request to reset the user password.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Reject direct GET access and redirect to forgot-password page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("forgot-password");
    }

    /**
     * Core logic to reset the password for a verified session.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("resetUser") == null
                || !Boolean.TRUE.equals(session.getAttribute("verifiedReset"))) {

            response.sendRedirect("forgot-password");
            return;
        }

        String username = (String) session.getAttribute("resetUser");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Password confirmation does not match or is empty.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        String hashedPassword = SecurityUtil.hashPassword(newPassword);

        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByUsername(username, hashedPassword);

        if (updated) {
            dao.clearOTP(username);

            session.removeAttribute("resetUser");
            session.removeAttribute("otp");
            session.removeAttribute("resetEmail");
            session.removeAttribute("otpPurpose");
            session.removeAttribute("verifiedReset");

            request.setAttribute("success", "Password reset successful. Please log in.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to reset password. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
        }
    }
}
