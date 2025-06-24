package controller;

import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ProfileServlet
 *
 * This servlet handles requests to display the profile page of the currently
 * authenticated user. If the user is not logged in, the servlet redirects them
 * to the login page.
 *
 * URL mapping: /profile
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes both GET and POST requests to show the user's profile page.
     *
     * Steps: 1. Get the current session without creating a new one. 2. Check if
     * an authenticated account exists in the session. 3. If not authenticated,
     * redirect to login page. 4. If authenticated, attach user data to the
     * request scope. 5. Forward to profile.jsp for rendering.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error is detected
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false); // Do not create if not exist
        AccountDTO account = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("user", account);
        request.getRequestDispatcher("/WEB-INF/view/account/profile.jsp").forward(request, response);
    }

    /**
     * Handles GET requests to load the profile page.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException in case of servlet error
     * @throws IOException in case of I/O error
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST requests. Same behavior as GET to support form resubmission.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException in case of servlet error
     * @throws IOException in case of I/O error
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return String - servlet information summary
     */
    @Override
    public String getServletInfo() {
        return "Displays profile information of the currently authenticated user.";
    }
}
