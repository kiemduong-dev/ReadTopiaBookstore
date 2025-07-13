package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Book Detail - Handles displaying book details for customers.
 *
 * Retrieves a book by its ID and forwards it to the detail JSP page. Redirects
 * to the homepage if the book is not found or the ID is invalid.
 *
 * URL: /book/detail?id={bookID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/book/detail")
public class BookDetailServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to display book details.
     *
     * Steps: 1. Get "id" from the request parameters. 2. Parse the ID and
     * retrieve the book from the database. 3. If the book exists, set it as a
     * request attribute and forward to JSP. 4. If not found, redirect to the
     * homepage with an error message.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input/output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int bookId = 0;

        try {
            if (idParam != null) {
                bookId = Integer.parseInt(idParam);
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid book ID.");
            response.sendRedirect(request.getContextPath() + "/book/home");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookId);

        if (book != null) {
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/view/book/detail.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Book not found.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        }
    }
}
