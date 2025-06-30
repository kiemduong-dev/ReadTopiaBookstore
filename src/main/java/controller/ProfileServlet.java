package controller;

import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ProfileServlet – Displays the profile page of the authenticated user.
 * Redirects to login page if no valid session exists.
 *
 * URL mapping: /profile Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles both GET and POST requests to show user profile.
     *
     * Flow: 1. Get existing session without creating a new one. 2. Check if
     * session contains authenticated user. 3. If not logged in → redirect to
     * login. 4. If logged in → forward to profile.jsp with user data.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if servlet error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false); // Avoid creating new session
        AccountDTO user = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/WEB-INF/view/account/profile.jsp").forward(request, response);
    }

    /**
     * Handles GET request.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST request (same as GET – allows resubmission or refresh).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @return Brief description of servlet.
     */
    @Override
    public String getServletInfo() {
        return "Displays authenticated user's profile page.";
    }
}
