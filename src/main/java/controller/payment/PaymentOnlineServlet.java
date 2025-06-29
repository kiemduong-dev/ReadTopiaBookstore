package controller.payment;

import dao.PaymentDAO;
import dto.PaymentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/payment/online")
public class PaymentOnlineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Lấy dữ liệu từ URL
            String orderId = request.getParameter("orderId");
            String paymentMethod = request.getParameter("paymentMethod");
            double amount = Double.parseDouble(request.getParameter("amount"));
            String orderAddress = request.getParameter("orderAddress");

            // Tạo paymentId
            String paymentId = UUID.randomUUID().toString();

            // Tạo đối tượng Payment
            PaymentDTO payment = new PaymentDTO();
            payment.setPaymentId(paymentId);
            payment.setOrderId(orderId);
            payment.setAmount(amount);
            payment.setStatus("PENDING");
            payment.setPaymentMethod(paymentMethod);

            // Lưu thanh toán
            PaymentDAO paymentDAO = new PaymentDAO();
            paymentDAO.insertPayment(payment);

            // Tạo nội dung chuyển khoản & QR
            String transferCode = "DH" + orderId + "-" + paymentId.substring(0, 8);
            String qrData = "Chuyen khoan " + amount + " VND voi noi dung: " + transferCode;

            // Truyền sang JSP
            request.setAttribute("payment", payment);
            request.setAttribute("qrData", qrData);
            request.setAttribute("transferCode", transferCode);
            request.setAttribute("amount", amount);
            request.setAttribute("orderId", orderId);
            request.setAttribute("orderAddress", orderAddress);

            // Chuyển trang
            request.getRequestDispatcher("/WEB-INF/view/payment/process.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xử lý thanh toán thất bại");
        }
    }
}
