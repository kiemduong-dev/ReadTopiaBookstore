package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * CategoryAddServlet - Handles adding new book categories by Admin.
 * Displays the add form (GET) and processes the form submission (POST).
 *
 * Author: Vuong Chi Bao_CE182018
 */
@WebServlet("/admin/category/add")
public class CategoryAddServlet extends HttpServlet {

    private final CategoryDAO dao = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách danh mục cha (có thể làm danh mục mẹ)
        List<CategoryDTO> parentList = dao.getAllCategories();
        request.setAttribute("parentList", parentList);

        request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("categoryName") != null ? request.getParameter("categoryName").trim() : "";
        String description = request.getParameter("categoryDescription") != null ? request.getParameter("categoryDescription").trim() : "";
        String parentIDRaw = request.getParameter("parentID");

        int parentID = 0;
        try {
            if (parentIDRaw != null && !parentIDRaw.isEmpty()) {
                parentID = Integer.parseInt(parentIDRaw);
            }
        } catch (NumberFormatException e) {
            parentID = 0;
        }

        // Validate
        if (name.isEmpty() || description.isEmpty()) {
            request.setAttribute("error", "Category name and description must not be empty.");
            request.setAttribute("parentList", dao.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
            return;
        }

        // Kiểm tra tên trùng
        if (dao.isCategoryNameExists(name)) {
            request.setAttribute("error", "Category name already exists. Please choose a different name.");
            request.setAttribute("parentList", dao.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/view/admin/category/add.jsp").forward(request, response);
            return;
        }

        // Build DTO
        CategoryDTO category = new CategoryDTO();
        category.setCategoryName(name);
        category.setCategoryDescription(description);
        category.setParentID(parentID);  // Set danh mục cha

        // Save
        dao.addCategory(category);

        // Redirect
        response.sendRedirect(request.getContextPath() + "/admin/category/list");
    }
}
