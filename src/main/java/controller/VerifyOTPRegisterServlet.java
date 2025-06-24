package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * VerifyOTPRegisterServlet
 *
 * Handles OTP verification during the registration process. On successful OTP
 * match, the pending account is saved to the database.
 *
 * URL mapping: /verify-otp-register
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "VerifyOTPRegisterServlet", urlPatterns = {"/verify-otp-register"})
public class VerifyOTPRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles POST request for verifying OTP during registration. If
     * successful, creates the new account and forwards to login page.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false); // Do not create new session

        // Validate session and expected attributes
        if (session == null
                || session.getAttribute("otp") == null
                || session.getAttribute("pendingAccount") == null
                || !"register".equals(session.getAttribute("otpPurpose"))) {

            request.setAttribute("error", "Session expired or invalid. Please register again.");
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
            return;
        }

        String enteredOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("otp");

        if (enteredOtp == null || !enteredOtp.equals(sessionOtp)) {
            request.setAttribute("error", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            return;
        }

        try {
            AccountDTO pendingAccount = (AccountDTO) session.getAttribute("pendingAccount");
            AccountDAO dao = new AccountDAO();
            boolean added = dao.addAccount(pendingAccount);

            if (added) {
                session.removeAttribute("otp");
                session.removeAttribute("otpPurpose");
                session.removeAttribute("pendingAccount");

                request.setAttribute("success", "Registration successful! You can now log in.");
                request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to create account. Please try again.");
                request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Server error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);
        }
    }
}
