//package controller.order;
//
//import dao.OrderDAO;
//import dao.OrderDetailDAO;
//import dao.PromotionDAO; // Thêm dòng này
//import dto.OrderDTO;
//import dto.OrderDetailDTO;
//import dto.PromotionDTO; // Thêm dòng này
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@WebServlet("/order/details")
//public class OrderDetailServlet extends HttpServlet {
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException, SQLException {
//
//        response.setContentType("text/html;charset=UTF-8");
//
//        HttpSession session = request.getSession();
//        String username = (String) session.getAttribute("username");
//        Integer role = (Integer) session.getAttribute("role");
//
//        // Giả sử role của nhân viên là 0 (theo code bạn gửi)
//        if (username == null || role == null || role != 0 && role != 2) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        String orderIDStr = request.getParameter("orderID");
//        try {
//            int orderID = Integer.parseInt(orderIDStr);
//
//            OrderDAO orderDAO = new OrderDAO();
//            OrderDTO order = orderDAO.getOrderByID(orderID);
//
//            if (order != null) {
//                OrderDetailDAO detailDAO = new OrderDetailDAO();
//                List<OrderDetailDTO> orderDetails = detailDAO.getOrderDetailsByOrderID(orderID);
//
//                // Lấy thông tin Promotion nếu có (nếu order.getProID() khác null)
//                PromotionDTO promo = null;
//                if (order.getProID() != null) {
//                    PromotionDAO promoDAO = new PromotionDAO();
//                    promo = promoDAO.getPromotionByID(order.getProID());
//                }
//
//                // Truyền dữ liệu sang JSP
//                request.setAttribute("order", order);
//                request.setAttribute("orderDetails", orderDetails);
//                request.setAttribute("promo", promo);
//
//                request.getRequestDispatcher("/WEB-INF/view/order/details.jsp").forward(request, response);
//            } else {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
//            }
//
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
//        }
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            processRequest(request, response);
//        } catch (SQLException ex) {
//            Logger.getLogger(OrderDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
//            throw new ServletException("Database error occurred", ex);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            processRequest(request, response);
//        } catch (SQLException ex) {
//            Logger.getLogger(OrderDetailServlet.class.getName()).log(Level.SEVERE, null, ex);
//            throw new ServletException("Database error occurred", ex);
//        }
//    }
//
//    @Override
//    public String getServletInfo() {
//        return "Displays order details for staff, including voucher info if available.";
//    }
//}
