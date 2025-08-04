package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * AccountDetailServlet – Displays detailed information of a specific account.
 * Only accessible by Admin (role = 0).
 *
 * URL: /admin/account/detail?username={username}
 *
 * @author CE181518
 */
@WebServlet(name = "AccountDetailServlet", urlPatterns = {"/admin/account/detail"})
public class AccountDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        AccountDTO account = accountDAO.getAccountByUsername(username.trim());

        if (account == null) {
            session.setAttribute("message", "Account not found.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        // ✅ Admin chỉ được xem thông tin role 0, 2, 3 → không được edit
        // ✅ Được xem đầy đủ với role 1 và 4 (Staff & Customer)
        request.setAttribute("account", account);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/detail.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays account details (Admin only)";
    }
}
