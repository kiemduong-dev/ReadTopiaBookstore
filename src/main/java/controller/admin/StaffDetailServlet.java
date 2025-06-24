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
 * Servlet hiển thị chi tiết nhân viên dựa trên staffID.
 */
@WebServlet(name = "StaffDetailServlet", urlPatterns = {"/admin/staff/detail"})
public class StaffDetailServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Đặt encoding chuẩn cho request và response
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Lấy tham số 'staffID' từ URL
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Dùng chung logic cho GET và POST
    }

    @Override
    public String getServletInfo() {
        return "Displays detailed information of a staff member based on staffID.";
    }
}
