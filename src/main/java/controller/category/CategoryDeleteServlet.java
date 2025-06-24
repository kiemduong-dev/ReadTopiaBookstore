/**
 * CategoryDeleteServlet - Handles deletion of a category by Admin.
 * This servlet performs a deletion based on category ID and provides
 * session feedback on success or failure.
 *
 * Author: Vuong Chi Bao_CE182018
 */
package controller.category;

import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Servlet mapped to /admin/category/delete for deleting a category by ID.
 */
@WebServlet("/admin/category/delete")
public class CategoryDeleteServlet extends HttpServlet {

    private final CategoryDAO dao = new CategoryDAO();

    /**
     * Handles GET request to delete a category based on ID.
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

            boolean deleted = dao.deleteCategory(id);

            if (deleted) {
                request.getSession().setAttribute("success", " Category deleted successfully.");
            } else {
                request.getSession().setAttribute("error", " Cannot delete this category. It might be used by existing books.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid category ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", " Internal system error during category deletion.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
