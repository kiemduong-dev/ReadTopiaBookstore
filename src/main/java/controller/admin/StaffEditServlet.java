package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * StaffEditServlet
 *
 * Handles editing of staff information by admin. Supports both GET (form load)
 * and POST (form submit).
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffEditServlet", urlPatterns = {"/admin/staff/edit"})
public class StaffEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles HTTP GET request to load staff edit form.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String idParam = request.getParameter("staffID");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO staff = staffDAO.getStaffByID(staffID);

            if (staff == null) {
                response.sendRedirect("list");
                return;
            }

            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list");
        }
    }

    /**
     * Handles HTTP POST request to submit staff update.
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
        response.setContentType("text/html;charset=UTF-8");

        try {
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            String username = request.getParameter("username");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            Date dob = null;
            String dobParam = request.getParameter("dob");
            if (dobParam != null && !dobParam.trim().isEmpty()) {
                dob = Date.valueOf(dobParam.trim());
            }

            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int sex = Integer.parseInt(request.getParameter("sex"));
            int role = Integer.parseInt(request.getParameter("role"));
            String address = request.getParameter("address");

            StaffDTO staff = new StaffDTO();
            staff.setStaffID(staffID);
            staff.setUsername(username);
            staff.setFirstName(firstName);
            staff.setLastName(lastName);
            staff.setDob(dob);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setSex(sex);
            staff.setRole(role);
            staff.setAddress(address);
            staff.setAccStatus(1); // Default active
            staff.setCode(null);   // No verification code

            boolean updated = staffDAO.updateStaff(staff);

            if (updated) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update staff.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Invalid input. Please check all fields.");
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
        }
    }

    /**
     * Returns a brief description of this servlet.
     *
     * @return servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles editing of staff accounts by admin.";
    }
}
