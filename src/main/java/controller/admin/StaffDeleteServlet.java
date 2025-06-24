package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * StaffDeleteServlet
 *
 * Servlet xử lý xóa nhân viên (không xóa được admin - role != 0).
 */
@WebServlet(name = "StaffDeleteServlet", urlPatterns = {"/admin/staff/delete"})
public class StaffDeleteServlet extends HttpServlet {

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

                // Lấy thông tin nhân viên theo ID
                StaffDTO staff = dao.getStaffByID(staffID);

                // Chỉ cho phép xóa nếu staff tồn tại và không phải admin (role != 0)
                if (staff != null && staff.getRole() != 0) {
                    // Sử dụng method đúng để xóa cả staff và account
                    boolean deleted = dao.deleteStaffByID(staffID);

                    if (deleted) {
                        session.setAttribute("message", "Staff deleted successfully.");
                    } else {
                        session.setAttribute("message", "Failed to delete staff.");
                    }
                } else {
                    session.setAttribute("message", "Invalid staff or admin account cannot be deleted.");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("message", "Invalid staff ID format.");
            }
        } else {
            session.setAttribute("message", "Staff ID is required.");
        }

        // Quay lại trang danh sách nhân viên sau khi xử lý
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
        return "Handles deletion of staff accounts (excluding admin) by administrator.";
    }
}
