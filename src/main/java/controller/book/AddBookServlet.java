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
 * Supports: - GET: Loads available categories and displays the add book form.
 *           - POST: Processes the submitted form, inserts the book into the database,
 *             and maps it to the selected category.
 *
 * URL: /admin/book/add
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/admin/book/add")
public class AddBookServlet extends HttpServlet {

    private static final int ACTIVE_STATUS = 1;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        BookDAO bookDAO = new BookDAO();
        List<String> authors = bookDAO.getAllAuthors();
        List<String> translators = bookDAO.getAllTranslators();
        List<String> publishers = bookDAO.getAllPublishers();

        request.setAttribute("categoryList", categories);
        request.setAttribute("authors", authors);
        request.setAttribute("translators", translators);
        request.setAttribute("publishers", publishers);

        request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
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
            book.setBookStatus(ACTIVE_STATUS);

            int categoryId = Integer.parseInt(request.getParameter("categoryID"));
book.setCategoryID(categoryId);

            BookDAO bookDAO = new BookDAO();
            int bookId = bookDAO.insertBook(book);

            if (bookId != -1) {
                bookDAO.insertBookCategory(bookId, categoryId);

                // ✅ Thông báo rõ ràng hơn
                String successMessage = "Book \"" + book.getBookTitle() + "\" has been added successfully.";
                request.getSession().setAttribute("success", successMessage);

                response.sendRedirect(request.getContextPath() + "/admin/book/list");
            } else {
                throw new Exception("Failed to insert book.");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Failed to add book. Please check your input and try again.");

            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();
            request.setAttribute("categoryList", categories);

            BookDAO bookDAO = new BookDAO();
            request.setAttribute("authors", bookDAO.getAllAuthors());
            request.setAttribute("translators", bookDAO.getAllTranslators());
            request.setAttribute("publishers", bookDAO.getAllPublishers());

            request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
        }
    }
}