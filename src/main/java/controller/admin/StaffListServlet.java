package controller.admin;

import dao.StaffDAO;
import dto.AccountDTO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * StaffListServlet – Displays the list of all active staff accounts based on
 * role. Admin (0), Staff Manager (1): Can view all (roles 1, 2, 3) Seller Staff
 * (2), Warehouse Staff (3): Can only view staff with roles 2 & 3.
 *
 * URL: /admin/staff/list
 *
 * @author CE181518
 */
@WebServlet(name = "StaffListServlet", urlPatterns = {"/admin/staff/list"})
public class StaffListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles GET request to show staff list by role.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // ❌ Not logged in or invalid role
        if (loginUser == null || loginUser.getRole() > 3) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to view this page.");
            return;
        }

        List<StaffDTO> staffList;
        int role = loginUser.getRole();

        try {
            if (role == 0 || role == 1) {
                // Admin hoặc Staff Manager: xem tất cả staff (role 1, 2, 3)
                staffList = staffDAO.findAllActiveStaff();
            } else {
                // Seller hoặc Warehouse: chỉ xem staff role 2 và 3
                staffList = staffDAO.findActiveSellerAndWarehouseStaff();
            }

            request.setAttribute("staffs", staffList);
            request.setAttribute("currentUser", loginUser);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load staff list: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
        }
    }
}
