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
 * StaffSearchServlet – Handles search requests for staff accounts.
 * Allows Admin (0), Staff Manager (1), Seller Staff (2), and Warehouse Staff (3)
 * to search staff accounts based on keyword.
 *
 * URL: /admin/staff/search?keyword=...
 *
 * @author CE181518
 */
@WebServlet(name = "StaffSearchServlet", urlPatterns = {"/admin/staff/search"})
public class StaffSearchServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final StaffDAO staffDAO = new StaffDAO();

    /**
     * Handles GET requests for staff search.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO loginUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // ❌ Unauthorized access
        if (loginUser == null || loginUser.getRole() > 3) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You are not allowed to search staff.");
            return;
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/staff/list");
            return;
        }

        keyword = keyword.trim();
        List<StaffDTO> result;

        int role = loginUser.getRole();
        switch (role) {
            case 0:
            case 1:
                // Admin hoặc Staff Manager: search toàn bộ staff
                result = staffDAO.searchStaffs(keyword); // roles 1,2,3
                break;
            case 2:
            case 3:
                // Seller hoặc Warehouse: chỉ được search staff role 2 & 3
                result = staffDAO.searchStaffsByStaffRole(keyword); // roles 2,3
                break;
            default:
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid role.");
                return;
        }

        request.setAttribute("staffs", result);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentUser", loginUser);
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
    }
}
