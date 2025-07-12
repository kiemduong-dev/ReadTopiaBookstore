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
 * Displays detailed information of a specific account for administrators. Only
 * supports HTTP GET method.
 *
 * URL mapping: /admin/account/detail
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountDetailServlet", urlPatterns = {"/admin/account/detail"})
public class AccountDetailServlet extends HttpServlet {

    private final AccountDAO accountDAO = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDTO currentUser = (AccountDTO) session.getAttribute("account");

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
        } else {
            request.setAttribute("account", account);
            request.getRequestDispatcher("/WEB-INF/view/admin/account/detail.jsp").forward(request, response);
        }
    }
}
