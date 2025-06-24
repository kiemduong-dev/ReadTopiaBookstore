package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * AccountListServlet
 *
 * Handles the display and search of user accounts for administrators.
 * Supports keyword-based search by username, full name, email, or phone.
 * 
 * URL mapping: /admin/account/list
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountListServlet", urlPatterns = {"/admin/account/list"})
public class AccountListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles both GET and POST requests to display the account list.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String keyword = request.getParameter("keyword");

        AccountDAO dao = new AccountDAO();
        List<AccountDTO> accounts;

        if (keyword != null && !keyword.trim().isEmpty()) {
            accounts = dao.searchAccounts(keyword.trim());
            request.setAttribute("keyword", keyword.trim());
        } else {
            accounts = dao.getAllAccounts();
        }

        request.setAttribute("accounts", accounts);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/list.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP GET method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns servlet description.
     *
     * @return servlet information string
     */
    @Override
    public String getServletInfo() {
        return "Admin - Handles listing and searching of user accounts.";
    }
}
