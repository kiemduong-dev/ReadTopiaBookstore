package controller.order;

import dao.OrderDAO;
import dao.OrderDetailDAO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/order/customerdetails")
public class OrderCustomerDetailServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        // Chỉ cho khách hàng (role = 1) truy cập
        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIDStr = request.getParameter("orderID");
        try {
            int orderID = Integer.parseInt(orderIDStr);

            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderID);

            if (order != null && username.equals(order.getUsername())) {
                OrderDetailDAO detailDAO = new OrderDetailDAO();
                List<OrderDetailDTO> orderDetails = detailDAO.getOrderDetailsByOrderID(orderID);

                request.setAttribute("order", order);
                request.setAttribute("orderDetails", orderDetails);
                request.getRequestDispatcher("/WEB-INF/view/order/customerdetails.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied or order not found");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Displays order details for customer";
    }
}
