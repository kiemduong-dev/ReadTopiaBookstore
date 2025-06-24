package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * EditProfileServlet
 *
 * This servlet handles both displaying and updating a user's profile. Supports
 * GET (display form) and POST (handle update).
 *
 * URL Mapping: /edit-profile
 */
@WebServlet(name = "EditProfileServlet", urlPatterns = {"/edit-profile"})
public class EditProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Central request processing for both GET and POST.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        // Handle GET request: show the form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("user", account);
            request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
            return;
        }

        // Handle POST request: process form submission
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("sex");

            int sex = "male".equalsIgnoreCase(gender) ? 1 : 0;

            // Validate required fields
            if (firstName == null || email == null || dobStr == null
                    || firstName.isEmpty() || email.isEmpty() || dobStr.isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields.");
            } else {
                Date dob = Date.valueOf(dobStr);

                // Update account data
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
                    request.setAttribute("success", "Profile updated successfully.");
                } else {
                    request.setAttribute("error", "Failed to update profile. Please try again.");
                }
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An unexpected error occurred.");
        }

        // Forward to profile form with current user data
        request.setAttribute("user", account);
        request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
    }

    /**
     * Handles GET requests by displaying the edit profile form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST requests by updating the profile.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of this servlet.
     */
    @Override
    public String getServletInfo() {
        return "Handles editing and updating user profile information.";
    }
}
