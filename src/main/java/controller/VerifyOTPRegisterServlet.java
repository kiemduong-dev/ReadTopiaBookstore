package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * VerifyOTPRegisterServlet – Handles OTP verification during registration. If
 * OTP is valid, the pending account is stored in the database.
 *
 * URL mapping: /verify-otp-register
 *
 * @author CE181518 Dương An
 */
@WebServlet(name = "VerifyOTPRegisterServlet", urlPatterns = {"/verify-otp-register"})
public class VerifyOTPRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles OTP validation during user registration.
     *
     * Flow: 1. Validate session and required attributes 2. Compare user input
     * OTP with session OTP 3. If match → persist account to DB 4. If success →
     * clear session and redirect to login 5. If fail → return error to verify
     * page
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // Do not create new session

        // Step 1: Validate session and OTP purpose
        if (session == null
                || session.getAttribute("otp") == null
                || session.getAttribute("pendingAccount") == null
                || !"register".equals(session.getAttribute("otpPurpose"))) {

            request.setAttribute("error", "Session expired or invalid. Please register again.");
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
            return;
        }

        // Step 2: Get and compare OTP
        String enteredOtp = request.getParameter("otp");
        String expectedOtp = (String) session.getAttribute("otp");

        if (enteredOtp == null || !enteredOtp.equals(expectedOtp)) {
            request.setAttribute("error", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            return;
        }

        // Step 3: Persist account to DB
        try {
            AccountDTO pendingAccount = (AccountDTO) session.getAttribute("pendingAccount");
            AccountDAO dao = new AccountDAO();

            boolean success = dao.addAccount(pendingAccount);

            if (success) {
                // Step 4: Clear session-related OTP
                session.removeAttribute("otp");
                session.removeAttribute("otpPurpose");
                session.removeAttribute("pendingAccount");

                request.setAttribute("success", "✅ Registration successful. You can now log in.");
                request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "❌ Failed to create account. Please try again.");
                request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "🚨 Server error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
        }
    }
}
