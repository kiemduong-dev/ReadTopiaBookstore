package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý danh sách tài khoản người dùng cho Admin.
 * Hỗ trợ tìm kiếm tài khoản theo username, họ tên, email hoặc số điện thoại.
 */
@WebServlet(name = "AccountListServlet", urlPatterns = {"/admin/account/list"})
public class AccountListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Lấy tham số tìm kiếm từ request (GET hoặc POST)
        String keyword = request.getParameter("keyword");

        AccountDAO dao = new AccountDAO();
        List<AccountDTO> accounts;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm theo từ khóa (đã trim khoảng trắng đầu cuối)
            accounts = dao.searchAccounts(keyword.trim());
            // Đưa keyword lên JSP để giữ lại giá trị ô tìm kiếm
            request.setAttribute("keyword", keyword.trim());
        } else {
            // Không có từ khóa -> lấy toàn bộ danh sách tài khoản
            accounts = dao.getAllAccounts();
        }

        // Đưa danh sách tài khoản lên request để JSP hiển thị
        request.setAttribute("accounts", accounts);

        // Chuyển tiếp đến trang JSP hiển thị danh sách tài khoản
        request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
    }

    // Xử lý cả GET và POST đều gọi processRequest
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
        return "Admin - Quản lý danh sách tài khoản người dùng với chức năng tìm kiếm.";
    }
}
