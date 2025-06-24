package controller;

import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * ProfileServlet
 *
 * This servlet handles displaying the profile page for the currently
 * authenticated user. If the session does not contain a valid user account,
 * it redirects to the login page.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    /**
     * Processes both GET and POST requests to show the user's profile page.
     * 
     * Algorithm:
     * 1. Retrieve the current session (without creating a new one).
     * 2. Check if a valid AccountDTO object exists in session.
     * 3. If not logged in, redirect the user to login page.
     * 4. Otherwise, attach user data to the request.
     * 5. Forward the request to profile.jsp for rendering.
     *
     * @param request  The HttpServletRequest object
     * @param response The HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an input or output error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set the response content type to HTML with UTF-8 encoding
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve the current session without creating a new one
        HttpSession session = request.getSession(false);

        // Attempt to get the logged-in account from session (may be null)
        AccountDTO acc = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // If account is not found, user is not authenticated → redirect to login
        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        // Attach user information to the request for rendering on JSP
        request.setAttribute("user", acc);

        // Forward the request to profile.jsp located in the secured view folder
        request.getRequestDispatcher("/WEB-INF/view/account/profile.jsp").forward(request, response);
    }

    /**
     * Handles GET requests to load the profile page.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException in case of servlet failure
     * @throws IOException      in case of I/O error
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles POST requests. In this case, it behaves identically to GET.
     * Allows form submissions or refreshes that post back to profile.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException in case of servlet failure
     * @throws IOException      in case of I/O error
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Provides a short description of this servlet.
     *
     * @return a brief message explaining the servlet’s purpose
     */
    @Override
    public String getServletInfo() {
        return "Displays profile information of the currently logged-in user.";
    }
}
