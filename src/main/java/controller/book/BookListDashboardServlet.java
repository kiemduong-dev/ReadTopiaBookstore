/**
 * BookListDashboardServlet - Handles displaying the list of books for admin dashboard.
 * Supports keyword-based searching by book title or author.
 *
 * @author
 * Vuong Chi Bao_CE182018
 */
package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet mapped to /admin/book/list to render the admin book list page.
 */
@WebServlet("/admin/book/list")
public class BookListDashboardServlet extends HttpServlet {

    /**
     * Processes GET requests for viewing the list of books in the admin
     * dashboard.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve search keyword from request parameter
        String keyword = request.getParameter("keyword");

        BookDAO dao = new BookDAO();
        List<BookDTO> books;

        // If keyword exists, perform search; otherwise, fetch all books
        if (keyword != null && !keyword.trim().isEmpty()) {
            books = dao.searchBooksByTitleOrAuthor(keyword.trim());
        } else {
            books = dao.getAllBooks();
        }

        // Set attributes for JSP rendering
        request.setAttribute("bookList", books);
        request.setAttribute("keyword", keyword); // Keep keyword in input box

        // Forward to the list view
        request.getRequestDispatcher("/WEB-INF/view/admin/book/list.jsp").forward(request, response);
    }
}
