/**
 * Category Detail Servlet - Displays details of a specific category.
 *
 * Retrieves a category by ID and forwards it to the detail JSP page. Redirects
 * to the category list page if the category is not found.
 *
 * URL: /admin/category/detail?id={categoryID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
package controller.category;

import dao.CategoryDAO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/category/detail")
public class CategoryDetailServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to fetch and display category details by ID.
     *
     * Steps: 1. Retrieve the "id" parameter from the request. 2. Parse the
     * category ID and fetch details using CategoryDAO. 3. Forward to the detail
     * JSP page if the category exists. 4. Otherwise, redirect to the category
     * list page with an error message.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        try {
            int categoryId = Integer.parseInt(idParam);

            CategoryDAO categoryDAO = new CategoryDAO();
            CategoryDTO category = categoryDAO.getCategoryById(categoryId);

            if (category != null) {
                request.setAttribute("category", category);
                request.getRequestDispatcher("/WEB-INF/view/admin/category/detail.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Category not found.");
                response.sendRedirect(request.getContextPath() + "/admin/category/list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid category ID format.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "An error occurred while retrieving category details.");
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
        }
    }
}