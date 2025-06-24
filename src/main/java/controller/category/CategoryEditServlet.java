/**
 * CategoryEditServlet - Handles category editing for Admin.
 * This servlet provides the form to edit a category and processes the update.
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
 * Servlet mapped to /admin/category/edit for updating category information.
 */
@WebServlet("/admin/category/edit")
public class CategoryEditServlet extends HttpServlet {

    /**
     * Handles GET request to show the edit form for a specific category.
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
                request.getRequestDispatcher("/WEB-INF/view/admin/category/edit.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "❌ Category not found.");
                response.sendRedirect(request.getContextPath() + "/admin/category/list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "❌ Invalid category ID.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "❌ Error loading category.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }

    /**
     * Handles POST request to update category information.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");

            CategoryDTO category = new CategoryDTO(categoryID, categoryName, description);
            CategoryDAO dao = new CategoryDAO();
            dao.updateCategory(category);

            request.getSession().setAttribute("success", " Category updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", " Failed to update category.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}
