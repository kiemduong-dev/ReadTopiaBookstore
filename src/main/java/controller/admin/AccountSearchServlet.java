package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "AccountSearchServlet", urlPatterns = {"/admin/account/search"})
public class AccountSearchServlet extends HttpServlet {

    private final AccountDAO dao = new AccountDAO();

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
            List<AccountDTO> accounts = dao.searchAccounts(keyword.trim());
            request.setAttribute("accountList", accounts);
            request.setAttribute("keyword", keyword.trim());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Search failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
        }
    }
}
