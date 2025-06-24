/**
 * EditBookServlet - Handles both displaying and updating book details by Admin.
 * Supports GET for preloading the book form and POST for submitting edits.
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
 * Servlet mapped to /admin/book/edit to edit existing books.
 */
@WebServlet("/admin/book/edit")
public class EditBookServlet extends HttpServlet {

    /**
     * Handles GET requests to load book data into edit form.
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

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            BookDAO dao = new BookDAO();
            BookDTO book = dao.getBookByID(id);

            if (book != null) {
                request.setAttribute("book", book);
                request.getRequestDispatcher("/WEB-INF/view/admin/book/edit.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/book/list");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
        }
    }

    /**
     * Handles POST requests to update book details after admin submits form.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            BookDTO book = new BookDTO();
            book.setBookID(id);
            book.setBookTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));
            book.setTranslator(request.getParameter("translator"));
            book.setPublisher(request.getParameter("publisher"));
            book.setPublicationYear(Integer.parseInt(request.getParameter("year")));
            book.setIsbn(request.getParameter("isbn"));
            book.setImage(request.getParameter("image"));
            book.setBookDescription(request.getParameter("description"));
            book.setHardcover(Integer.parseInt(request.getParameter("hardcover")));
            book.setDimension(request.getParameter("dimension"));
            book.setWeight(Float.parseFloat(request.getParameter("weight")));
            book.setBookPrice(Double.parseDouble(request.getParameter("price")));
            book.setBookQuantity(Integer.parseInt(request.getParameter("quantity")));
            book.setBookStatus(Integer.parseInt(request.getParameter("status")));

            BookDAO dao = new BookDAO();
            dao.updateBook(book);

            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", " Failed to update book. Please check your input.");
            request.getRequestDispatcher("/WEB-INF/view/admin/book/edit.jsp").forward(request, response);
        }
    }
}
