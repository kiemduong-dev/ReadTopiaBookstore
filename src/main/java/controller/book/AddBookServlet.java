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
 * AddBookServlet - Handles book creation by admin. - GET: show form to add book
 * - POST: process form submission
 *
 * @author Vuong Chi Bao_CE182018
 */
@WebServlet("/admin/book/add")
public class AddBookServlet extends HttpServlet {

    /**
     * Handles HTTP GET method. Loads category list and forwards to add.jsp.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException
     * @throws IOException
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
     * Handles HTTP POST method. Extracts form data, creates BookDTO, inserts
     * book into DB. Redirects to book list if successful, or reloads form with
     * error message if failed.
     *
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            BookDTO book = new BookDTO();

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

            // Default values
            book.setBookQuantity(1);
            book.setBookStatus(1);

            BookDAO dao = new BookDAO();
            dao.insertBook(book);

            response.sendRedirect(request.getContextPath() + "/admin/book/list");

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();

            request.setAttribute("error", "Failed to add book. Please check your input.");

            // Reload category list in case of error
            CategoryDAO categoryDAO = new CategoryDAO();
            List<CategoryDTO> categories = categoryDAO.getAllCategories();
            request.setAttribute("categoryList", categories);

            request.getRequestDispatcher("/WEB-INF/view/admin/book/add.jsp").forward(request, response);
        }
    }
}
