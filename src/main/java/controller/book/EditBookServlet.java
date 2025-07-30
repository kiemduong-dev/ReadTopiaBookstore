package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dto.BookDTO;
import dto.CategoryDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Edit Book Servlet - Handles displaying and updating book details for Admin.
 *
 * Supports: - GET: Loads existing book data into the edit form. - POST:
 * Processes the submitted form and updates the book in the database.
 *
 * URL: /admin/book/edit?id={bookID}
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/edit")
public class EditBookServlet extends HttpServlet {

    /**
     * Handles the HTTP GET request to load book data into the edit form.
     *
     * Steps: 
     * 1. Retrieve "id" from request parameters. 
     * 2. Fetch the book by ID using BookDAO.
     * 3. Forward to the edit JSP view if found.
     * 4. Redirect to the book list page if not found or invalid ID.
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

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID.");
            return;
        }

        try {
            int bookId = Integer.parseInt(idParam);
            BookDAO bookDAO = new BookDAO();
            BookDTO book = bookDAO.getBookByID(bookId);

            if (book != null) {
                // Load the list of categories, authors, translators, and publishers
                CategoryDAO categoryDAO = new CategoryDAO();
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("categoryList", categories);

                BookDAO bookDAOList = new BookDAO();
                List<String> authors = bookDAOList.getAllAuthors();
                List<String> translators = bookDAOList.getAllTranslators();
                List<String> publishers = bookDAOList.getAllPublishers();
                
                // Set the attributes to the request to send to JSP
                request.setAttribute("book", book);
                request.setAttribute("authors", authors);
                request.setAttribute("translators", translators);
                request.setAttribute("publishers", publishers);
                
                // Forward to the edit page
                request.getRequestDispatcher("/WEB-INF/view/admin/book/edit.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("error", "Book not found.");
                response.sendRedirect(request.getContextPath() + "/admin/book/list");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid book ID format.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");
        }
    }

    /**
     * Handles the HTTP POST request to update book details.
     *
     * Steps: 
     * 1. Parse form parameters and populate a BookDTO object. 
     * 2. Update the book in the database using BookDAO. 
     * 3. Redirect to the book list page on success. 
     * 4. Forward back to the edit form with an error message on failure.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int bookId = Integer.parseInt(request.getParameter("id"));

            BookDTO book = new BookDTO();
            book.setBookID(bookId);
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

            BookDAO bookDAO = new BookDAO();
            bookDAO.updateBook(book);

            request.getSession().setAttribute("success", "Book updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid data format. Please check your inputs.");
            response.sendRedirect(request.getContextPath() + "/admin/book/edit?id=" + request.getParameter("id"));
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Failed to update the book. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/book/edit?id=" + request.getParameter("id"));
        }
    }
}
