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

@WebServlet("/customer/book/list")
public class BookListCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");
        String sort = request.getParameter("sort");
        String catIdParam = request.getParameter("catID");

        BookDAO bookDAO = new BookDAO();
        List<BookDTO> books;

        // Ưu tiên filter theo category nếu có
        if (catIdParam != null && !catIdParam.isEmpty()) {
            try {
                int catID = Integer.parseInt(catIdParam);
                books = bookDAO.getBooksByCategory(catID);
            } catch (NumberFormatException e) {
                books = bookDAO.getAllBooks();
            }
        } else if (keyword != null && !keyword.isEmpty()) {
            books = bookDAO.searchBooksByTitleOrAuthor(keyword);
        } else if (sort != null && !sort.isEmpty()) {
            books = bookDAO.getBooksSortedBy(sort);
        } else {
            books = bookDAO.getAllBooks();
        }

        // Load danh mục
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> categories = categoryDAO.getAllCategories();

        // Gửi dữ liệu sang view
        request.setAttribute("bookList", books);
        request.setAttribute("categoryList", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sort", sort);
        request.setAttribute("catID", catIdParam);

        request.getRequestDispatcher("/WEB-INF/view/book/homepage.jsp").forward(request, response);
    }
}
