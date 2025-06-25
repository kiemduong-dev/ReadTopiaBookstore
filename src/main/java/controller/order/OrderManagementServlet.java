/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.order;

import dao.OrderDAO;
import dto.OrderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author NGUYEN THAI ANH
 */
@WebServlet("/order/management")
public class OrderManagementServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        OrderDAO dao = new OrderDAO();
        List<OrderDTO> orders = null;

        if ("search".equals(action)) {
            String orderIDStr = request.getParameter("orderID");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            try {
                if (orderIDStr != null && !orderIDStr.trim().isEmpty()) {
                    int orderID = Integer.parseInt(orderIDStr);
                    orders = dao.searchOrdersByID(orderID);
                } else if (startDateStr != null && !startDateStr.trim().isEmpty()
                        && endDateStr != null && !endDateStr.trim().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = new Date(sdf.parse(startDateStr).getTime());
                    Date endDate = new Date(sdf.parse(endDateStr).getTime());
                    orders = dao.searchOrdersByDateRange(startDate, endDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Invalid search parameters");
            }
        } else {
            orders = dao.getAllOrders();
        }

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/view/order/management.jsp").forward(request, response);
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
        return "Handles order management for staff";
    }
}
