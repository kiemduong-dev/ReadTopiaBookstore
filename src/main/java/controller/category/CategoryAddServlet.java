/**
 * CategoryAddServlet - Handles adding new book categories by Admin.
 * This servlet displays the add form and processes the form submission.
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
 * Servlet mapped to /admin/category/add for displaying and processing the form
 * to add a new category.
 */
@WebServlet("/admin/category/add")
public class CategoryAddServlet extends HttpServlet {

    /**
     * Handles GET request to show the add category form.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
    }

    /**
     * Handles POST request to process the category addition.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve form data
        String name = request.getParameter("categoryName");
        String description = request.getParameter("description");

        // Build category object
        CategoryDTO category = new CategoryDTO();
        category.setCategoryName(name);
        category.setCategoryDescription(description);

        // Save to database
        CategoryDAO dao = new CategoryDAO();
        dao.addCategory(category);

        // Redirect to category list
        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
