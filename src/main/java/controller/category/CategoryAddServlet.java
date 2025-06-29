package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * CategoryAddServlet - Handles adding new book categories by Admin.
 * Displays the add form (GET) and processes the form submission (POST).
 *
 * Author: Vuong Chi Bao_CE182018
 */
@WebServlet("/admin/category/add")
public class CategoryAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve and trim form data
        String name = request.getParameter("categoryName") != null ? request.getParameter("categoryName").trim() : "";
        String description = request.getParameter("categoryDescription") != null ? request.getParameter("categoryDescription").trim() : "";

        // Validate
        if (name.isEmpty() || description.isEmpty()) {
            request.setAttribute("error", "Category name and description must not be empty.");
            request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
            return;
        }

        // Optional: Check if category already exists (by name)
        CategoryDAO dao = new CategoryDAO();
        boolean exists = dao.isCategoryNameExists(name);
        if (exists) {
            request.setAttribute("error", "Category name already exists. Please choose a different name.");
            request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
            return;
        }

        // Build DTO
        CategoryDTO category = new CategoryDTO();
        category.setCategoryName(name);
        category.setCategoryDescription(description);

        // Save
        dao.addCategory(category);

        // Redirect on success
        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
