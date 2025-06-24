package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LoginServlet
 *
 * This servlet handles user login requests. It validates the input, checks the
 * credentials using AccountDAO, and redirects users based on their role.
 *
 * URL mapping: /login
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     * Handles GET requests to show the login page.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
    }

    /**
     * Handles POST requests to process login.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input fields
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Please enter both username and password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        AccountDAO dao = new AccountDAO();
        AccountDTO account = dao.login(username.trim(), password);

        if (account != null) {
            // Login successful, store account in session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);

            // Redirect based on user role
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
            // Login failed
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        }
    }

    /**
     * Provides a short description of the servlet.
     *
     * @return a string containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles user login and role-based redirection.";
    }
}
