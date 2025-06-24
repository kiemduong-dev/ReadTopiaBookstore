package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;

import java.io.IOException;
import java.util.Random;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "⚠️ Please enter your email.");
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO dao = new AccountDAO();
            AccountDTO account = dao.findByEmail(email.trim());

            if (account == null) {
                request.setAttribute("error", "❌ Email not found in the system.");
                request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Gửi OTP
            String otp = generateOTP();
            MailUtil.sendOTP(email, otp);

            // Lưu OTP vào DB
            dao.saveOTPForReset(account.getUsername(), otp);

            // Lưu session phục vụ bước xác minh OTP
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "reset");
            session.setAttribute("resetUser", account.getUsername());
            session.setAttribute("resetEmail", email);

            // Điều hướng đến trang xác minh OTP
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Failed to send OTP: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
        }
    }
}
