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
 * Displays detailed information of a specific account for administrators.
 * Only supports HTTP GET method.
 * 
 * URL mapping: /admin/account/detail
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountDetailServlet", urlPatterns = {"/admin/account/detail"})
public class AccountDetailServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Handles HTTP GET request to retrieve and display account detail.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            request.getSession().setAttribute("message", "Username is missing.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        AccountDTO account = accountDAO.getAccountByUsername(username.trim());

        if (account == null) {
            request.getSession().setAttribute("message", "Account not found or has been deleted.");
            response.sendRedirect(request.getContextPath() + "/admin/account/list");
            return;
        }

        request.setAttribute("account", account);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/detail.jsp").forward(request, response);
    }

    /**
     * Handles HTTP POST request (not supported in this servlet).
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/account/list");
    }

    /**
     * Returns servlet description.
     *
     * @return a string description
     */
    @Override
    public String getServletInfo() {
        return "Displays detailed information of an account for admin view.";
    }
}
