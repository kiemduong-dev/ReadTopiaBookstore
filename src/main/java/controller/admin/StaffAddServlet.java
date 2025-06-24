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

    /**
     * Handles HTTP GET to display the staff creation form.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    /**
     * Handles HTTP POST to process the submitted staff form.
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
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dobRaw = request.getParameter("dob");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String sexRaw = request.getParameter("sex");
        String roleRaw = request.getParameter("role");

        try {
            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;
            int sex = "male".equalsIgnoreCase(sexRaw) ? 1 : 0;

            int role;
            if (roleRaw != null) {
                switch (roleRaw.toLowerCase()) {
                    case "admin":
                        role = 0;
                        break;
                    case "seller staff":
                        role = 2;
                        break;
                    case "warehouse staff":
                        role = 3;
                        break;
                    case "staff":
                        role = 1;
                        break;
                    default:
                        role = 1;
                }
            } else {
                role = 1;
            }

            StaffDTO staff = new StaffDTO(
                    username, password, firstName, lastName, dob,
                    email, phone, role, address, sex, 1, null // accStatus = 1 (active)
            );

            boolean success = new StaffDAO().addStaff(staff);

            if (success) {
                session.setAttribute("message", "Staff added successfully.");
                response.sendRedirect("list");
                return;
            } else {
                request.setAttribute("error", "Failed to add staff. Username or email may already exist.");
                request.setAttribute("staff", staff);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input: " + e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    /**
     * Returns servlet description.
     *
     * @return servlet info
     */
    @Override
    public String getServletInfo() {
        return "Admin adds a new staff account.";
    }
}
