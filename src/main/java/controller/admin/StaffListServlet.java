package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * StaffListServlet
 *
 * Servlet hiển thị danh sách nhân viên cho Admin, hỗ trợ tìm kiếm theo keyword.
 */
@WebServlet(name = "StaffListServlet", urlPatterns = {"/admin/staff/list"})
public class StaffListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Lấy từ khóa tìm kiếm (keyword) nếu có
        String keyword = request.getParameter("keyword");

        StaffDAO dao = new StaffDAO();
        List<StaffDTO> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm theo keyword
            list = dao.searchStaffs(keyword.trim());
            // Đưa keyword lên JSP để giữ giá trị tìm kiếm
            request.setAttribute("keyword", keyword.trim());
        } else {
            // Lấy tất cả nhân viên nếu không có từ khóa
            list = dao.findAll();
        }

        // Đưa danh sách nhân viên lên JSP
        request.setAttribute("staffs", list);
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
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
        return "Admin - Quản lý danh sách nhân viên với chức năng tìm kiếm.";
    }
}
