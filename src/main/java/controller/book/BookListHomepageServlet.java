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
 * Book List Homepage - Displays a list of books for customers.
 *
 * Supports: - Search by keyword (title or author) - Filter by category - Sort
 * by title or price
 *
 * URL: /book/home
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/book/home")
public class BookListHomepageServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to display books with optional filters.
     *
     * Steps: 1. Retrieve optional query parameters: keyword, category ID, sort
     * option. 2. Fetch books based on the provided filters. 3. Retrieve all
     * categories for the filter dropdown. 4. Set attributes for rendering in
     * the JSP view. 5. Forward to the homepage JSP.
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
        String categoryIdParam = request.getParameter("catID");
        String sortOption = request.getParameter("sortBy");

        int categoryId = 0;
        try {
            if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
                categoryId = Integer.parseInt(categoryIdParam);
            }
        } catch (NumberFormatException e) {
            // Invalid category ID, ignore and continue without filter
        }

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        // Determine which books to fetch based on filters
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooksByTitleOrAuthor(keyword.trim());
        } else if (categoryId > 0) {
            books = bookDAO.getBooksByCategory(categoryId);
        } else if (sortOption != null && !sortOption.isEmpty()) {
            books = bookDAO.getBooksSortedBy(sortOption);
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
        request.setAttribute("selectedCatID", categoryId);
        request.setAttribute("sortBy", sortOption);

        // Forward to the homepage JSP view
        request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);
    }
}
