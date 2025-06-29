package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dto.BookDTO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * AddBookServlet - Handles the book creation process by the admin. - GET: Loads
 * category list and displays the book creation form. - POST: Processes form
 * submission, inserts book data into the database.
 *
 * @author CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/add")
public class AddBookServlet extends HttpServlet {

    /**
     * Handles the HTTP GET method. Loads the list of available categories and
     * forwards to the add book form view.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();
        request.setAttribute("categoryList", categories);
        request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP POST method. Extracts form data, creates a BookDTO
     * object, inserts it into the database, then redirects to the book list
     * page or shows an error.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Create new book object and set values from form parameters
            BookDTO book = new BookDTO();
            book.setBookTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));
            book.setTranslator(request.getParameter("translator"));
            book.setPublisher(request.getParameter("publisher"));
            book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
            book.setIsbn(request.getParameter("isbn"));
            book.setImage(request.getParameter("image"));
            book.setBookDescription(request.getParameter("description"));
            book.setHardcover(Integer.parseInt(request.getParameter("hardcover")));
            book.setDimension(request.getParameter("dimension"));
            book.setWeight(Float.parseFloat(request.getParameter("weight")));
            book.setBookPrice(Double.parseDouble(request.getParameter("price")));
            book.setBookQuantity(Integer.parseInt(request.getParameter("quantity")));
            book.setBookStatus(1); // Active status

            int categoryID = Integer.parseInt(request.getParameter("categoryID"));

            // Insert book and map it to the selected category
            BookDAO bookDAO = new BookDAO();
            int bookID = bookDAO.insertBook(book);

            if (bookID != -1) {
                bookDAO.insertBookCategory(bookID, categoryID);
                response.sendRedirect(request.getContextPath() + "/admin/book/list");
            } else {
                throw new Exception("Failed to insert book.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Failed to add book. Please check your input.");

            // Reload category list on error
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();
            request.setAttribute("categoryList", categories);

            request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
        }
    }
}
