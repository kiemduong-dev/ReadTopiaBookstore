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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/forgot-password");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // Kiểm tra session hợp lệ
        if (session == null
                || session.getAttribute("resetUser") == null
                || !Boolean.TRUE.equals(session.getAttribute("verifiedReset"))) {

            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String username = (String) session.getAttribute("resetUser");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Kiểm tra đầu vào mật khẩu
        if (newPassword == null || confirmPassword == null || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Password fields cannot be empty.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Password confirmation does not match.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        // Hash và cập nhật mật khẩu
        String hashedPassword = SecurityUtil.hashPassword(newPassword);
        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByUsername(username, hashedPassword);

        if (updated) {
            // Clear thông tin liên quan đến OTP & reset
            dao.clearOTP(username);
            session.removeAttribute("resetUser");
            session.removeAttribute("otp");
            session.removeAttribute("resetEmail");
            session.removeAttribute("otpPurpose");
            session.removeAttribute("verifiedReset");

            // Gửi thông báo thành công
            request.setAttribute("success", "✅ Password reset successful. Please log in.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        } else {
            // Gửi thông báo thất bại
            request.setAttribute("error", "❌ Failed to reset password. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
        }
    }
}
