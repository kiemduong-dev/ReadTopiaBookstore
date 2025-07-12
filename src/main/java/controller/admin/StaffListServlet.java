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
 * StaffListServlet
 *
 * Handles the display of staff list for admin. Shows all active Staff (role 1),
 * Seller Staff (role 2), and Warehouse Staff (role 3).
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffListServlet", urlPatterns = {"/admin/staff/list"})
public class StaffListServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            // Lấy toàn bộ nhân viên có role 1, 2, 3 và status = 1
            List<StaffDTO> staffList = staffDAO.findAllActiveStaffSellerWarehouse();
            request.setAttribute("staffs", staffList);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to load staff list: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
        }
    }
}
