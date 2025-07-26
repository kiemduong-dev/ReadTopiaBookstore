package controller.admin;

import dao.StaffDAO;
import dto.AccountDTO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * StaffDeleteServlet – Handles deletion of seller and warehouse staff accounts by Staff role (1).
 * Only Staff role (1) can delete staff with role 2 or 3.
 * Admin cannot delete any staff.
 * Staff cannot delete staff role 1 (including themselves).
 * 
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffDeleteServlet", urlPatterns = {"/admin/staff/delete"})
public class StaffDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles staff deletion request via GET/POST method.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // ❌ Must be Staff (role = 1)
        if (loginUser == null || loginUser.getRole() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Only Staff can delete.");
            return;
        }

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("message", "Staff ID is required.");
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO target = staffDAO.getStaffByID(staffID);

            if (target == null) {
                session.setAttribute("message", "Staff not found.");
                response.sendRedirect("list");
                return;
            }

            int targetRole = target.getRole();

            // ❌ Cannot delete Admin or Staff (role 0 or 1)
            if (targetRole == 0 || targetRole == 1) {
                session.setAttribute("message", "You are not allowed to delete Admin or Staff.");
                response.sendRedirect("list");
                return;
            }

            // ❌ Cannot delete self
            if (target.getUsername().equals(loginUser.getUsername())) {
                session.setAttribute("message", "You cannot delete your own account.");
                response.sendRedirect("list");
                return;
            }

            // ✅ Proceed deletion
            boolean deleted = staffDAO.deleteStaffByID(staffID);
            if (deleted) {
                session.setAttribute("message", "Staff deleted successfully.");
            } else {
                session.setAttribute("message", "Failed to delete staff. Please try again.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid staff ID format.");
        } catch (Exception e) {
            session.setAttribute("message", "Unexpected error: " + e.getMessage());
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
        return "Handles deletion of seller/warehouse staff by Staff role only.";
    }
}
