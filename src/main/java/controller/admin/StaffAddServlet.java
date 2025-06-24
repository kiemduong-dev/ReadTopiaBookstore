package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * StaffAddServlet
 * Handles admin creation of new staff accounts.
 */
@WebServlet(name = "StaffAddServlet", urlPatterns = {"/admin/staff/add"})
public class StaffAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hiển thị form thêm staff
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String dobRaw = request.getParameter("dob");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String sexRaw = request.getParameter("sex");
        String roleRaw = request.getParameter("role");

        try {
            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;
            int sex = "male".equalsIgnoreCase(sexRaw) ? 1 : 0;

            // Chuẩn hóa role input về chữ thường để so sánh
            int role;
            if (roleRaw != null) {
                switch (roleRaw.toLowerCase()) {
                    case "admin":
                        role = 0;
                        break;
                    case "seller staff":
                        role = 2;
                        break;
                    case "warehouse staff":
                        role = 3;
                        break;
                    case "staff":
                        role = 1;
                        break;
                    default:
                        role = 1; // Mặc định role = Staff
                }
            } else {
                role = 1; // Mặc định nếu role không truyền lên
            }

            // Tạo đối tượng StaffDTO
            StaffDTO staff = new StaffDTO(username, password, firstName, lastName,
                    dob, email, phone, role, address, sex, 1, null); // accStatus = 1 (active)

            // Thêm staff qua DAO
            boolean success = new StaffDAO().addStaff(staff);

            if (success) {
                session.setAttribute("message", "✅ Staff added successfully.");
                response.sendRedirect("list");
                return;
            } else {
                request.setAttribute("error", "❌ Failed to add staff. Username or email may already exist.");
                request.setAttribute("staff", staff);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Invalid input: " + e.getMessage());
        }

        // Nếu lỗi thì trả lại form với dữ liệu cũ
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/add.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin adds a new staff account.";
    }
}
