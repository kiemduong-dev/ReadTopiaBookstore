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
 * Sort Book Servlet - Handles sorting the book list for customers.
 *
 * Retrieves books sorted by the selected criteria (e.g., price or title) and
 * forwards them to the homepage view.
 *
 * URL: /book/sort?sortBy={criteria}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/book/sort")
public class SortBookServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to sort books by selected criteria.
     *
     * Steps: 1. Get "sortBy" parameter from the request (e.g., price_asc,
     * title_desc). 2. Fetch sorted books using BookDAO. 3. Set data as request
     * attributes and forward to homepage view.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sortBy = request.getParameter("sortBy"); // Example: price_asc, title_desc

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> sortedBooks = bookDAO.getBooksSortedBy(sortBy);

        // Set attributes for JSP rendering
        request.setAttribute("bookList", sortedBooks);
        request.setAttribute("sortBy", sortBy); // Retain selected sort option in UI

        // Forward to homepage JSP
        request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);
    }
}
