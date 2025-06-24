/**
 * SortBookServlet - Handles sorting book list by criteria (price or title).
 * This servlet retrieves the sorted book list and forwards it to homepage.jsp.
 *
 * Author: Vuong Chi Bao_CE182018
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
 * Servlet mapped to /book/sort to sort and display books for customers.
 */
@WebServlet("/book/sort")
public class SortBookServlet extends HttpServlet {

    /**
     * Handles GET requests to sort books by selected criteria.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sortBy = request.getParameter("sortBy"); // e.g., "price_asc", "title_desc"

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> sortedBooks = bookDAO.getBooksSortedBy(sortBy);

        request.setAttribute("bookList", sortedBooks);
        request.setAttribute("sortBy", sortBy); // used to retain selected option in UI

        request.getRequestDispatcher("/book/homepage.jsp").forward(request, response);
    }
}
