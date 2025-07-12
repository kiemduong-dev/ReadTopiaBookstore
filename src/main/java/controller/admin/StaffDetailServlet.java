package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "StaffDetailServlet", urlPatterns = {"/admin/staff/detail"})
public class StaffDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String idParam = request.getParameter("staffID");

        if (idParam == null || idParam.trim().isEmpty()) {
            request.getSession().setAttribute("message", "Staff ID is required.");
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO staff = staffDAO.getStaffByID(staffID);

            if (staff != null && (staff.getRole() == 2 || staff.getRole() == 3)) {
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/detail.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("message", "Staff not found or invalid role.");
                response.sendRedirect("list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid staff ID format.");
            response.sendRedirect("list");

        } catch (Exception e) {
            request.getSession().setAttribute("message", "Error retrieving staff detail.");
            response.sendRedirect("list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays detailed information of a seller or warehouse staff member based on staffID.";
    }
}
