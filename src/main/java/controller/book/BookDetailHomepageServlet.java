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
 * Book Detail Customer - Displays details of a book for customers.
 *
 * Retrieves a book by its ID and forwards the data to the book detail JSP page.
 * Redirects to the book list page if the book is not found or inactive.
 *
 * URL: /customer/book/detail?id={bookID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/homepage/book/detail")
public class BookDetailHomepageServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to display book details for customers.
     *
     * Steps: 1. Get the "id" parameter from the request. 2. Parse the book ID
     * and fetch details using BookDAO. 3. If the book exists and is active,
     * forward to the detail JSP. 4. Otherwise, redirect to the book list page.
     *
     * @param request the HttpServletRequest object containing client request
     * data
     * @param response the HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
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
            request.getSession().setAttribute("error", "Invalid book ID format.");
            response.sendRedirect(request.getContextPath() + "/book/home");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookId);

        if (book != null && book.getBookStatus() == 1) {
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/view/book/detail.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Book not found or inactive.");
            response.sendRedirect(request.getContextPath() + "/book/home");
        }
    }
}
