package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.ValidationUtil;

import java.io.IOException;

/**
 * LoginServlet – Handles user login logic with validation and role-based redirection
 * @author SE
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     * Handles both GET and POST requests for login functionality.
     * Algorithm:
     * 1. If GET → display login form.
     * 2. If POST:
     *    a. Validate input format
     *    b. Authenticate user using DAO
     *    c. On success: store in session & redirect by role
     *    d. On failure: return to login page with error
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Step 1: Handle GET → show login form
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        // Step 2: Handle POST → get input and validate
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!ValidationUtil.isValidUsername(username)) {
            request.setAttribute("error", "Invalid username format.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("error", "Invalid password format.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        // Step 3: Authenticate credentials via DAO
        AccountDAO dao = new AccountDAO();
        AccountDTO account = dao.login(username, password);

        if (account != null) {
            // Step 4: Successful login → create session
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            session.setAttribute("username", account.getUsername());
            session.setAttribute("role", account.getRole());

            // Step 5: Redirect based on role
            int role = account.getRole();
            switch (role) {
                case 0: // Admin
                case 2: // Seller Staff
                case 3: // Warehouse Staff
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                case 1: // Customer
                    response.sendRedirect(request.getContextPath() + "/customer/book/list");
                    break;
                default: // Unknown role
                    session.invalidate();
                    request.setAttribute("error", "Unauthorized role.");
                    request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            }

        } else {
            // Step 6: Login failed
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        }
    }

    /**
     * Handles GET request to show login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request to process login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @return Short description of servlet
     */
    @Override
    public String getServletInfo() {
        return "Handles user login and redirects based on role.";
    }
}
