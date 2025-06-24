/**
 * FilterBookServlet - Handles filtering books by category for end users.
 * Retrieves books of a selected category and displays them on the homepage.
 *
 * Author: Vuong Chi Bao_CE182018
 */
package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dto.BookDTO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet mapped to /book/filter to show books filtered by category.
 */
@WebServlet("/book/filter")
public class FilterBookServlet extends HttpServlet {

    /**
     * Handles GET requests to filter books by category.
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
            int categoryId = Integer.parseInt(request.getParameter("catID"));

            BookDAO bookDAO = new BookDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            List<BookDTO> books = bookDAO.getBooksByCategory(categoryId);
            List<CategoryDTO> categories = categoryDAO.getAllCategories();

            request.setAttribute("bookList", books);
            request.setAttribute("categoryList", categories);
            request.setAttribute("selectedCatID", categoryId); // used to mark the selected filter

            request.getRequestDispatcher("/book/homepage.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Invalid category ID passed
            request.getSession().setAttribute("error", "Invalid category ID.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        } catch (Exception e) {
            e.printStackTrace(); // log the full stack trace
            request.getSession().setAttribute("error", "An error occurred while filtering.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        }
    }
}
