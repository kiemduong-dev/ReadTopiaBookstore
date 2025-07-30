package controller.cart;

import dao.BookDAO;
import dao.CartDAO;
import dto.BookDTO;
import dto.CartDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/cart/add")
public class CartAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bookIdRaw = request.getParameter("bookID");
        String quantityRaw = request.getParameter("quantity");
        String action = request.getParameter("action");

        int bookID, quantity = 1;
        try {
            bookID = Integer.parseInt(bookIdRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect("view?msg=invalid_book");
            return;
        }

        try {
            if (quantityRaw != null && !quantityRaw.trim().isEmpty()) {
                int parsedQuantity = Integer.parseInt(quantityRaw);
                if (parsedQuantity > 0) {
                    quantity = parsedQuantity;
                }
            }
        } catch (NumberFormatException ignored) {
        }

        BookDAO bookDAO = new BookDAO();
        BookDTO book = bookDAO.getBookByID(bookID);

        if (book == null || book.getBookStatus() != 1 || book.getBookQuantity() <= 0) {
            response.sendRedirect("view?msg=book_not_available");
            return;
        }

        // ➤ Nếu là Buy Now → chuyển hướng đến trang thanh toán trực tiếp (không thêm vào giỏ)
        if ("buyNow".equals(action)) {
            if (quantity > book.getBookQuantity()) {
                response.sendRedirect(request.getContextPath() + "/customer/book/detail?id=" + bookID + "&error=outofstock");
                return;
            }

            session.setAttribute("buyNowBook", book);
            session.setAttribute("buyNowQuantity", quantity);
            session.setAttribute("buyNowTotal", quantity * book.getBookPrice());

            response.sendRedirect(request.getContextPath()
                    + "/order/checkout?type=buynow&bookID=" + bookID + "&quantity=" + quantity);
            return;
        }

        // ➤ Nếu là Add to Cart
        CartDAO cartDAO = new CartDAO();
        CartDTO existingItem = cartDAO.findByUsernameAndBookID(username, bookID);

        int totalQuantity = quantity;
        if (existingItem != null) {
            totalQuantity += existingItem.getQuantity();
        }

        if (!cartDAO.isStockAvailable(bookID, totalQuantity)) {
            response.sendRedirect(request.getContextPath()
                    + "/customer/book/detail?id=" + bookID + "&error=insufficient_stock");

            return;
        }

        boolean success;
        if (existingItem == null) {
            CartDTO newItem = new CartDTO(0, username, bookID, quantity);
            success = cartDAO.addToCart(newItem);
        } else {
            existingItem.setQuantity(totalQuantity);
            success = cartDAO.updateCart(existingItem);
        }

        if (success) {
            response.sendRedirect(request.getContextPath()
                    + "/customer/book/detail?id=" + bookID + "&added=true");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/customer/book/detail?id=" + bookID + "&added=false");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
