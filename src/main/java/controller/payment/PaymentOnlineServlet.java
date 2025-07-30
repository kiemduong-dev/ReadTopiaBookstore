package controller.payment;

import dao.BookDAO;
import dao.CartDAO;
import dto.BookDTO;
import dto.CartDTO;
import dto.PromotionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@WebServlet("/payment/online")
public class PaymentOnlineServlet extends HttpServlet {

    // ✅ Xử lý POST để tránh lỗi 405 khi form submit từ checkout
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Dùng chung logic với GET để render trang QR
    }

    // ✅ Logic hiển thị trang mã QR và xác nhận đơn hàng online
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        try {
            String username = (String) session.getAttribute("username");
            Integer role = (Integer) session.getAttribute("role");

            if (username == null || role == null || role != 4) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String type = request.getParameter("type");
            String paymentMethod = request.getParameter("paymentMethod");
            String addressRaw = request.getParameter("orderAddress");
            String amountParam = request.getParameter("amount");
            String finalAmountParam = request.getParameter("finalAmount");
            String proIDRaw = request.getParameter("promotionID");
            if (proIDRaw == null) {
                proIDRaw = request.getParameter("proID"); // Thử thêm key khác nữa
            }

            Integer proID = null;

            if (proIDRaw != null && !proIDRaw.isEmpty()) {
                try {
                    proID = Integer.parseInt(proIDRaw);
                } catch (NumberFormatException ignored) {
                }
            }

            if (proID == null) {
                PromotionDTO appliedPromotion = (PromotionDTO) session.getAttribute("appliedPromotion");
                if (appliedPromotion != null) {
                    proID = appliedPromotion.getProID();
                }
            }

            if (type == null || paymentMethod == null || addressRaw == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required data.");
                return;
            }

            String orderAddress = URLDecoder.decode(addressRaw, "UTF-8");

            BookDAO bookDAO = new BookDAO();
            CartDAO cartDAO = new CartDAO();

            List<CartDTO> selectedItems = new ArrayList<>();
            List<Integer> selectedCartIDs = new ArrayList<>();

            double totalAmount = 0;

            if (finalAmountParam != null && !finalAmountParam.trim().isEmpty()) {
                totalAmount = Double.parseDouble(finalAmountParam);
            } else if (amountParam != null && !amountParam.trim().isEmpty()) {
                totalAmount = Double.parseDouble(amountParam);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount is required.");
                return;
            }

            if ("buynow".equals(type)) {
                String bookIdRaw = request.getParameter("bookId");
                String quantityRaw = request.getParameter("quantity");

                if (bookIdRaw == null || quantityRaw == null) {
                    response.sendRedirect(request.getContextPath() + "/order/checkout");
                    return;
                }

                int bookId = Integer.parseInt(bookIdRaw);
                int quantity = Integer.parseInt(quantityRaw);

                BookDTO book = bookDAO.getBookByID(bookId);

                if (book == null || quantity > book.getBookQuantity()) {
                    request.setAttribute("error", "Not enough stock for this book.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }

                CartDTO temp = new CartDTO();
                temp.setBookID(bookId);
                temp.setQuantity(quantity);
                selectedItems.add(temp);

                request.setAttribute("bookTitle", book.getBookTitle());
                request.setAttribute("bookId", bookId);
                request.setAttribute("quantity", quantity);

            } else {
                String[] selectedIDs = request.getParameterValues("selectedCartIDs");

                if (selectedIDs == null || selectedIDs.length == 0) {
                    request.setAttribute("error", "No items selected for payment.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }

                for (String idStr : selectedIDs) {
                    int cartId = Integer.parseInt(idStr);
                    CartDTO item = cartDAO.findByCartID(cartId);
                    if (item != null) {
                        selectedItems.add(item);
                        selectedCartIDs.add(cartId);
                    }
                }

                if (selectedItems.isEmpty()) {
                    request.setAttribute("error", "No valid items found for checkout.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }

                String selectedCartIDsString = String.join(",", selectedIDs);
                request.setAttribute("selectedCartIDsString", selectedCartIDsString);
            }

            // Tạo mã chuyển khoản
            String transferCode = (String) session.getAttribute("transferCode");
            if (transferCode == null) {
                transferCode = "TX-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
                session.setAttribute("transferCode", transferCode);
            }

            request.setAttribute("transferCode", transferCode);
            request.setAttribute("finalAmount", totalAmount);
            request.setAttribute("amount", amountParam);
            request.setAttribute("orderAddress", orderAddress);
            request.setAttribute("paymentMethod", paymentMethod);
            request.setAttribute("type", type);
            request.setAttribute("proID", proID);

            // Chuyển đến trang hiển thị QR code
            request.getRequestDispatcher("/WEB-INF/view/payment/process.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process payment.");
        }
    }
}
