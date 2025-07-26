package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * AccountSearchServlet – Allows Admin to search for accounts by keyword.
 * Only Admin (role = 0) can access this function.
 * 
 * @author CE181518
 */
@WebServlet(name = "AccountSearchServlet", urlPatterns = {"/admin/account/search"})
public class AccountSearchServlet extends HttpServlet {

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

        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        try {
            List<AccountDTO> result = accountDAO.searchAccounts(keyword.trim());
            request.setAttribute("accountList", result);
            request.setAttribute("keyword", keyword.trim());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "🔍 Search failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Search accounts by keyword (Admin only)";
    }
}
