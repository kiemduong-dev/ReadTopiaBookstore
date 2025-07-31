//package controller.payment;
//
//import dao.BookDAO;
//import dao.CartDAO;
//import dao.OrderDAO;
//import dao.OrderDetailDAO;
//import dao.PromotionDAO;
//import dto.BookDTO;
//import dto.CartDTO;
//import dto.OrderDTO;
//import dto.OrderDetailDTO;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//
//import java.io.IOException;
//import java.sql.Timestamp;
//import java.util.*;
//
//@WebServlet("/payment/process")
//public class PaymentProcessServlet extends HttpServlet {
//
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.setContentType("text/html;charset=UTF-8");
//
//        HttpSession session = request.getSession();
//        String username = (String) session.getAttribute("username");
//        Integer role = (Integer) session.getAttribute("role");
//
//        if (username == null || role == null || role != 4) {
//            response.sendRedirect(request.getContextPath() + "/login");
//            return;
//        }
//
//        try {
//            String paymentMethod = request.getParameter("paymentMethod");
//            String orderAddress = request.getParameter("orderAddress");
//            String type = request.getParameter("type");
//
//            if (paymentMethod == null || orderAddress == null || orderAddress.trim().isEmpty()) {
//                request.setAttribute("error", "Missing payment information.");
//                request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
//                return;
//            }
//
//            CartDAO cartDAO = new CartDAO();
//            BookDAO bookDAO = new BookDAO();
//            OrderDAO orderDAO = new OrderDAO();
//            OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
//            PromotionDAO promotionDAO = new PromotionDAO(); // ✅ Thêm dòng này
//
//            List<CartDTO> cartItems = new ArrayList<>();
//            List<Integer> selectedCartIDs = new ArrayList<>();
//            double totalAmount = 0;
//
//            if ("buynow".equals(type)) {
//                int bookId = Integer.parseInt(request.getParameter("bookId"));
//                int quantity = Integer.parseInt(request.getParameter("quantity"));
//
//                BookDTO book = bookDAO.getBookByID(bookId);
//                if (book == null || quantity > book.getBookQuantity()) {
//                    request.setAttribute("error", "Not enough stock for this book.");
//                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
//                    return;
//                }
//
//                CartDTO temp = new CartDTO();
//                temp.setBookID(bookId);
//                temp.setQuantity(quantity);
//                cartItems.add(temp);
//
//                totalAmount = quantity * book.getBookPrice();
//
//            } else {
//                String[] selectedCartIdsStr = request.getParameterValues("selectedCartIDs");
//
//                if (selectedCartIdsStr != null && selectedCartIdsStr.length > 0) {
//                    for (String idStr : selectedCartIdsStr) {
//                        int cartId = Integer.parseInt(idStr);
//                        CartDTO cartItem = cartDAO.findByCartID(cartId);
//                        if (cartItem != null) {
//                            BookDTO book = bookDAO.getBookByID(cartItem.getBookID());
//                            if (book != null) {
//                                if (cartItem.getQuantity() > book.getBookQuantity()) {
//                                    request.setAttribute("error", "Not enough stock for " + book.getBookTitle() + ".");
//                                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
//                                    return;
//                                }
//
//                                totalAmount += cartItem.getQuantity() * book.getBookPrice();
//                                cartItems.add(cartItem);
//                                selectedCartIDs.add(cartId);
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (cartItems.isEmpty()) {
//                request.setAttribute("error", "No valid items to checkout.");
//                request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
//                return;
//            }
//
//            double discountAmount = 0;
//            String discountRaw = request.getParameter("discountAmount");
//            if (discountRaw != null && !discountRaw.trim().isEmpty()) {
//                discountAmount = Double.parseDouble(discountRaw);
//            }
//
//            double finalAmount = totalAmount - discountAmount;
//            if (finalAmount < 0) {
//                finalAmount = 0;
//            }
//
//            OrderDTO order = new OrderDTO();
//            order.setUsername(username);
//            order.setOrderDate(new Timestamp(new Date().getTime()));
//            order.setTotalAmount(finalAmount);
//            order.setOrderStatus("CASH".equalsIgnoreCase(paymentMethod) ? 0 : 5);
//            order.setOrderAddress(orderAddress);
//
//            String promotionIdRaw = request.getParameter("promotionID");
//            if (promotionIdRaw != null && !promotionIdRaw.trim().isEmpty()) {
//                try {
//                    int promotionID = Integer.parseInt(promotionIdRaw.trim());
//                    if (promotionID != 0) {
//                        order.setProID(promotionID);
//                    }
//                } catch (NumberFormatException e) {
//                    // Ignore invalid promotion ID
//                }
//            }
//
//            int orderID = orderDAO.createOrder(order);
//
//            // ✅ Nếu có mã khuyến mãi, trừ số lượng
//            if (order.getProID() != null) {
//                promotionDAO.decreasePromotionQuantity(order.getProID());
//            }
//
//            for (CartDTO cart : cartItems) {
//                BookDTO book = bookDAO.getBookByID(cart.getBookID());
//                if (book != null) {
//                    OrderDetailDTO detail = new OrderDetailDTO();
//                    detail.setOrderID(orderID);
//                    detail.setBookID(book.getBookID());
//                    detail.setQuantity(cart.getQuantity());
//                    detail.setTotalPrice(book.getBookPrice() * cart.getQuantity());
//                    orderDetailDAO.addOrderDetail(detail);
//
//                    int newQuantity = book.getBookQuantity() - cart.getQuantity();
//                    bookDAO.updateBookQuantity(book.getBookID(), Math.max(0, newQuantity));
//                }
//            }
//
//            if (!"buynow".equals(type)) {
//                cartDAO.deleteMultipleFromCart(selectedCartIDs, username);
//            }
//
//            request.setAttribute("orderID", orderID);
//            request.getRequestDispatcher("/WEB-INF/view/order/confirmation.jsp").forward(request, response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            request.setAttribute("error", "An error occurred during payment processing.");
//            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        processRequest(request, response);
//    }
//}
