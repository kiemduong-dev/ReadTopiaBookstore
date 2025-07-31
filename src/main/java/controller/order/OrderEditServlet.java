package controller.order;

import dao.OrderDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import dto.OrderDTO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/order/edit")
public class OrderEditServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        // Chỉ cho phép role 2 (staff)
        if (username == null || role == null || role != 2) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            // Handle status update
            String orderIDStr = request.getParameter("orderID");
            String newStatusStr = request.getParameter("newStatus");

            try {
                int orderID = Integer.parseInt(orderIDStr);
                int newStatus = Integer.parseInt(newStatusStr);

                OrderDAO dao = new OrderDAO();
                OrderDTO currentOrder = dao.getOrderByID(orderID);

                if (currentOrder == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                    return;
                }

                // Nếu trạng thái giống nhau thì không cập nhật
                if (currentOrder.getOrderStatus() == newStatus) {
                    response.sendRedirect(request.getContextPath() + "/order/management?msg=same");
                    return;
                }

                // Lấy account từ session
                AccountDTO account = (AccountDTO) session.getAttribute("account");
                int staffID = account.getStaffID();

                // Nếu staffID chưa gán trong AccountDTO, thì lấy từ DAO
                if (staffID == 0) {
                    StaffDAO staffDAO = new StaffDAO();
                    staffID = staffDAO.getStaffIDByUsername(username);
                }

                boolean success = dao.updateOrderStatus(orderID, newStatus, staffID);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/order/management?msg=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/order/management?msg=error");
                }

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            }

        } else {
            // Hiển thị form chỉnh sửa
            String orderIDStr = request.getParameter("orderID");

            try {
                int orderID = Integer.parseInt(orderIDStr);
                OrderDAO dao = new OrderDAO();
                OrderDTO order = dao.getOrderByID(orderID);

                if (order != null) {
                    request.setAttribute("order", order);

                    // Lấy username từ staffID nếu có
                    String staffUsername = null;
                    if (order.getStaffID() > 0) {
                        StaffDAO staffDAO = new StaffDAO();
                        StaffDTO staff = staffDAO.getStaffByID(order.getStaffID());
                        if (staff != null) {
                            staffUsername = staff.getUsername();
                        }
                    }

                    request.setAttribute("staffUsername", staffUsername);
                    request.getRequestDispatcher("/WEB-INF/view/order/edit.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                }

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
            }
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
        return "Handles order status editing for staff";
    }
}
