package controller.order;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.VoucherDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import dto.VoucherDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/order/details")
public class OrderDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || (role != 0 && role != 2)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIDStr = request.getParameter("orderID");
        try {
            int orderID = Integer.parseInt(orderIDStr);

            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderID);

            if (order != null) {
                OrderDetailDAO detailDAO = new OrderDetailDAO();
                List<OrderDetailDTO> orderDetails = detailDAO.getOrderDetailsByOrderID(orderID);

                // Lấy thông tin Voucher nếu có
                VoucherDTO voucher = null;
                if (order.getVouID() != null) {
                    VoucherDAO voucherDAO = new VoucherDAO();
                    voucher = voucherDAO.getVoucherByID(order.getVouID());
                }

                request.setAttribute("order", order);
                request.setAttribute("orderDetails", orderDetails);
                request.setAttribute("voucher", voucher);

                request.getRequestDispatcher("/WEB-INF/view/order/details.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException("Database error occurred", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException("Database error occurred", ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Displays order details for staff, including voucher info if available.";
    }
}
