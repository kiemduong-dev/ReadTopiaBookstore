/*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.payment;

import dao.OrderDAO;
import dao.PaymentDAO;
import dto.OrderDTO;
import dto.PaymentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author NGUYEN THAI ANH
 */
@WebServlet("/payment/process")
public class PaymentProcessServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        String amountStr = request.getParameter("amount");
        String paymentMethod = request.getParameter("paymentMethod");
        String orderAddress = request.getParameter("orderAddress");

        if (orderIdStr == null || amountStr == null || paymentMethod == null || orderAddress == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu thông tin thanh toán.");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            double amount = Double.parseDouble(amountStr);

            // Update địa chỉ giao hàng
            OrderDAO orderDAO = new OrderDAO();
            OrderDTO order = orderDAO.getOrderByID(orderId);
            if (order != null) {
                order.setOrderAddress(orderAddress);
                orderDAO.updateOrderAddress(order);
            }

            if ("CASH".equals(paymentMethod)) {
                // Thanh toán khi nhận hàng
                orderDAO.updateOrderStatus(orderId, 0); // Đang xử lý
                response.sendRedirect(request.getContextPath() + "/order/confirmation?orderId=" + orderId);
                return;
            }

            if ("CREDIT_CARD".equals(paymentMethod)) {
                // Chuyển hướng sang trang nhập thông tin thẻ
                response.sendRedirect(request.getContextPath() + "/payment/credit?orderId=" + orderId + "&amount=" + amount);
                return;
            }

            // Các phương thức khác bạn có thể xử lý sau (QR_CODE, BANK_TRANSFER,...)
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Phương thức thanh toán chưa được hỗ trợ.");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ.");
        } catch (Exception e) {
            System.err.println("❌ PaymentProcessServlet error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi xử lý thanh toán.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
