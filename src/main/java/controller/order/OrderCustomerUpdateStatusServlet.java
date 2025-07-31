package controller.order;

import dao.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/order/update-status")
public class OrderCustomerUpdateStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        // Only allow customer (role == 1)
        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            int newStatus = Integer.parseInt(request.getParameter("newStatus"));

            OrderDAO dao = new OrderDAO();
            boolean success;

            // If cancelling or returning → restore stock
            if (newStatus == 3 || newStatus == 4) {
                success = dao.updateStatusAndRestoreStock(orderID, newStatus);
            } else {
                success = dao.updateOrderStatusForCus(orderID, newStatus);
            }

            if (success) {
                response.sendRedirect(request.getContextPath() + "/order/customerdetails?orderID=" + orderID + "&msg=updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/order/customerdetails?orderID=" + orderID + "&msg=fail");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        }
    }
}
