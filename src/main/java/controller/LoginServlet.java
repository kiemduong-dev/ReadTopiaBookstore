package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng đến trang login.jsp
        request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra dữ liệu đầu vào có null không
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "⚠️ Please enter both username and password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        // Gọi DAO để xử lý đăng nhập
        AccountDAO dao = new AccountDAO();
        AccountDTO account = dao.login(username, password); // ✅ Bên trong đã dùng BCrypt.checkpw

        if (account != null) {
            // ✅ Đăng nhập thành công
            HttpSession session = request.getSession();
            session.setAttribute("account", account);

            // 👉 Điều hướng theo vai trò
            switch (account.getRole()) {
                case 0: // Admin
                case 2: // Seller Staff
                case 3: // Warehouse Staff
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                default: // Customer
                    response.sendRedirect(request.getContextPath() + "/home");
                    break;
            }
        } else {
            // ❌ Đăng nhập thất bại
            request.setAttribute("error", "❌ Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user login and role-based redirection.";
    }
}
