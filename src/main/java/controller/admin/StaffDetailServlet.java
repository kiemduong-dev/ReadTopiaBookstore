package controller.admin;

import dao.StaffDAO;
import dto.AccountDTO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * StaffDetailServlet – Displays detailed information of a staff member based on staffID.
 * Accessible by:
 * - Admin (0): view role 1,2,3 (but not another Admin)
 * - Staff Manager (1): view role 2,3 (not another Staff)
 * - Seller (2) / Warehouse (3): view role 2,3
 * 
 * URL: /admin/staff/detail?staffID=...
 * 
 * @author CE181518
 */
@WebServlet(name = "StaffDetailServlet", urlPatterns = {"/admin/staff/detail"})
public class StaffDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (loginUser == null || loginUser.getRole() > 3) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized access.");
            return;
        }

        String idParam = request.getParameter("staffID");
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

            int viewerRole = loginUser.getRole();
            int targetRole = target.getRole();

            // ❌ Admin không được xem Admin khác
            if (viewerRole == 0 && targetRole == 0 && !loginUser.getUsername().equals(target.getUsername())) {
                session.setAttribute("message", "You are not allowed to view another Admin.");
                response.sendRedirect("list");
                return;
            }

            // ❌ Staff Manager không được xem Staff Manager khác
            if (viewerRole == 1 && targetRole == 1 && !loginUser.getUsername().equals(target.getUsername())) {
                session.setAttribute("message", "You cannot view another Staff Manager.");
                response.sendRedirect("list");
                return;
            }

            // ❌ Seller/Warehouse chỉ được xem role 2 và 3
            if ((viewerRole == 2 || viewerRole == 3) && (targetRole != 2 && targetRole != 3)) {
                session.setAttribute("message", "You can only view Seller and Warehouse Staff.");
                response.sendRedirect("list");
                return;
            }

            // ✅ Passed – Cho phép xem
            request.setAttribute("staff", target);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            session.setAttribute("message", "Invalid staff ID format.");
            response.sendRedirect("list");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "Error retrieving staff detail.");
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
        return "Displays detailed information of a staff member based on role access.";
    }
}
