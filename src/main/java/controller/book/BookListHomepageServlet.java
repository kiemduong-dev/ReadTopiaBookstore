package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dao.NotificationDAO;
import dto.BookDTO;
import dto.CategoryDTO;
import dto.NotificationDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBContext;

/**
 * Book List Customer - Displays the list of books for customers.
 *
 * Retrieves books from the database with optional filtering, sorting, and
 * category selection. Also loads user notifications if the customer is logged
 * in.
 *
 * URL: /customer/book/list
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/homepage/book/list")
public class BookListHomepageServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests to display the book list for customers.
     *
     * Steps: 1. Check for logged-in user and load notifications (if any). 2.
     * Retrieve optional filters: keyword, sort option, category ID. 3. Fetch
     * the list of books based on filters. 4. Load all categories for the filter
     * dropdown. 5. Set data as request attributes and forward to the homepage
     * JSP.
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
        String sortOption = request.getParameter("sort");
        String categoryIdParam = request.getParameter("catID");

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        HttpSession session = request.getSession();

        // Determine books to display based on filters
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam);
                books = bookDAO.getBooksByCategory(categoryId);
            } catch (NumberFormatException e) {
                Logger.getLogger(BookListHomepageServlet.class.getName())
                        .log(Level.WARNING, "Invalid category ID format", e);
                books = bookDAO.getAllBooks();
            }
        } else if (keyword != null && !keyword.isEmpty()) {
            books = bookDAO.searchBooksByTitleOrAuthor(keyword);
        } else if (sortOption != null && !sortOption.isEmpty()) {
            books = bookDAO.getBooksSortedBy(sortOption);
        } else {
            books = bookDAO.getAllBooks();
        }

        // Load all categories for filtering
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        // Set attributes for JSP rendering
        request.setAttribute("bookList", books);
        request.setAttribute("categoryList", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sort", sortOption);
        request.setAttribute("catID", categoryIdParam);

        // Forward to homepage view
        request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);
    }
}
