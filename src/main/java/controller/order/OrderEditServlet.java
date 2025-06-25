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

/**
 *
 * @author NGUYEN THAI ANH
 */
@WebServlet("/order/edit")
public class OrderEditServlet extends HttpServlet {

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

        if ("POST".equals(request.getMethod())) {
            // Handle status update
            String orderIDStr = request.getParameter("orderID");
            String newStatusStr = request.getParameter("newStatus");

            try {
                int orderID = Integer.parseInt(orderIDStr);
                int newStatus = Integer.parseInt(newStatusStr);

                OrderDAO dao = new OrderDAO();
                boolean success = dao.updateOrderStatus(orderID, newStatus);

                if (success) {
                    response.sendRedirect(request.getContextPath()
                            + "/order/management?msg=success");
                } else {
                    response.sendRedirect(request.getContextPath()
                            + "/order/management?msg=error");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            }
        } else {
            // Show edit form
            String orderIDStr = request.getParameter("orderID");
            try {
                int orderID = Integer.parseInt(orderIDStr);
                OrderDAO dao = new OrderDAO();
                OrderDTO order = dao.getOrderByID(orderID);

                if (order != null) {
                    request.setAttribute("order", order);
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
