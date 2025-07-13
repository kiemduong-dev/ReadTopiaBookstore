package controller.book;

import dao.BookDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Delete Book Servlet - Handles deletion of books by Admin.
 *
 * Deletes a book from the system based on its ID and sets a session message to
 * indicate success or failure.
 *
 * URL: /admin/book/delete?id={bookID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/delete")
public class DeleteBookServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

    /**
     * Handles the HTTP GET request to delete a book by ID.
     *
     * Steps: 1. Get the "id" parameter from the request. 2. Parse the ID and
     * attempt to delete the book using BookDAO. 3. Set a session message
     * indicating success or failure. 4. Redirect back to the admin book list
     * page.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = Integer.parseInt(request.getParameter("id"));
            boolean isDeleted = bookDAO.deleteBookByID(bookId);

            if (isDeleted) {
                request.getSession().setAttribute("success", "Book deleted successfully.");
            } else {
                request.getSession().setAttribute("error", "Failed to delete the book.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid book ID.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "An unexpected error occurred while deleting the book.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/book/list");
    }
}
