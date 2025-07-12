package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * StaffAddServlet
 *
 * Handles staff creation by admin via form submission. Supports GET (load form)
 * and POST (submit form).
 *
 * URL mapping: /admin/staff/add
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffAddServlet", urlPatterns = {"/admin/staff/add"})
public class StaffAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        String dobRaw = request.getParameter("dob");
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        int sex = Integer.parseInt(request.getParameter("sex"));
        int role = Integer.parseInt(request.getParameter("role"));

        try {
            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;

            StaffDTO staff = new StaffDTO(
                    username, password, firstName, lastName, dob,
                    email, phone, role, address, sex, 1, null
            );

            boolean exists = staffDAO.findByUsername(username) != null || staffDAO.findByEmail(email) != null;

            if (exists) {
                request.setAttribute("error", "Username or email already exists.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
                return;
            }

            boolean success = staffDAO.addStaff(staff);

            if (success) {
                session.setAttribute("message", "Staff added successfully.");
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to add staff. Please try again.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Admin adds a new staff account.";
    }
}
