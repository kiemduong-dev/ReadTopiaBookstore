package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.util.Random;

/**
 * ForgotPasswordServlet – Handles "Forgot Password" flow by: 1. Verifying
 * username & email 2. Generating OTP and sending email 3. Saving OTP for later
 * verification
 *
 * URL mapping: /forgot-password Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Generate 6-digit OTP
     *
     * @return String OTP
     */
    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    /**
     * Display forgot password form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
    }

    /**
     * Handle form submission: validate input, verify account, send OTP
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // Step 1: Input validation
        if (username == null || username.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both username and email.");
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidUsername(username) || !ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Invalid username or email format.");
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO dao = new AccountDAO();
            AccountDTO account = dao.getAccountByUsername(username.trim());

            // Step 2: Account existence + email match
            if (account == null || !account.getEmail().equalsIgnoreCase(email.trim())) {
                request.setAttribute("error", "Username and email do not match any account.");
                request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Step 3: Generate & send OTP
            String otp = generateOTP();
            MailUtil.sendOtp(email.trim(), otp);

            // Step 4: Save OTP in database
            dao.saveOTPForReset(username.trim(), otp);

            // Step 5: Save session info
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "reset");
            session.setAttribute("resetUser", username.trim());
            session.setAttribute("resetEmail", email.trim());

            // Step 6: Forward to OTP verification page
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to send OTP: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
        }
    }
}
