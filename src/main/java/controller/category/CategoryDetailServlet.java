/**
 * CategoryDetailServlet - Displays details of a specific category.
 * This servlet retrieves a category by ID and forwards to the detail view.
 *
 * Author: Vuong Chi Bao_CE182018
 */
package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet mapped to /admin/category/detail for viewing category details.
 */
@WebServlet("/admin/category/detail")
public class CategoryDetailServlet extends HttpServlet {

    /**
     * Handles GET request to fetch and show category details by ID.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            CategoryDAO dao = new CategoryDAO();
            CategoryDTO category = dao.getCategoryById(id);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/WEB-INF/view/admin/category/detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/category/list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", " Invalid category ID.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", " Error retrieving category detail.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}
