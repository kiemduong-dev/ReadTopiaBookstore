package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Book List Dashboard - Displays the list of books for Admin.
 *
 * Supports searching books by title or author using a keyword. Retrieves all
 * books if no keyword is provided.
 *
 * URL: /admin/book/list
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/list")
public class BookListDashboardServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to display the book list in the Admin
     * Dashboard.
     *
     * Steps: 1. Retrieve the optional "keyword" parameter from the request. 2.
     * If keyword exists, search books by title or author. 3. If no keyword,
     * fetch all books. 4. Set the book list and keyword as request attributes.
     * 5. Forward the request to the admin book list JSP view.
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
        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        if (keyword != null && !keyword.trim().isEmpty()) {
            books = bookDAO.searchBooksByTitleOrAuthor(keyword.trim());
        } else {
            books = bookDAO.getAllBooks();
        }

        // Set attributes for JSP rendering
        request.setAttribute("bookList", books);
        request.setAttribute("keyword", keyword); // Retain search input

        // Forward to the admin book list JSP page
        request.getRequestDispatcher("/WEB-INF/view/admin/book/list.jsp").forward(request, response);
    }
}
