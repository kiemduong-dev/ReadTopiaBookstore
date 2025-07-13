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
 * Category List Servlet - Handles listing and searching of categories for
 * Admin.
 *
 * Retrieves all categories or filters them by a keyword.
 *
 * URL: /admin/category/list
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/category/list")
public class CategoryListServlet extends HttpServlet {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Handles the HTTP GET request to list all categories or filter by keyword.
     *
     * Steps: 1. Retrieve the "keyword" parameter from the request (if
     * provided). 2. Fetch categories from the database using CategoryDAO. - If
     * keyword exists, search by name or description. - Otherwise, load all
     * categories. 3. Set the result and keyword as request attributes. 4.
     * Forward to the category list JSP page for rendering.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        List<CategoryDTO> categoryList;

        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryList = categoryDAO.searchCategories(keyword.trim());
        } else {
            categoryList = categoryDAO.getAllCategories();
        }

        request.setAttribute("categoryList", categoryList);
        request.setAttribute("keyword", keyword); // Retain search input

        request.getRequestDispatcher("/WEB-INF/view/admin/category/list.jsp").forward(request, response);
    }
}
