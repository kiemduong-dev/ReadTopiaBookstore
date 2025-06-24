package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * StaffDeleteServlet
 *
 * Handles deletion of staff accounts by admin. Only non-admin (role != 0) staff
 * accounts can be deleted.
 *
 * URL mapping: /admin/staff/delete Method: GET or POST
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffDeleteServlet", urlPatterns = {"/admin/staff/delete"})
public class StaffDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO dao = new StaffDAO();

    /**
     * Handles both GET and POST requests to delete a staff member.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int staffID = Integer.parseInt(idParam.trim());
                StaffDTO staff = dao.getStaffByID(staffID);

                // Allow deletion only if staff exists and is not an admin
                if (staff != null && staff.getRole() != 0) {
                    boolean deleted = dao.deleteStaffByID(staffID);

                    if (deleted) {
                        session.setAttribute("message", "Staff deleted successfully.");
                    } else {
                        session.setAttribute("message", "Failed to delete staff.");
                    }
                } else {
                    session.setAttribute("message", "Invalid staff or admin account cannot be deleted.");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("message", "Invalid staff ID format.");
            }
        } else {
            session.setAttribute("message", "Staff ID is required.");
        }

        // Redirect to staff list page after processing
        response.sendRedirect("list");
    }

    /**
     * Handles the HTTP GET method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns servlet description.
     *
     * @return servlet info string
     */
    @Override
    public String getServletInfo() {
        return "Handles deletion of staff accounts (excluding admin) by administrator.";
    }
}
