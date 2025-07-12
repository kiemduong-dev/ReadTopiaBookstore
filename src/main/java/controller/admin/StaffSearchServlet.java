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
 * StaffSearchServlet
 *
 * Handles searching staff list by keyword for admin. Only Admin is allowed.
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffSearchServlet", urlPatterns = {"/admin/staff/search"})
public class StaffSearchServlet extends HttpServlet {

    private final StaffDAO dao = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/staff/list");
            return;
        }

        List<StaffDTO> searchList = dao.searchStaffs(keyword.trim());
        request.setAttribute("staffs", searchList);
        request.setAttribute("keyword", keyword.trim());
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
    }
}
