package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * BookDetailDashboardServlet - Displays detailed information of a book in Admin Dashboard.
 * 
 * Handles GET requests to show book detail view.
 * 
 * URL: /admin/book/detail?id={bookID}
 * 
 * @author Vuong Chi Bao_CE182018
 */
@WebServlet("/admin/book/detail")
public class BookDetailDashboardServlet extends HttpServlet {

    /**
     * Handles GET request to view a book's detail.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int bookID;

        try {
            bookID = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/book/list");
            return;
        }

        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookID);

        if (book == null) {
            request.getSession().setAttribute("error", "❌ Book not found.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");
            return;
        }

        // Set book to request
        request.setAttribute("book", book);

        // Determine return URL (back link)
        String returnUrl = request.getParameter("returnUrl");
        if (returnUrl == null || returnUrl.isEmpty()) {
            returnUrl = request.getContextPath() + "/admin/book/list";
        }
        request.setAttribute("returnUrl", returnUrl);

        // Forward to JSP view
        request.getRequestDispatcher("/WEB-INF/view/admin/book/detail.jsp").forward(request, response);
    }
}
