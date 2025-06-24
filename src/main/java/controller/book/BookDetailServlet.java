/**
 * BookDetailServlet - Handles the display of book details for customers.
 * This servlet retrieves a book by ID and forwards the request to the detail JSP.
 *
 * @author Vuong Chi Bao_CE182018
 */
package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet mapped to /book/detail for displaying book details to customers.
 */
@WebServlet("/book/detail")
public class BookDetailServlet extends HttpServlet {

    /**
     * Handles the GET request to show a book's detail.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int bookID = 0;

        // Validate and parse book ID
        if (idParam != null) {
            try {
                bookID = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // You can log this instead
            }
        }

        // Retrieve book data by ID
        BookDAO dao = new BookDAO();
        BookDTO book = dao.getBookByID(bookID);

        // Forward to detail page or redirect to homepage if not found
        if (book != null) {
            request.setAttribute("book", book);
            request.getRequestDispatcher("/book/detail.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/book/home");
        }
    }
}
