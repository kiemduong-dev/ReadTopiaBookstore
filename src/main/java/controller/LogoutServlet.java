package controller;

import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * LogoutServlet – Handles user logout by invalidating the session and
 * redirecting to homepage. URL Mapping: /logout
 *
 * @author CE181518
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Common logout process (both GET & POST): 1. Invalidate session if exists
     * 2. Redirect to homepage
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Get existing session (do not create new)
        HttpSession session = request.getSession(false);
        if (session != null) {
            AccountDTO user = (AccountDTO) session.getAttribute("account");

            // Optional: Logging
            if (user != null) {
                System.out.println("User logged out: " + user.getUsername());
            }

            session.invalidate(); // Step 2: Invalidate session
        }

        // Step 3: Redirect to home page
        response.sendRedirect(request.getContextPath() + "/homepage/book/list");
    }

    /**
     * Handle GET logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handle POST logout
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @return Servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles user logout by clearing session and redirecting to homepage.";
    }
}
