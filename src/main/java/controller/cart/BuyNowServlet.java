package controller.order;

import dao.BookDAO;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.BookDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;

@WebServlet("/buynow")
public class BuyNowServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bookIDStr = request.getParameter("bookID");
        String quantityStr = request.getParameter("quantity");

        try {
            int bookID = Integer.parseInt(bookIDStr);
            int quantity = Integer.parseInt(quantityStr);

            BookDAO bookDAO = new BookDAO();
            BookDTO book = bookDAO.getBookByID(bookID);

            if (book == null || quantity <= 0 || quantity > book.getBookQuantity()) {
                response.sendRedirect(request.getContextPath() + "/customer/book/detail?bookID=" + bookID + "&error=invalid");
                return;
            }

            double totalPrice = quantity * book.getBookPrice();

            // 1. Tạo đơn hàng
            OrderDTO order = new OrderDTO();
            order.setUsername(username);
            order.setOrderDate(new Date());
            order.setOrderStatus(0); // Đang xử lý
            order.setProID(null);
            order.setStaffID(null);
            order.setOrderAddress("Chưa cập nhật");

            int orderID = new OrderDAO().createOrder(order);

            // 2. Chi tiết đơn hàng
            OrderDetailDTO detail = new OrderDetailDTO();
            detail.setOrderID(orderID);
            detail.setBookID(bookID);
            detail.setQuantity(quantity);
            detail.setTotalPrice(totalPrice);

            new OrderDetailDAO().addOrderDetail(detail);

            // 3. Trừ tồn kho
            book.setBookQuantity(book.getBookQuantity() - quantity);
            bookDAO.updateBook(book);

            // 4. Điều hướng xác nhận
            response.sendRedirect(request.getContextPath() + "/order/confirmation?orderID=" + orderID);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Buy Now failed");
        }
    }
}
