package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDetailServlet
 * 
 * Hiển thị chi tiết thông tin tài khoản cho Admin.
 * Chỉ hỗ trợ phương thức GET.
 */
@WebServlet(name = "AccountDetailServlet", urlPatterns = {"/admin/account/detail"})
public class AccountDetailServlet extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO(); // ✅ dùng biến DAO tái sử dụng

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ✅ Lấy username từ param
        String username = request.getParameter("username");

        // ✅ Kiểm tra hợp lệ
        if (username == null || username.trim().isEmpty()) {
            request.getSession().setAttribute("message", "⚠️ Username is missing.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        // ✅ Truy vấn dữ liệu tài khoản từ DAO
        AccountDTO account = accountDAO.getAccountByUsername(username.trim());

        if (account == null) {
            request.getSession().setAttribute("message", "⚠️ Account not found or has been deleted.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        // ✅ Gửi dữ liệu sang JSP
        request.setAttribute("account", account);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/account/list"); // Không hỗ trợ POST
    }

    @Override
    public String getServletInfo() {
        return "Displays detailed information of an account for admin view.";
    }
}
