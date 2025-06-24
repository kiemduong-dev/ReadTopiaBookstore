package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LogoutServlet
 *
 * This servlet is responsible for logging out a user by invalidating their session.
 * It handles both GET and POST methods and redirects the user to the homepage.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    /**
     * This method handles both GET and POST logout requests.
     *
     * Algorithm:
     * 1. Retrieve the current session without creating a new one.
     * 2. If a session exists, invalidate it to log out the user.
     * 3. Redirect the user to the homepage after logout.
     *
     * @param request  HttpServletRequest object from client
     * @param response HttpServletResponse object to client
     * @throws ServletException if a servlet error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Get the current session if it exists; do not create a new session
        HttpSession session = request.getSession(false);

        // Step 2: If a session exists, invalidate it to log out the user
        if (session != null) {
            session.invalidate();
        }

        // Step 3: Redirect to homepage after logout
        response.sendRedirect(request.getContextPath() + "/home");
    }

    /**
     * Handles GET request to logout the user.
     * Delegates to processRequest().
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException in case of servlet error
     * @throws IOException      in case of I/O error
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request to logout the user.
     * Delegates to processRequest().
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException in case of servlet error
     * @throws IOException      in case of I/O error
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Provides a short description of this servlet's purpose.
     *
     * @return description of LogoutServlet
     */
    @Override
    public String getServletInfo() {
        return "Handles user logout by invalidating session and redirecting to homepage.";
    }
}
