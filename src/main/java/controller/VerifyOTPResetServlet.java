package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "VerifyOTPResetServlet", urlPatterns = {"/verify-otp-reset"})
public class VerifyOTPResetServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // ❗ Không tạo mới nếu không có

        // ❌ Kiểm tra session và các biến cần thiết
        if (session == null ||
            session.getAttribute("otp") == null ||
            session.getAttribute("resetUser") == null ||
            !"reset".equals(session.getAttribute("otpPurpose"))) {

            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        String username = (String) session.getAttribute("resetUser");

        // ❌ OTP sai hoặc rỗng
        if (enteredOtp == null || !enteredOtp.equals(sessionOtp)) {
            request.setAttribute("error", "❌ Invalid OTP. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // ✅ Bảo mật: kiểm tra OTP có đúng từ DB không
        AccountDAO dao = new AccountDAO();
        if (!dao.verifyOTP(username, enteredOtp)) {
            request.setAttribute("error", "❌ OTP mismatch or expired. Please request again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // ✅ Xác thực thành công → đánh dấu đã xác minh
        session.setAttribute("verifiedReset", true);

        // Chuyển đến trang đặt lại mật khẩu
        request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 🚫 Không cho phép truy cập trực tiếp GET
        response.sendRedirect(request.getContextPath() + "/forgot-password");
    }
}
