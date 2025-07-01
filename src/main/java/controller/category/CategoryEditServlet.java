package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * CategoryEditServlet - Handles category editing for Admin.
 * This servlet provides the form to edit a category and processes the update.
 *
 * Author: Vuong Chi Bao_CE182018
 */
@WebServlet("/admin/category/edit")
public class CategoryEditServlet extends HttpServlet {

    private final CategoryDAO dao = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CategoryDTO category = dao.getCategoryById(id);

            if (category != null) {
                // Lấy danh sách danh mục cha, loại bỏ chính nó khỏi danh sách
                List<CategoryDTO> parentList = dao.getAllCategoriesExcluding(id);

                request.setAttribute("category", category);
                request.setAttribute("parentList", parentList);

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            String parentIDRaw = request.getParameter("parentID");

            int parentID = 0;
            if (parentIDRaw != null && !parentIDRaw.isEmpty()) {
                parentID = Integer.parseInt(parentIDRaw);
            }

            // Không được chọn chính nó làm cha
            if (categoryID == parentID) {
                request.getSession().setAttribute("error", "❌ A category cannot be its own parent.");
                response.sendRedirect(request.getContextPath() + "/admin/category/edit?id=" + categoryID);
                return;
            }

            // Build DTO
            CategoryDTO category = new CategoryDTO();
            category.setCategoryID(categoryID);
            category.setCategoryName(categoryName);
            category.setCategoryDescription(description);
            category.setParentID(parentID);

            dao.updateCategory(category);

            request.getSession().setAttribute("success", "✅ Category updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "❌ Failed to update category.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}
