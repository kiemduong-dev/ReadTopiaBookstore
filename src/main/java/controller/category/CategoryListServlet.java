/**
 * CategoryListServlet - Handles the listing and searching of categories for Admin.
 * This servlet retrieves all categories or filters them based on a keyword.
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
import java.util.List;

/**
 * Servlet mapped to /admin/category/list to display and search category data.
 */
@WebServlet("/admin/category/list")
public class CategoryListServlet extends HttpServlet {

    private final CategoryDAO dao = new CategoryDAO();

    /**
     * Handles GET requests to list all categories or filter them by keyword.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get keyword from request
        String keyword = request.getParameter("keyword");
        List<CategoryDTO> categoryList;

        // Decide to search or list all
        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryList = dao.searchCategories(keyword.trim());
        } else {
            categoryList = dao.getAllCategories();
        }

        // Pass data to JSP
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("keyword", keyword); // retain input

        request.getRequestDispatcher("/WEB-INF/view/admin/category/list.jsp").forward(request, response);
    }
}
