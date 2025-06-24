package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.SecurityUtil;

import java.io.IOException;

@WebServlet(name = "ResetPasswordServlet", urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // 🔐 Kiểm tra session hợp lệ
        if (session == null ||
            session.getAttribute("resetUser") == null ||
            !Boolean.TRUE.equals(session.getAttribute("verifiedReset"))) {

            response.sendRedirect("forgot-password");
            return;
        }

        String username = (String) session.getAttribute("resetUser");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // ❌ Kiểm tra mật khẩu hợp lệ
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "❌ Mật khẩu xác nhận không khớp hoặc bị trống.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
            return;
        }

        // 🔒 Hash mật khẩu mới
        String hashedPassword = SecurityUtil.hashPassword(newPassword);

        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByUsername(username, hashedPassword);

        if (updated) {
            dao.clearOTP(username); // 🧹 Xoá OTP trong DB

            // 🧼 Xóa session tạm
            session.removeAttribute("resetUser");
            session.removeAttribute("otp");
            session.removeAttribute("resetEmail");
            session.removeAttribute("otpPurpose");
            session.removeAttribute("verifiedReset");

            request.setAttribute("success", "🎉 Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "❌ Đặt lại mật khẩu thất bại. Vui lòng thử lại.");
            request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 🚫 Không cho truy cập GET trực tiếp
        response.sendRedirect("forgot-password");
    }
}
