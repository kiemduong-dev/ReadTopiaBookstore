package controller.payment;

import dao.BookDAO;
import dao.CartDAO;
import dto.BookDTO;
import dto.CartDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@WebServlet("/payment/online")
public class PaymentOnlineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();

        try {
            String username = (String) session.getAttribute("username");
            Integer role = (Integer) session.getAttribute("role");

            if (username == null || role == null || role != 1) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String type = request.getParameter("type");
            String paymentMethod = request.getParameter("paymentMethod");
            String addressRaw = request.getParameter("orderAddress");
            String amountParam = request.getParameter("amount");

            if (type == null || paymentMethod == null || addressRaw == null || amountParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required data.");
                return;
            }

            String orderAddress = URLDecoder.decode(addressRaw, "UTF-8");

            BookDAO bookDAO = new BookDAO();
            CartDAO cartDAO = new CartDAO();

            List<CartDTO> selectedItems = new ArrayList<>();
            List<Integer> selectedCartIDs = new ArrayList<>();
            double totalAmount = 0;

            if ("buynow".equals(type)) {
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
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

                totalAmount = quantity * book.getBookPrice();
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
                        BookDTO book = bookDAO.getBookByID(item.getBookID());
                        if (book != null) {
                            totalAmount += item.getQuantity() * book.getBookPrice();
                            selectedItems.add(item);
                            selectedCartIDs.add(cartId);
                        }
                    }
                }

                if (selectedItems.isEmpty()) {
                    request.setAttribute("error", "No valid items found for checkout.");
                    request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);
                    return;
                }
            }

            String transferCode = "TMP-" + UUID.randomUUID().toString().substring(0, 8);
            session.setAttribute("transferCode", transferCode);

            request.setAttribute("transferCode", transferCode);
            request.setAttribute("amount", totalAmount);
            request.setAttribute("orderAddress", orderAddress);
            request.setAttribute("paymentMethod", paymentMethod);
            request.setAttribute("type", type);

            List<String> idsAsString = new ArrayList<>();
            for (Integer id : selectedCartIDs) {
                idsAsString.add(String.valueOf(id));
            }
            String selectedCartIDsString = String.join(",", idsAsString);

            request.setAttribute("bookId", request.getParameter("bookId"));
            request.setAttribute("quantity", request.getParameter("quantity"));
            request.setAttribute("selectedCartIDsString", selectedCartIDsString);

            request.getRequestDispatcher("/WEB-INF/view/payment/process.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to process payment.");
        }
    }
}
