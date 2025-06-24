package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;

import java.io.IOException;
import java.util.Random;

/**
 * ForgotPasswordServlet
 *
 * Handles the "Forgot Password" functionality by verifying the user's email,
 * generating an OTP, sending it to the email, and saving the OTP for later
 * verification.
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    /**
     * Generates a 6-digit random OTP.
     *
     * @return String representation of a 6-digit OTP
     */
    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    /**
     * Displays the forgot password form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
    }

    /**
     * Handles form submission to initiate OTP generation and email sending.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Please enter your email.");
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            AccountDAO dao = new AccountDAO();
            AccountDTO account = dao.findByEmail(email.trim());

            if (account == null) {
                request.setAttribute("error", "Email not found in the system.");
                request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
                return;
            }

            // Generate and send OTP
            String otp = generateOTP();
            MailUtil.sendOtp(email, otp);

            // Save OTP to database for verification
            dao.saveOTPForReset(account.getUsername(), otp);

            // Save relevant session attributes
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "reset");
            session.setAttribute("resetUser", account.getUsername());
            session.setAttribute("resetEmail", email);

            // Forward to OTP verification page
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to send OTP: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/forgotPassword.jsp").forward(request, response);
        }
    }
}
