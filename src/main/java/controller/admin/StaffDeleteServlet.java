package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "StaffDeleteServlet", urlPatterns = {"/admin/staff/delete"})
public class StaffDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO dao = new StaffDAO();

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

                if (staff != null && (staff.getRole() == 2 || staff.getRole() == 3)) {
                    boolean deleted = dao.deleteStaffByID(staffID);
                    if (deleted) {
                        session.setAttribute("message", "Staff deleted successfully.");
                    } else {
                        session.setAttribute("message", "Failed to delete staff.");
                    }
                } else {
                    session.setAttribute("message", "Invalid staff or unauthorized role deletion attempt.");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("message", "Invalid staff ID format.");
            }
        } else {
            session.setAttribute("message", "Staff ID is required.");
        }

        response.sendRedirect("list");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles deletion of seller and warehouse staff accounts by administrator.";
    }
}
