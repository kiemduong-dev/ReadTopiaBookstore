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
     * Steps: 1. Retrieve "id" from request parameters. 2. Validate and parse
     * book ID. 3. Fetch book by ID from database. 4. If found: - Load current
     * category ID of the book. - Load lists of categories, authors,
     * translators, and publishers. - Set attributes to request and forward to
     * edit.jsp. 5. If book not found or ID is invalid: - Redirect to book list
     * page with error message.
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
                // Lấy danh mục hiện tại của sách
                int selectedCategoryID = bookDAO.getCategoryIDByBookID(bookId);
                request.setAttribute("selectedCategoryID", selectedCategoryID);

                // Danh sách category, author, publisher, translator
                CategoryDAO categoryDAO = new CategoryDAO();
                List<CategoryDTO> categories = categoryDAO.getAllCategories();
                request.setAttribute("categoryList", categories);

                List<String> authors = bookDAO.getAllAuthors();
                List<String> translators = bookDAO.getAllTranslators();
                List<String> publishers = bookDAO.getAllPublishers();

                request.setAttribute("book", book);
                request.setAttribute("authors", authors);
                request.setAttribute("translators", translators);
                request.setAttribute("publishers", publishers);

                // Forward to edit page
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
     * Steps: 1. Parse and validate form inputs (book ID, category ID, numeric
     * fields). 2. Create a BookDTO object and set all properties from form
     * data. 3. Call DAO to: - Update book info in Book table. - Update book's
     * category in Book_Category table. 4. On success: redirect to book list
     * with success message. 5. On failure: redirect back to edit form with
     * error message.
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

            // Lấy categoryID và kiểm tra nếu không chọn
            String categoryParam = request.getParameter("categoryID");
            if (categoryParam == null || categoryParam.isEmpty()) {
                request.getSession().setAttribute("error", "Vui lòng chọn danh mục cho sách.");
                response.sendRedirect(request.getContextPath() + "/admin/book/edit?id=" + bookId);
                return;
            }
            int categoryId = Integer.parseInt(categoryParam);

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
            bookDAO.updateBookCategory(bookId, categoryId); // ✅ Cập nhật danh mục

            request.getSession().setAttribute("success", "Book updated successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid data format. Please check your inputs.");
            response.sendRedirect(request.getContextPath() + "/admin/book/edit?id=" + request.getParameter("id"));
        } catch (Exception e) {
            e.printStackTrace(); // để dễ debug
            request.getSession().setAttribute("error", "Failed to update the book. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/book/edit?id=" + request.getParameter("id"));
        }
    }
}
