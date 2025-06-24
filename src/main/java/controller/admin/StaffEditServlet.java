package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * StaffEditServlet
 *
 * Servlet xử lý việc sửa thông tin nhân viên bởi admin.
 */
@WebServlet(name = "StaffEditServlet", urlPatterns = {"/admin/staff/edit"})
public class StaffEditServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String idParam = request.getParameter("staffID"); // Nên đặt thống nhất param là "staffID"

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO staff = staffDAO.getStaffByID(staffID);

            if (staff == null) {
                response.sendRedirect("list");
                return;
            }

            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            String username = request.getParameter("username");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Date dob = null;
            String dobParam = request.getParameter("dob");
            if (dobParam != null && !dobParam.trim().isEmpty()) {
                dob = Date.valueOf(dobParam.trim());
            }
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            int sex = Integer.parseInt(request.getParameter("sex"));
            int role = Integer.parseInt(request.getParameter("role"));
            String address = request.getParameter("address");

            StaffDTO staff = new StaffDTO();
            staff.setStaffID(staffID);
            staff.setUsername(username);
            staff.setFirstName(firstName);
            staff.setLastName(lastName);
            staff.setDob(dob);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setSex(sex);
            staff.setRole(role);
            staff.setAddress(address);
            staff.setAccStatus(1); // Mặc định kích hoạt
            staff.setCode(null);   // Không có mã xác thực

            boolean updated = staffDAO.updateStaff(staff);

            if (updated) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update staff.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Invalid input. Please check all fields.");
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles editing of staff accounts by admin.";
    }
}
