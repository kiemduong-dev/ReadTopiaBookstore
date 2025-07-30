package controller.order;

import dao.BookDAO;
import dao.CartDAO;
import dao.PromotionDAO;
import dto.AccountDTO;
import dto.BookDTO;
import dto.CartDTO;
import dto.PromotionDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import util.DBContext;

@WebServlet("/order/checkout")
public class CheckoutServlet extends HttpServlet {

    public static class CartItemWithBook {

        private CartDTO cartItem;
        private BookDTO book;
        private double itemTotal;

        public CartItemWithBook(CartDTO cartItem, BookDTO book) {
            this.cartItem = cartItem;
            this.book = book;
            this.itemTotal = cartItem.getQuantity() * book.getBookPrice();
        }

        public CartDTO getCartItem() {
            return cartItem;
        }

        public BookDTO getBook() {
            return book;
        }

        public double getItemTotal() {
            return itemTotal;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 4) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String bookIdRaw = request.getParameter("bookID");
        String quantityRaw = request.getParameter("quantity");
        String idsRaw = request.getParameter("ids");
        String promotionIdRaw = request.getParameter("promotionID");

        try ( Connection conn = DBContext.getConnection()) {
            BookDAO bookDAO = new BookDAO();
            CartDAO cartDAO = new CartDAO();
            PromotionDAO promotionDAO = new PromotionDAO(conn);

            int promotionID = 0;
            if (promotionIdRaw != null && !promotionIdRaw.trim().isEmpty()) {
                try {
                    promotionID = Integer.parseInt(promotionIdRaw.trim());
                } catch (NumberFormatException e) {
                    promotionID = 0;
                }
            }

            double totalAmount = 0;

            if (bookIdRaw != null && quantityRaw != null) {
                // Buy Now flow
                int bookID = Integer.parseInt(bookIdRaw);
                int quantity = Integer.parseInt(quantityRaw);

                BookDTO book = bookDAO.getBookByID(bookID);
                if (book == null || quantity <= 0 || quantity > book.getBookQuantity()) {
                    response.sendRedirect(request.getContextPath() + "/customer/book/detail?id=" + bookID + "&error=invalid");
                    return;
                }

                totalAmount = quantity * book.getBookPrice();

                request.setAttribute("type", "buynow");
                request.setAttribute("bookId", bookID);
                request.setAttribute("bookTitle", book.getBookTitle());
                request.setAttribute("unitPriceRaw", book.getBookPrice());
                request.setAttribute("quantity", quantity);
                request.setAttribute("amountRaw", totalAmount);

            } else {
                // Cart checkout flow
                List<CartDTO> selectedItems;

                if (idsRaw != null && !idsRaw.trim().isEmpty()) {
                    selectedItems = new ArrayList<>();
                    String[] idStrings = idsRaw.split(",");
                    for (String idStr : idStrings) {
                        try {
                            int cartID = Integer.parseInt(idStr.trim());
                            CartDTO item = cartDAO.findByCartID(cartID);
                            if (item != null && username.equals(item.getUsername())) {
                                selectedItems.add(item);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                } else {
                    selectedItems = cartDAO.getCartByUsername(username);
                }

                if (selectedItems == null || selectedItems.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/cart/view?error=empty-cart");
                    return;
                }

                if (selectedItems.size() == 1) {
                    // Single cart item
                    CartDTO item = selectedItems.get(0);
                    BookDTO book = bookDAO.getBookByID(item.getBookID());

                    if (book == null || !cartDAO.isStockAvailable(item.getBookID(), item.getQuantity())) {
                        response.sendRedirect(request.getContextPath() + "/cart/view?error=insufficient_stock");
                        return;
                    }

                    totalAmount = item.getQuantity() * book.getBookPrice();

                    request.setAttribute("type", "single-cart");
                    request.setAttribute("bookTitle", book.getBookTitle());
                    request.setAttribute("quantity", item.getQuantity());
                    request.setAttribute("unitPriceRaw", book.getBookPrice());
                    request.setAttribute("amountRaw", totalAmount);

                    List<Integer> singleIdList = new ArrayList<>();
                    singleIdList.add(item.getCartID());
                    request.setAttribute("selectedItems", singleIdList);

                } else {
                    // Multi-cart
                    List<CartItemWithBook> cartItemsWithBooks = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();

                    for (CartDTO item : selectedItems) {
                        if (!cartDAO.isStockAvailable(item.getBookID(), item.getQuantity())) {
                            response.sendRedirect(request.getContextPath() + "/cart/view?error=insufficient_stock");
                            return;
                        }

                        BookDTO book = bookDAO.getBookByID(item.getBookID());
                        if (book != null) {
                            cartItemsWithBooks.add(new CartItemWithBook(item, book));
                            totalAmount += item.getQuantity() * book.getBookPrice();
                            ids.add(item.getCartID());
                        }
                    }

                    request.setAttribute("type", "multi-cart");
                    request.setAttribute("cartItemsWithBooks", cartItemsWithBooks);
                    request.setAttribute("orderItems", cartItemsWithBooks.size());
                    request.setAttribute("amountRaw", totalAmount);
                    request.setAttribute("selectedItems", ids);
                }
            }

            // Áp dụng khuyến mãi (không check valid)
            setPromotionAttributes(request, promotionID, promotionDAO, totalAmount);

            // Load thông tin account
            AccountDTO account = (AccountDTO) session.getAttribute("account");
            request.setAttribute("account", account);

            // Load danh sách khuyến mãi còn hạn, còn số lượng
            List<PromotionDTO> promotions = promotionDAO.getValidPromotions();
            request.setAttribute("promotions", promotions);

            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Checkout failed");
        }
    }

    private void setPromotionAttributes(HttpServletRequest request, int promotionID, PromotionDAO promotionDAO, double originalAmount) throws Exception {
        double discountAmount = 0;
        double finalAmount = originalAmount;

        PromotionDTO selectedPromotion = null;

        if (promotionID > 0) {
            selectedPromotion = promotionDAO.getPromotionByID(promotionID); // Không check valid thời gian

            if (selectedPromotion != null) {
                double discountPercent = selectedPromotion.getDiscount();
                discountAmount = Math.round(originalAmount * discountPercent / 100.0);
                finalAmount = originalAmount - discountAmount;
            }
        }

        request.setAttribute("promotionID", promotionID);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("finalAmount", finalAmount);
        request.setAttribute("selectedPromotion", selectedPromotion);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
