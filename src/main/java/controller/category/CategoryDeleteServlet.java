package controller.category;

import dao.CategoryDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Category Delete Servlet - Handles the deletion of a category by Admin.
 *
 * Deletes a category based on its ID and provides session feedback on success
 * or failure.
 *
 * URL: /admin/category/delete?id={categoryID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/category/delete")
public class CategoryDeleteServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Handles the HTTP GET request to delete a category.
     *
     * Steps: 1. Retrieve the "id" parameter from the request. 2. Parse the
     * category ID and attempt deletion using CategoryDAO. 3. Set a success or
     * error message in the session. 4. Redirect to the category list page.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));

            boolean isDeleted = categoryDAO.deleteCategory(categoryId);

            if (isDeleted) {
                request.getSession().setAttribute("success", "Category deleted successfully.");
            } else {
                request.getSession().setAttribute("error", "Cannot delete this category. It may be linked to existing books.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid category ID format.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "An unexpected error occurred while deleting the category.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
