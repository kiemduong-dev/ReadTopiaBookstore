/**
 * BookListHomepageServlet - Handles the display of book listings for customers.
 * Supports keyword-based search, category filtering, and sorting.
 *
 * @author
 * Vuong Chi Bao_CE182018
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
 * Servlet mapped to /book/home to show the book list page for end users.
 */
@WebServlet("/book/home")
public class BookListHomepageServlet extends HttpServlet {

    /**
     * Processes GET requests to show books with optional filters: - Search by
     * keyword (title or author) - Filter by category - Sort by title or price
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String catIDStr = request.getParameter("catID");
        String sortBy = request.getParameter("sortBy");

        int catID = 0;
        try {
            if (catIDStr != null && !catIDStr.isEmpty()) {
                catID = Integer.parseInt(catIDStr);
            }
        } catch (NumberFormatException ignored) {
            // Ignore invalid category ID format
        }

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        // Determine which book list to return based on query params
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooksByTitleOrAuthor(keyword.trim());
        } else if (catID > 0) {
            books = bookDAO.getBooksByCategory(catID);
        } else if (sortBy != null && !sortBy.isEmpty()) {
            books = bookDAO.getBooksSortedBy(sortBy);
        } else {
            books = bookDAO.getAllBooks();
        }

        // Retrieve all categories for the filter dropdown
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        // Set attributes for JSP rendering
        request.setAttribute("bookList", books);
        request.setAttribute("categoryList", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCatID", catID);
        request.setAttribute("sortBy", sortBy);

        // Forward to homepage view
        request.getRequestDispatcher("/book/homepage.jsp").forward(request, response);
    }
}
