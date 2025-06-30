package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * VerifyOTPResetServlet – Handles OTP verification during forgot password flow.
 * If OTP is valid, user is redirected to the password reset form.
 *
 * URL mapping: /verify-otp-reset Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "VerifyOTPResetServlet", urlPatterns = {"/verify-otp-reset"})
public class VerifyOTPResetServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles POST request to validate the OTP and redirect to password reset
     * page
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException in case of servlet error
     * @throws IOException in case of I/O error
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // Do not create new session

        // Step 1: Check session validity
        if (session == null
                || session.getAttribute("otp") == null
                || session.getAttribute("resetUser") == null
                || !"reset".equals(session.getAttribute("otpPurpose"))) {

            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        // Step 2: Retrieve OTP & user input
        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        String username = (String) session.getAttribute("resetUser");

        // Step 3: Validate OTP format and match
        if (enteredOtp == null || !enteredOtp.matches("\\d{6}") || !enteredOtp.equals(sessionOtp)) {
            request.setAttribute("error", "Invalid OTP. Please enter the correct 6-digit code.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // Step 4: Check OTP with database (extra security)
        AccountDAO dao = new AccountDAO();
        if (!dao.verifyOTP(username, enteredOtp)) {
            request.setAttribute("error", "OTP mismatch or expired. Please request a new one.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // Step 5: Valid OTP → allow password reset
        session.setAttribute("verifiedReset", true);

        // Forward to password reset page
        request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
    }

    /**
     * Deny direct GET access to OTP verification
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/forgot-password");
    }
}
