package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LogoutServlet
 *
 * This servlet handles user logout by invalidating the session and redirecting
 * the user to the homepage.
 *
 * URL Mapping: /logout
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles logout request by invalidating the current session (if it exists)
     * and redirecting the user to the homepage.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Do not create new session
        if (session != null) {
            session.invalidate(); // Invalidate the current session
        }

        response.sendRedirect(request.getContextPath() + "/home"); // Redirect to homepage
    }

    /**
     * Handles GET request to log out the user.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request to log out the user.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return A brief description of the servlet's functionality
     */
    @Override
    public String getServletInfo() {
        return "Handles user logout by invalidating the session and redirecting to the homepage.";
    }
}
