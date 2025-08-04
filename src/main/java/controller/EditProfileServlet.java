package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * EditProfileServlet – Handles displaying and updating user's profile.
 * Supports GET (view form) and POST (handle update).
 *
 * URL Mapping: /edit-profile
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "EditProfileServlet", urlPatterns = {"/edit-profile"})
public class EditProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Show profile edit form (GET)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("user", account);
        request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
    }

    /**
     * Handle profile update (POST)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Get input
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email"); // hidden input
            String phone = request.getParameter("phone"); // hidden input
            String address = request.getParameter("address");
            String dobStr = request.getParameter("dob"); // Expected: dd/MM/yyyy
            String gender = request.getParameter("sex");

            int sex = Integer.parseInt(gender);

            // Validate
            if (!ValidationUtil.isValidName(firstName)) {
                request.setAttribute("error", "Invalid first name.");
            } else if (!ValidationUtil.isValidName(lastName)) {
                request.setAttribute("error", "Invalid last name.");
            } else if (!ValidationUtil.isValidEmail(email)) {
                request.setAttribute("error", "Invalid email format.");
            } else if (!ValidationUtil.isValidPhone(phone)) {
                request.setAttribute("error", "Invalid phone number.");
            } else if (!ValidationUtil.isValidDob(dobStr)) {
                request.setAttribute("error", "Invalid date of birth or must be 13+.");
            } else if (!ValidationUtil.isValidGender(gender)) {
                request.setAttribute("error", "Please select gender.");
            } else if (!ValidationUtil.isValidAddress(address)) {
                request.setAttribute("error", "Address is required.");
            } else {
                // Parse and update
                LocalDate parsedDob = LocalDate.parse(dobStr, DOB_FORMATTER);
                Date dob = Date.valueOf(parsedDob);

                account.setFirstName(firstName.trim());
                account.setLastName(lastName.trim());
                account.setEmail(email.trim());
                account.setPhone(phone.trim());
                account.setAddress(address.trim());
                account.setSex(sex);
                account.setDob(dob);

                AccountDAO dao = new AccountDAO();
                boolean updated = dao.updateProfile(account);

                if (updated) {
                    session.setAttribute("account", account);
                    request.setAttribute("success", "Profile updated successfully.");
                } else {
                    request.setAttribute("error", "Failed to update profile. Please try again.");
                }
            }

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Date of birth must be in dd/MM/yyyy format.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }

        request.setAttribute("user", account);
        request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles displaying and updating user profile information.";
    }
}
