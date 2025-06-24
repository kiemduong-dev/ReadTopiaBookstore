/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.payment;

import dao.PaymentDAO;
import dao.OrderDAO;
import dto.PaymentDTO;
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
@WebServlet("/payment/confirm")
public class PaymentConfirmServlet extends HttpServlet {

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

        String paymentId = request.getParameter("paymentId");
        String action = request.getParameter("action");

        if (paymentId == null || action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            PaymentDAO paymentDAO = new PaymentDAO();
            PaymentDTO payment = paymentDAO.getPaymentById(paymentId);

            if (payment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found");
                return;
            }

            if ("confirm".equals(action)) {
                // Confirm payment (simulate bank confirmation)
                payment.setStatus("COMPLETED");

                boolean updated = paymentDAO.updatePayment(payment);
                if (updated) {
                    // Update order status to confirmed
                    OrderDAO orderDAO = new OrderDAO();
                    int orderId = Integer.parseInt(payment.getOrderId());
                    orderDAO.updateOrderStatus(orderId, 0); // Processing

                    response.sendRedirect(request.getContextPath()
                            + "/payment/success?paymentId=" + paymentId);
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Failed to confirm payment");
                }
            } else if ("cancel".equals(action)) {
                // Cancel payment
                payment.setStatus("FAILED");
                paymentDAO.updatePayment(payment);

                response.sendRedirect(request.getContextPath()
                        + "/payment/failed?paymentId=" + paymentId);
            }

        } catch (Exception e) {
            System.err.println("❌ PaymentConfirmServlet error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Confirmation failed");
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
        return "Handles payment confirmation";
    }
}
