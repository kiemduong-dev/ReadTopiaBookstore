package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "VerifyOTPRegisterServlet", urlPatterns = {"/verify-otp-register"})
public class VerifyOTPRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // ❗ Không tạo mới

        // ❌ Session hoặc dữ liệu không hợp lệ
        if (session == null ||
            session.getAttribute("otp") == null ||
            session.getAttribute("pendingAccount") == null ||
            !"register".equals(session.getAttribute("otpPurpose"))) {

            request.setAttribute("error", "⚠️ Session expired or invalid. Please register again.");
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
            return;
        }

        // ✅ Lấy OTP và so sánh
        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");

        if (enteredOtp == null || !enteredOtp.equals(sessionOtp)) {
            request.setAttribute("error", "❌ Invalid OTP. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            return;
        }

        try {
            // ✅ OTP đúng → tiến hành tạo tài khoản
            AccountDTO pendingAccount = (AccountDTO) session.getAttribute("pendingAccount");
            AccountDAO dao = new AccountDAO();

            boolean added = dao.addAccount(pendingAccount);

            if (added) {
                // 🧹 Dọn session sau khi đăng ký
                session.removeAttribute("otp");
                session.removeAttribute("otpPurpose");
                session.removeAttribute("pendingAccount");

                request.setAttribute("success", "🎉 Registration successful! You can now log in.");
                request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "❌ Failed to create account. Please try again.");
                request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Server error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
        }
    }
}
