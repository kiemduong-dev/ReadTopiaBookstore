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
 * Category Add Servlet - Handles adding new book categories by Admin.
 *
 * Supports: - GET: Loads parent categories and displays the add category form.
 * - POST: Validates and saves the new category.
 *
 * URL: /admin/category/add
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/category/add")
public class CategoryAddServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Handles the HTTP GET request to display the add category form.
     *
     * Loads all parent categories and forwards to the JSP view.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CategoryDTO> parentList = categoryDAO.getAllCategories();
        request.setAttribute("parentList", parentList);

        request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP POST request to save a new category.
     *
     * Steps: 1. Validate form inputs (name, description). 2. Check for
     * duplicate category names. 3. Create CategoryDTO and save it. 4. Redirect
     * to the category list page on success or reload form on failure.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("categoryName") != null
                ? request.getParameter("categoryName").trim() : "";
        String description = request.getParameter("categoryDescription") != null
                ? request.getParameter("categoryDescription").trim() : "";
        String parentIdRaw = request.getParameter("parentID");

        int parentId = 0;
        try {
            if (parentIdRaw != null && !parentIdRaw.isEmpty()) {
                parentId = Integer.parseInt(parentIdRaw);
            }
        } catch (NumberFormatException e) {
            parentId = 0; // Default to no parent category
        }

        // Validate inputs
        if (name.isEmpty() || description.isEmpty()) {
            request.setAttribute("error", "Category name and description cannot be empty.");
            reloadFormWithParents(request, response);
            return;
        }

        // Check for duplicate category name
        if (categoryDAO.isCategoryNameExists(name)) {
            request.setAttribute("error", "Category name already exists. Please choose a different name.");
            reloadFormWithParents(request, response);
            return;
        }

        // Create and save the category
        CategoryDTO category = new CategoryDTO();
        category.setCategoryName(name);
        category.setCategoryDescription(description);
        category.setParentID(parentId);

        categoryDAO.addCategory(category);
        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }

    /**
     * Helper method to reload the add form with parent categories.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    private void reloadFormWithParents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CategoryDTO> parentList = categoryDAO.getAllCategories();
        request.setAttribute("parentList", parentList);
        request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
    }
}
