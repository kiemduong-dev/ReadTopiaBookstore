package controller.order;

import dao.BookDAO;
import dao.CartDAO;
import dao.VoucherDAO;
import dto.AccountDTO;
import dto.BookDTO;
import dto.CartDTO;
import dto.VoucherDTO;
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
        String voucherIdRaw = request.getParameter("voucherID");

        try (Connection conn = DBContext.getConnection()) {
            BookDAO bookDAO = new BookDAO();
            CartDAO cartDAO = new CartDAO();
            VoucherDAO voucherDAO = new VoucherDAO();

            int voucherID = 0;
            if (voucherIdRaw != null && !voucherIdRaw.trim().isEmpty()) {
                try {
                    voucherID = Integer.parseInt(voucherIdRaw.trim());
                } catch (NumberFormatException e) {
                    voucherID = 0;
                }
            }

            double totalAmount = 0;

            if (bookIdRaw != null && quantityRaw != null) {
                int bookID = Integer.parseInt(bookIdRaw);
                int quantity = Integer.parseInt(quantityRaw);

                BookDTO book = bookDAO.getBookByID(bookID);
                if (book == null || quantity <= 0 || quantity > book.getBookQuantity()) {
                    response.sendRedirect(request.getContextPath() + "/homepage/book/detail?id=" + bookID + "&error=invalid");
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
                        } catch (NumberFormatException ignored) {}
                    }
                } else {
                    selectedItems = cartDAO.getCartByUsername(username);
                }

                if (selectedItems == null || selectedItems.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/cart/view?error=empty-cart");
                    return;
                }

                if (selectedItems.size() == 1) {
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

            setVoucherAttributes(request, voucherID, voucherDAO, totalAmount);

            AccountDTO account = (AccountDTO) session.getAttribute("account");
            request.setAttribute("account", account);

            List<VoucherDTO> vouchers = voucherDAO.getValidVouchers();
            request.setAttribute("vouchers", vouchers);

            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Checkout failed");
        }
    }

    private void setVoucherAttributes(HttpServletRequest request, int voucherID, VoucherDAO voucherDAO, double originalAmount) throws Exception {
        double discountAmount = 0;
        double finalAmount = originalAmount;

        VoucherDTO selectedVoucher = null;

        if (voucherID > 0) {
            selectedVoucher = voucherDAO.getVoucherByID(voucherID);

            if (selectedVoucher != null) {
                double discountPercent = selectedVoucher.getDiscount();
                discountAmount = Math.round(originalAmount * discountPercent / 100.0);
                finalAmount = originalAmount - discountAmount;
            }
        }

        request.setAttribute("voucherID", voucherID);
        request.setAttribute("discountAmount", discountAmount);
        request.setAttribute("finalAmount", finalAmount);
        request.setAttribute("selectedVoucher", selectedVoucher);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
