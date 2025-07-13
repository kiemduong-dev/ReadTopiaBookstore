package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Category Edit Servlet - Handles editing of categories for Admin.
 *
 * Supports: - GET: Loads category data into the edit form. - POST: Updates
 * category details in the database.
 *
 * URL: /admin/category/edit?id={categoryID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/category/edit")
public class CategoryEditServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Handles the HTTP GET request to load category data into the edit form.
     *
     * Steps: 1. Retrieve the "id" parameter from the request. 2. Fetch category
     * details and parent categories from the database. 3. Exclude the current
     * category from the parent list. 4. Forward to the edit JSP view or
     * redirect on error.
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
            CategoryDTO category = categoryDAO.getCategoryById(categoryId);

            if (category != null) {
                List<CategoryDTO> parentList = categoryDAO.getAllCategoriesExcluding(categoryId);

                request.setAttribute("category", category);
                request.setAttribute("parentList", parentList);

                request.getRequestDispatcher("/WEB-INF/view/admin/category/edit.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Category not found.");
                response.sendRedirect(request.getContextPath() + "/admin/category/list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid category ID format.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Error loading category details.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }

    /**
     * Handles the HTTP POST request to update category details.
     *
     * Steps: 1. Retrieve and validate form parameters. 2. Prevent selecting the
     * category as its own parent. 3. Update the category in the database. 4.
     * Redirect with success or error message.
     *
     * @param request the HttpServletRequest object containing form data
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("categoryID"));
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            String parentIdRaw = request.getParameter("parentID");

            int parentId = 0;
            if (parentIdRaw != null && !parentIdRaw.isEmpty()) {
                parentId = Integer.parseInt(parentIdRaw);
            }

            if (categoryId == parentId) {
                request.getSession().setAttribute("error", "A category cannot be its own parent.");
                response.sendRedirect(request.getContextPath() + "/admin/category/edit?id=" + categoryId);
                return;
            }

            CategoryDTO category = new CategoryDTO();
            category.setCategoryID(categoryId);
            category.setCategoryName(categoryName);
            category.setCategoryDescription(description);
            category.setParentID(parentId);

            categoryDAO.updateCategory(category);

            request.getSession().setAttribute("success", "Category updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");

        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to update category. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}
