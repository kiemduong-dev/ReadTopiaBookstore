package controller.book;

import dao.BookDAO;
import dao.CategoryDAO;
import dao.NotificationDAO;
import dto.BookDTO;
import dto.CategoryDTO;
import dto.NotificationDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBContext;

/**
 * Book List Customer - Displays the list of books for customers.
 *
 * Retrieves books from the database with optional filtering, sorting, and
 * category selection. Also loads user notifications if the customer is logged
 * in.
 *
 * URL: /customer/book/list
 *
 * Author: CE182018 Vuong Chi Bao
 */
@WebServlet("/homepage/book/list")
public class BookListHomepageServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests to display the book list for customers.
     *
     * Steps: 1. Check for logged-in user and load notifications (if any). 2.
     * Retrieve optional filters: keyword, sort option, category ID. 3. Fetch
     * the list of books based on filters. 4. Load all categories for the filter
     * dropdown. 5. Set data as request attributes and forward to the homepage
     * JSP.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an input or output error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String sortOption = request.getParameter("sort");
        String categoryIdParam = request.getParameter("categoryID"); // đồng bộ với form

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        // ====== FILTER BY CATEGORY ======
        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            try {
                int categoryId = Integer.parseInt(categoryIdParam);
                books = bookDAO.getBooksByCategory(categoryId);
            } catch (NumberFormatException e) {
                books = bookDAO.getAllBooks();
            }
        } else {
            books = bookDAO.getAllBooks();
        }

        // ====== FILTER BY KEYWORD ======
        if (keyword != null && !keyword.isEmpty()) {
            String kwLower = keyword.toLowerCase();
            List<BookDTO> filtered = new ArrayList<BookDTO>();
            for (BookDTO b : books) {
                if (b.getBookTitle().toLowerCase().contains(kwLower)
                        || b.getAuthor().toLowerCase().contains(kwLower)) {
                    filtered.add(b);
                }
            }
            books = filtered;
        }

        // ====== SORT ======
        if (sortOption != null && !sortOption.isEmpty()) {
            if ("price_asc".equals(sortOption)) {
                Collections.sort(books, new Comparator<BookDTO>() {
                    public int compare(BookDTO o1, BookDTO o2) {
                        return Double.compare(o1.getBookPrice(), o2.getBookPrice());
                    }
                });
            } else if ("price_desc".equals(sortOption)) {
                Collections.sort(books, new Comparator<BookDTO>() {
                    public int compare(BookDTO o1, BookDTO o2) {
                        return Double.compare(o2.getBookPrice(), o1.getBookPrice());
                    }
                });
            } else if ("title_asc".equals(sortOption)) {
                Collections.sort(books, new Comparator<BookDTO>() {
                    public int compare(BookDTO o1, BookDTO o2) {
                        return o1.getBookTitle().compareToIgnoreCase(o2.getBookTitle());
                    }
                });
            } else if ("title_desc".equals(sortOption)) {
                Collections.sort(books, new Comparator<BookDTO>() {
                    public int compare(BookDTO o1, BookDTO o2) {
                        return o2.getBookTitle().compareToIgnoreCase(o1.getBookTitle());
                    }
                });
            }
        }

        // ====== LOAD CATEGORY LIST ======
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        // ====== PAGINATION ======
        int pageSize = 8; // 8 books per page
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        int totalBooks = books.size();
        int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

        if (currentPage < 1) {
            currentPage = 1;
        }
        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(currentPage * pageSize, totalBooks);

        List<BookDTO> pageBooks = books.subList(startIndex, endIndex);

        // ====== SET ATTRIBUTES ======
        request.setAttribute("bookList", pageBooks);
        request.setAttribute("categoryList", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sort", sortOption);
        request.setAttribute("categoryID", categoryIdParam); // để giữ lại khi load lại form
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", currentPage);

        // ====== FORWARD ======
        request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);
    }
}
