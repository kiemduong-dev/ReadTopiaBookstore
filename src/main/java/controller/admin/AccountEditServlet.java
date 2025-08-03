package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * AccountEditServlet – Handles editing of user accounts by admin.
 * Only Admin can edit roles 1 (Staff Manager) and 4 (Customer).
 * Cannot edit Admins or roles 2/3 (Seller/Warehouse).
 *
 * URL: /admin/account/edit
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/admin/account/edit"})
public class AccountEditServlet extends HttpServlet {

    private final AccountDAO dao = new AccountDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        AccountDTO currentUser  = (AccountDTO) session.getAttribute("account");

        // Kiểm tra quyền truy cập của người dùng
        if (currentUser  == null || currentUser .getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username");
        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        AccountDTO target = dao.getAccountByUsername(username.trim());
        // Kiểm tra xem tài khoản có thể chỉnh sửa hay không
        if (target == null || target.getRole() == 0 || target.getRole() == 2 || target.getRole() == 3) {
            session.setAttribute("message", "You cannot edit this account.");
            response.sendRedirect("list");
            return;
        }

        // Định dạng ngày sinh trước khi hiển thị
        String formattedDob = dateFormat.format(target.getDob());
        request.setAttribute("account", target);
        request.setAttribute("dobRaw", formattedDob);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        AccountDTO currentUser  = (AccountDTO) session.getAttribute("account");

        // Kiểm tra quyền truy cập của người dùng
        if (currentUser  == null || currentUser .getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username").trim();
        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        String dobRaw = request.getParameter("dob");
        String roleStr = request.getParameter("role");
        String sexStr = request.getParameter("sex");

        // Validate input
        if (!ValidationUtil.isValidUsername(username)
                || !ValidationUtil.isValidName(firstName)
                || !ValidationUtil.isValidName(lastName)
                || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.isValidPhone(phone)
                || !ValidationUtil.isValidAddress(address)
                || !ValidationUtil.isValidGender(sexStr)
                || !ValidationUtil.isValidDob(dobRaw)) {

            request.setAttribute("error", "Invalid input. Please check all fields.");
            request.setAttribute("account", new AccountDTO(username, null, firstName, lastName, null, email, phone,
                    parseInt(roleStr), address, parseInt(sexStr), 1, null));
            request.setAttribute("dobRaw", dobRaw);
            request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
            return;
        }

        try {
            int role = Integer.parseInt(roleStr);
            int sex = Integer.parseInt(sexStr);
            Date dob = new Date(dateFormat.parse(dobRaw).getTime()); // Sử dụng SimpleDateFormat để phân tích

            // Kiểm tra vai trò có thể chỉnh sửa
            if (role != 1 && role != 4) {
                request.setAttribute("error", "Only Staff Manager or Customer roles are editable.");
                request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
                return;
            }

            AccountDTO target = dao.getAccountByUsername(username);
            if (target == null || target.getRole() == 0 || target.getRole() == 2 || target.getRole() == 3) {
                session.setAttribute("message", "You cannot edit this account.");
                response.sendRedirect("list");
                return;
            }

            // Cập nhật thông tin tài khoản
            AccountDTO updated = new AccountDTO(username, null, firstName, lastName, dob, email, phone,
                    role, address, sex, target.getAccStatus(), null);

            boolean success = dao.updateAccountByAdmin(updated);

            if (success) {
                 session.setAttribute("message", "Account \"" + username + "\" updated successfully.");
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update account.");
                request.setAttribute("account", updated);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
        }
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1; // Trả về -1 nếu không thể chuyển đổi
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles editing of customer and staff manager accounts by admin only.";
    }
}
