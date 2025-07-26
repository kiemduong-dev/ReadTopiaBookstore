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
import java.time.format.DateTimeParseException;

/**
 * StaffAddServlet – Handles staff creation (only Seller or Warehouse Staff)
 * Only Staff (role = 1) is allowed to access this function. Admin cannot add staff.
 *
 * URL: /admin/staff/add
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffAddServlet", urlPatterns = {"/admin/staff/add"})
public class StaffAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (loginUser == null || loginUser.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this page.");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (loginUser == null || loginUser.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to add staff.");
            return;
        }

        // Get form data
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String confirmPassword = request.getParameter("confirmPassword").trim();
        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        String dobRaw = request.getParameter("dob").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        String sexStr = request.getParameter("sex");
        String roleStr = request.getParameter("role");

        // Validate format
        if (!ValidationUtil.isValidUsername(username)
                || !ValidationUtil.isValidPassword(password)
                || !ValidationUtil.isConfirmPasswordMatch(password, confirmPassword)
                || !ValidationUtil.isValidName(firstName)
                || !ValidationUtil.isValidName(lastName)
                || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.isValidPhone(phone)
                || !ValidationUtil.isValidAddress(address)
                || !ValidationUtil.isValidGender(sexStr)
                || !ValidationUtil.isValidDob(dobRaw)) {

            request.setAttribute("error", "Invalid input. Please check all fields carefully.");
            bindStaffToRequest(request, username, password, firstName, lastName, null,
                    email, phone, safeParseInt(roleStr), address, safeParseInt(sexStr));
            request.setAttribute("dobRaw", dobRaw);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
            return;
        }

        try {
            int role = Integer.parseInt(roleStr);
            int sex = Integer.parseInt(sexStr);

            if (role != 2 && role != 3) {
                request.setAttribute("error", "Only Seller or Warehouse Staff can be added.");
                bindStaffToRequest(request, username, password, firstName, lastName, null,
                        email, phone, role, address, sex);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
                return;
            }

            if (staffDAO.findByUsername(username) != null || staffDAO.findByEmail(email) != null) {
                request.setAttribute("error", "Username or Email already exists.");
                bindStaffToRequest(request, username, password, firstName, lastName, null,
                        email, phone, role, address, sex);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
                return;
            }

            LocalDate dobLocal = LocalDate.parse(dobRaw, formatter);
            Date dob = Date.valueOf(dobLocal);

            StaffDTO staff = new StaffDTO(username, password, firstName, lastName,
                    dob, email, phone, role, address, sex, 1, null);

            if (staffDAO.addStaff(staff)) {
                session.setAttribute("message", "Staff added successfully.");
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to add staff. Please try again.");
                request.setAttribute("staff", staff);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
            }

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid date format. Use dd/MM/yyyy.");
            request.setAttribute("dobRaw", dobRaw);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
        }
    }

    private void bindStaffToRequest(HttpServletRequest request, String username, String password, String firstName,
                                    String lastName, Date dob, String email, String phone, int role,
                                    String address, int sex) {
        StaffDTO staff = new StaffDTO(username, password, firstName, lastName,
                dob, email, phone, role, address, sex, 1, null);
        request.setAttribute("staff", staff);
    }

    private int safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles creation of seller/warehouse staff accounts by authorized staff only.";
    }
}
