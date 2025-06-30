package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * RegisterServlet – Handles user registration and OTP verification via email.
 * URL mapping: /register
 *
 * @author CE181518
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

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
     * Handle GET – Show register form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
    }

    /**
     * Handle POST – Validate fields, send OTP, store temp account
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
            // Validate input
            if (!ValidationUtil.isValidUsername(username)) {
                request.setAttribute("error", "Invalid username.");
            } else if (!ValidationUtil.isValidPassword(password)) {
                request.setAttribute("error", "Password must be 8+ characters, including uppercase, lowercase, digit and symbol.");
            } else if (!ValidationUtil.isConfirmPasswordMatch(password, confirmPassword)) {
                request.setAttribute("error", "Passwords do not match.");
            } else if (!ValidationUtil.isValidName(firstName)) {
                request.setAttribute("error", "Invalid first name.");
            } else if (!ValidationUtil.isValidName(lastName)) {
                request.setAttribute("error", "Invalid last name.");
            } else if (!ValidationUtil.isValidEmail(email)) {
                request.setAttribute("error", "Invalid email address.");
            } else if (!ValidationUtil.isValidPhone(phone)) {
                request.setAttribute("error", "Phone must be 10 digits and start with 03, 05, 07, 08, or 09.");
            } else if (!ValidationUtil.isValidAddress(address)) {
                request.setAttribute("error", "Address is required.");
            } else if (!ValidationUtil.isValidGender(gender)) {
                request.setAttribute("error", "Please select gender.");
            } else if (!ValidationUtil.isValidDob(dobRaw)) {
                request.setAttribute("error", "Invalid date of birth or under 13 years old.");
            }

            if (request.getAttribute("error") != null) {
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Convert values
            int sex = gender.equalsIgnoreCase("male") ? 1 : 0;
            Date dob = Date.valueOf(
                    LocalDate.parse(dobRaw, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            AccountDAO dao = new AccountDAO();

            // Check username/email uniqueness
            if (dao.getAccountByUsername(username) != null || dao.findByEmail(email) != null) {
                request.setAttribute("error", "Username or email already exists.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Create pending account
            AccountDTO pendingAccount = new AccountDTO(
                    username, password, firstName, lastName,
                    dob, email, phone, 1, address, sex, 1, null
            );

            // Send OTP
            String otp = generateOTP();
            MailUtil.sendOtp(email, otp);

            // Save to session
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "register");
            session.setAttribute("pendingAccount", pendingAccount);

            // Go to OTP page
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
        }
    }
}
