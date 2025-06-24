package controller;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * VerifyOTPResetServlet
 *
 * Handles the OTP verification during the forgot password process. Ensures
 * session validity, OTP correctness, and transitions to password reset view.
 *
 * URL mapping: /verify-otp-reset
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "VerifyOTPResetServlet", urlPatterns = {"/verify-otp-reset"})
public class VerifyOTPResetServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles HTTP POST request to verify the OTP code submitted by user. If
     * valid, redirects to reset password form.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // Do not create a new session if not exists

        // Validate session and necessary attributes
        if (session == null
                || session.getAttribute("otp") == null
                || session.getAttribute("resetUser") == null
                || !"reset".equals(session.getAttribute("otpPurpose"))) {

            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }

        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");
        String username = (String) session.getAttribute("resetUser");

        // Validate OTP from form input
        if (enteredOtp == null || !enteredOtp.equals(sessionOtp)) {
            request.setAttribute("error", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // Secure validation: check OTP in database
        AccountDAO dao = new AccountDAO();
        if (!dao.verifyOTP(username, enteredOtp)) {
            request.setAttribute("error", "OTP mismatch or expired. Please request again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-reset.jsp").forward(request, response);
            return;
        }

        // Mark session as verified
        session.setAttribute("verifiedReset", true);

        // Forward to reset password page
        request.getRequestDispatcher("/WEB-INF/view/account/resetPassword.jsp").forward(request, response);
    }

    /**
     * Prevent direct access via GET method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/forgot-password");
    }
}
