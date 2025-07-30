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
 * Add Book Servlet - Handles the creation of new books by Admin.
 *
 * Supports: - GET: Loads available categories and displays the add book form. -
 * POST: Processes the submitted form, inserts the book into the database, and
 * maps it to the selected category.
 *
 * URL: /admin/book/add
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/add")
public class AddBookServlet extends HttpServlet {

    private static final int ACTIVE_STATUS = 1;

    /**
     * Handles the HTTP GET request to display the add book form.
     *
     * Steps: 1. Retrieve all categories using CategoryDAO. 2. Set the category
     * list as a request attribute. 3. Forward the request to the add book JSP
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

        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        BookDAO bookDAO = new BookDAO();
        List<String> authors = bookDAO.getAllAuthors();  // Lấy danh sách tác giả
        List<String> translators = bookDAO.getAllTranslators();  // Lấy danh sách người phiên dịch
        List<String> publishers = bookDAO.getAllPublishers();  // Lấy danh sách nhà xuất bản

        // Đưa các danh sách vào request để gửi vào JSP
        request.setAttribute("categoryList", categories);
        request.setAttribute("authors", authors);
        request.setAttribute("translators", translators);
        request.setAttribute("publishers", publishers);

        // Chuyển tiếp tới trang thêm sách
        request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP POST request to process the submitted add book form.
     *
     * Steps: 1. Extract form data from request parameters. 2. Create a BookDTO
     * and populate its properties. 3. Insert the book using BookDAO and map it
     * to the selected category. 4. Redirect to the book list page on success or
     * reload the form on failure.
     *
     * @param request the HttpServletRequest object containing form submission
     * data
     * @param response the HttpServletResponse object for sending the response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            BookDTO book = new BookDTO();
            book.setBookTitle(request.getParameter("title"));
            book.setAuthor(request.getParameter("author"));  // Lấy tác giả từ dropdown
            book.setTranslator(request.getParameter("translator"));  // Lấy người phiên dịch từ dropdown
            book.setPublisher(request.getParameter("publisher"));  // Lấy nhà xuất bản từ dropdown
            book.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
            book.setIsbn(request.getParameter("isbn"));
            book.setImage(request.getParameter("image"));
            book.setBookDescription(request.getParameter("description"));
            book.setHardcover(Integer.parseInt(request.getParameter("hardcover")));
            book.setDimension(request.getParameter("dimension"));
            book.setWeight(Float.parseFloat(request.getParameter("weight")));
            book.setBookPrice(Double.parseDouble(request.getParameter("price")));
            book.setBookQuantity(Integer.parseInt(request.getParameter("quantity")));
            book.setBookStatus(ACTIVE_STATUS);

            int categoryId = Integer.parseInt(request.getParameter("categoryID"));
            book.setCategoryID(categoryId);

            BookDAO bookDAO = new BookDAO();
            int bookId = bookDAO.insertBook(book);

            if (bookId != -1) {
                bookDAO.insertBookCategory(bookId, categoryId);
                request.getSession().setAttribute("success", "Book added successfully.");
                response.sendRedirect(request.getContextPath() + "/admin/book/list");
            } else {
                throw new Exception("Failed to insert book.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Failed to add book. Please check your input.");

            // Reload category list for form
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();
            request.setAttribute("categoryList", categories);

            // Reload author, translator, and publisher lists for form
            BookDAO bookDAO = new BookDAO();
            request.setAttribute("authors", bookDAO.getAllAuthors());
            request.setAttribute("translators", bookDAO.getAllTranslators());
            request.setAttribute("publishers", bookDAO.getAllPublishers());

            request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
        }
    }

}
