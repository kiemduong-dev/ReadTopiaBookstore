package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * AccountListServlet – Displays all accounts for admin.
 * Only Admin (role = 0) is allowed to access.
 *
 * URL: /admin/account/list
 * 
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountListServlet", urlPatterns = {"/admin/account/list"})
public class AccountListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO dao = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN); // Admin only
            return;
        }

        try {
            List<AccountDTO> accounts = dao.getAllAccounts();
            request.setAttribute("accountList", accounts);

            // Message (if redirected from create/update/delete)
            String message = (String) session.getAttribute("message");
            if (message != null) {
                request.setAttribute("message", message);
                session.removeAttribute("message");
            }

            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to load account list.");
            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
        }
    }
}
