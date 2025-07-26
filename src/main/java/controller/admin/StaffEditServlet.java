package controller.admin;

import dao.StaffDAO;
import dto.AccountDTO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * StaffEditServlet – Handles editing staff info with role-based restrictions.
 * Only Staff Manager (role 1) can edit. Can only edit themselves or users with
 * role 2/3. Cannot edit Admin or Customer.
 *
 * URL: /admin/staff/edit
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffEditServlet", urlPatterns = {"/admin/staff/edit"})
public class StaffEditServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (loginUser == null || loginUser.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String idParam = request.getParameter("staffID");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO target = staffDAO.getStaffByID(staffID);

            if (target == null || target.getRole() == 0 || target.getRole() == 4) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            boolean isSelf = loginUser.getUsername().equals(target.getUsername());
            if (target.getRole() == 1 && !isSelf) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            request.setAttribute("staff", target);
            request.setAttribute("dobRaw", target.getDob() != null
                    ? target.getDob().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "");
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (loginUser == null || loginUser.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            StaffDTO original = staffDAO.getStaffByID(staffID);

            if (original == null || original.getRole() == 0 || original.getRole() == 4) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            boolean isSelf = loginUser.getUsername().equals(original.getUsername());
            if (original.getRole() == 1 && !isSelf) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // Retrieve and validate inputs
            String username = request.getParameter("username").trim();
            String firstName = request.getParameter("firstName").trim();
            String lastName = request.getParameter("lastName").trim();
            String dobRaw = request.getParameter("dob").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();
            int sex = Integer.parseInt(request.getParameter("sex"));
            int newRole = Integer.parseInt(request.getParameter("role"));

            Map<String, String> fieldErrors = new HashMap<>();

            if (!ValidationUtil.isValidName(firstName)) {
                fieldErrors.put("firstName", "First name is invalid.");
            }
            if (!ValidationUtil.isValidName(lastName)) {
                fieldErrors.put("lastName", "Last name is invalid.");
            }
            if (!ValidationUtil.isValidEmail(email)) {
                fieldErrors.put("email", "Email format is invalid.");
            }
            if (!ValidationUtil.isValidPhone(phone)) {
                fieldErrors.put("phone", "Phone number is invalid.");
            }
            if (!ValidationUtil.isValidAddress(address)) {
                fieldErrors.put("address", "Address cannot be empty.");
            }
            if (!ValidationUtil.isValidGender(String.valueOf(sex))) {
                fieldErrors.put("sex", "Gender is invalid.");
            }
            if (!ValidationUtil.isValidDob(dobRaw)) {
                fieldErrors.put("dob", "Date of birth must be dd/MM/yyyy and at least 13 years old.");
            }

            // Check role update validity
            if (!isSelf && (newRole != 2 && newRole != 3)) {
                fieldErrors.put("role", "Only Seller or Warehouse Staff roles are allowed.");
            }

            // If any field error → return to form
            if (!fieldErrors.isEmpty()) {
                request.setAttribute("fieldErrors", fieldErrors);
                request.setAttribute("staff", original);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
                return;
            }

            // Build DTO and update
            LocalDate dobDate = LocalDate.parse(dobRaw, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            StaffDTO updated = new StaffDTO();
            updated.setStaffID(staffID);
            updated.setUsername(username);
            updated.setFirstName(firstName);
            updated.setLastName(lastName);
            updated.setDob(Date.valueOf(dobDate));
            updated.setEmail(email);
            updated.setPhone(phone);
            updated.setSex(sex);
            updated.setRole(newRole);
            updated.setAddress(address);
            updated.setAccStatus(1);

            boolean success = staffDAO.updateStaff(updated);

            if (success) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update staff.");
                request.setAttribute("staff", updated);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unexpected error occurred.");
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
        }
    }
}
