package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;

import java.io.IOException;
import java.sql.Date;
import java.util.Random;

/**
 * RegisterServlet
 *
 * Handles user registration and OTP verification via email.
 *
 * URL mapping: /register
 *
 * @author CE181518
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Generates a 6-digit OTP string.
     *
     * @return the OTP code as a string
     */
    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    /**
     * Handles GET request: Displays the registration form.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
    }

    /**
     * Handles POST request: Validates form, sends OTP, stores temporary account
     * in session.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String dobRaw = request.getParameter("dob");
        String gender = request.getParameter("sex");

        try {
            // Validate required fields
            if (username == null || password == null || confirmPassword == null || email == null
                    || firstName == null || lastName == null || dobRaw == null) {
                request.setAttribute("error", "Please fill in all required fields.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Confirm password match
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Parse date of birth
            Date dob;
            try {
                dob = Date.valueOf(dobRaw);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid date format.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            int sex = "male".equalsIgnoreCase(gender) ? 1 : 0;

            AccountDAO dao = new AccountDAO();

            // Check if username or email already exists
            if (dao.getAccountByUsername(username) != null || dao.findByEmail(email) != null) {
                request.setAttribute("error", "Username or email already exists.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Create pending account
            AccountDTO pendingAccount = new AccountDTO(
                    username, password, firstName, lastName,
                    dob, email, phone,
                    1, address, sex, 1, null
            );

            // Generate OTP and send via email
            String otp = generateOTP();
            MailUtil.sendOtp(email, otp);

            // Store data in session for verification
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "register");
            session.setAttribute("pendingAccount", pendingAccount);

            // Redirect to OTP verification
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
        }
    }
}
