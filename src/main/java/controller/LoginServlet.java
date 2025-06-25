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
 * This servlet handles user login logic. - Supports GET requests to display the
 * login form. - Supports POST requests to authenticate credentials. - Redirects
 * users based on role after successful login.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     * Handles both GET and POST requests. Algorithm: 1. If GET → display login
     * form. 2. If POST → validate credentials using DAO. a. On success → store
     * user in session and redirect based on role. b. On failure → return to
     * login page with error message.
     *
     * @param request HttpServletRequest from client
     * @param response HttpServletResponse to client
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Set content type and encoding
        response.setContentType("text/html;charset=UTF-8");

        // Step 2: Check if the request is GET
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            // Show login form
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
            return;
        }

        // Step 3: Process login from form (POST)
        String username = request.getParameter("username"); // Get username input
        String password = request.getParameter("password"); // Get password input

        AccountDAO dao = new AccountDAO(); // Create DAO instance
        AccountDTO account = dao.login(username, password); // Authenticate user

        // Step 4: If login successful
        if (account != null) {
            // Create or retrieve session
            HttpSession session = request.getSession();
            session.setAttribute("account", account); // Store account in session
            session.setAttribute("username", account.getUsername());
            session.setAttribute("role", account.getRole());

            // Get user role
            int role = account.getRole();

            // Step 5: Redirect user based on their role
            switch (role) {
                case 0: // Admin
                case 2: // Seller Staff
                case 3: // Warehouse Staff
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    break;
                default:
                case 1: // Customer
                    response.sendRedirect(request.getContextPath() + "/customer/book/list");
                    break;

                // Unknown or guest
            }

        } else {
            // Step 6: Login failed
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/view/account/login.jsp").forward(request, response);
        }
    }

    /**
     * Handles GET request to render the login form.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request to process login form.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns description of the servlet.
     *
     * @return Short description string
     */
    @Override
    public String getServletInfo() {
        return "Handles user login and redirects to dashboard or homepage based on role.";
    }
}
