package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AccountDTO acc = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // 🔐 Nếu chưa đăng nhập → về login
        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        // 👉 Nếu là GET → hiển thị form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        // 👉 Nếu là POST → xử lý đổi mật khẩu
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // ⚠️ Kiểm tra dữ liệu đầu vào
        if (oldPassword == null || newPassword == null || confirmPassword == null
                || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {

            request.setAttribute("error", "❌ All fields are required.");
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "❌ Confirm password does not match.");
            request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
            return;
        }

        // 🔒 Xử lý đổi mật khẩu với xác minh cũ
        AccountDAO dao = new AccountDAO();
        boolean updated = dao.updatePasswordByOld(acc.getUsername(), oldPassword, newPassword);

        if (updated) {
            acc.setPassword("********"); // Cập nhật giả để không hiển thị hash
            session.setAttribute("account", acc);
            request.setAttribute("success", "✅ Password changed successfully.");
        } else {
            request.setAttribute("error", "❌ Old password is incorrect.");
        }

        request.getRequestDispatcher("/WEB-INF/view/account/changePassword.jsp").forward(request, response);
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
        return "Handles user password change functionality.";
    }
}
