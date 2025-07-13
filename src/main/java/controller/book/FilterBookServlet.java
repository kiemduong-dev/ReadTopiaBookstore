package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dto.BookDTO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Filter Book Servlet - Handles filtering books by category for end users.
 *
 * Retrieves books from the selected category and displays them on the homepage.
 *
 * URL: /book/filter?catID={categoryID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/book/filter")
public class FilterBookServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to filter books by category.
     *
     * Steps: 1. Retrieve the "catID" parameter from the request. 2. Parse the
     * category ID and fetch books using BookDAO. 3. Load all categories for the
     * filter dropdown. 4. Set data as request attributes and forward to the
     * homepage view. 5. Handle errors with appropriate session messages.
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
            int categoryId = Integer.parseInt(request.getParameter("catID"));

            BookDAO bookDAO = new BookDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            List<BookDTO> books = bookDAO.getBooksByCategory(categoryId);
            List<CategoryDTO> categories = categoryDAO.getAllCategories();

            request.setAttribute("bookList", books);
            request.setAttribute("categoryList", categories);
            request.setAttribute("selectedCatID", categoryId); // Highlight selected filter

            request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid category ID.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "An error occurred while filtering books.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        }
    }
}
