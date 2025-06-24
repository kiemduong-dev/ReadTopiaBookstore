package controller.book;

import dao.BookDAO;
import dto.BookDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/customer/book/detail")
public class BookDetailCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int bookID = 0;

        if (idParam != null) {
            try {
                bookID = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "view/book/list");
                return;
            }
        }

        BookDAO dao = new BookDAO();
        BookDTO book = dao.getBookByID(bookID);

        if (book != null && book.getBookStatus() == 1) {
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/view/book/detail.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "view/book/list");
        }
    }
} 
