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
 * Book Detail Dashboard - Displays detailed information of a book for Admin.
 *
 * Retrieves book data by ID and forwards it to the detail JSP page. Redirects
 * to the book list page if the book is not found.
 *
 * URL: /admin/book/detail?id={bookID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/detail")
public class BookDetailDashboardServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to display book details in the Admin
     * Dashboard.
     *
     * Steps: 1. Get "id" from request parameters. 2. Parse the ID and fetch the
     * book from the database. 3. If found, set it as a request attribute and
     * forward to the JSP view. 4. If not found, redirect to the book list page
     * with an error message.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int bookId;

        try {
            bookId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid book ID format.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookId);

        if (book == null) {
            request.getSession().setAttribute("error", "Book not found.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");
            return;
        }

        // Set book data for the JSP view
        request.setAttribute("book", book);

        // Determine return URL (for the back button)
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl == null || returnUrl.isEmpty()) {
            returnUrl = request.getContextPath() + "/admin/book/list";
        }
        request.setAttribute("returnUrl", returnUrl);

        // Forward to the detail JSP page
        request.getRequestDispatcher("/WEB-INF/view/admin/book/detail.jsp").forward(request, response);
    }
}
