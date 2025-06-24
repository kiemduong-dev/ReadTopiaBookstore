package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * HomePageServlet
 *
 * This servlet handles requests to the public homepage. It simply forwards the
 * request to the home.jsp view.
 *
 * Mapped URL: /home
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "HomePageServlet", urlPatterns = {"/home"})
public class HomePageServlet extends HttpServlet {

    /**
     * Handles GET requests by forwarding to the homepage JSP.
     *
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Future: Book list can be added to request here
        request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
    }

    /**
     * Provides a short description of the servlet.
     *
     * @return String describing servlet purpose
     */
    @Override
    public String getServletInfo() {
        return "Displays the public homepage of the application.";
    }
}
