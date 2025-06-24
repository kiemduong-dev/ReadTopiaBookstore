package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * StaffDetailServlet
 *
 * Handles the display of staff detail information for administrators. Retrieves
 * staff information based on the provided staffID.
 *
 * URL mapping: /admin/staff/detail Method supported: GET, POST
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffDetailServlet", urlPatterns = {"/admin/staff/detail"})
public class StaffDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles GET requests for viewing staff detail.
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
            request.getSession().setAttribute("message", "Staff ID is missing.");
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO staff = staffDAO.getStaffByID(staffID);

            if (staff != null) {
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/detail.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("message", "Staff not found.");
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

    /**
     * Handles POST request (delegates to doGet).
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns servlet description.
     *
     * @return brief description of this servlet
     */
    @Override
    public String getServletInfo() {
        return "Displays detailed information of a staff member based on staffID.";
    }
}
