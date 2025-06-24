/**
 * DeleteBookServlet - Handles book deletion requests by Admin.
 * This servlet deletes a book based on its ID and provides feedback via session attributes.
 *
 * @author Vuong Chi Bao_CE182018
 */
package controller.book;

import dao.BookDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet mapped to /admin/book/delete for deleting books from the system.
 */
@WebServlet("/admin/book/delete")
public class DeleteBookServlet extends HttpServlet {

    private final BookDAO dao = new BookDAO();

    /**
     * Handles GET request to delete a book by ID. Sets a session message on
     * success or failure, and redirects to book list.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookID = Integer.parseInt(request.getParameter("id"));
            boolean success = dao.deleteBookByID(bookID);

            if (success) {
                request.getSession().setAttribute("success", " Book deleted successfully.");
            } else {
                request.getSession().setAttribute("error", " Failed to delete the book.");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", " Invalid book ID.");
        } catch (Exception e) {
            request.getSession().setAttribute("error", " An unexpected error occurred.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/book/list");
    }
}
