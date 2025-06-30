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
 * EditProfileServlet – Handles displaying and updating user's profile. Supports
 * GET (view form) and POST (handle update).
 *
 * URL Mapping: /edit-profile Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "EditProfileServlet", urlPatterns = {"/edit-profile"})
public class EditProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handle GET/POST logic
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (account == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // GET → Hiển thị form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("user", account);
            request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
            return;
        }

        // POST → Xử lý cập nhật
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("sex");

            int sex = "male".equalsIgnoreCase(gender) ? 1 : 0;

            // Validate input
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
                // Parse date dd/MM/yyyy
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate parsedDob = LocalDate.parse(dobStr, formatter);
                Date dob = Date.valueOf(parsedDob);

                // Update DTO
                account.setFirstName(firstName);
                account.setLastName(lastName);
                account.setEmail(email);
                account.setPhone(phone);
                account.setAddress(address);
                account.setSex(sex);
                account.setDob(dob);

                AccountDAO dao = new AccountDAO();
                boolean updated = dao.updateProfile(account);

                if (updated) {
                    session.setAttribute("account", account); // Update session
                    request.setAttribute("success", "✅ Profile updated successfully.");
                } else {
                    request.setAttribute("error", "❌ Failed to update profile. Please try again.");
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

    /**
     * Handle GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handle POST request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles displaying and updating user profile information.";
    }
}
