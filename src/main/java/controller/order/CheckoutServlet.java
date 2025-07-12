package controller.order;

import dao.BookDAO;
import dao.CartDAO;
import dto.AccountDTO;
import dto.BookDTO;
import dto.CartDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/order/checkout")
public class CheckoutServlet extends HttpServlet {

    public static class CartItemWithBook {
        private CartDTO cartItem;
        private BookDTO book;
        private double itemTotal;
        private String formattedItemTotal;

        public CartItemWithBook(CartDTO cartItem, BookDTO book, DecimalFormat formatter) {
            this.cartItem = cartItem;
            this.book = book;
            this.itemTotal = cartItem.getQuantity() * book.getBookPrice();
            this.formattedItemTotal = formatter.format(this.itemTotal);
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

        public String getFormattedItemTotal() {
            return formattedItemTotal;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        Integer role = (Integer) session.getAttribute("role");

        if (username == null || role == null || role != 1) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Setup decimal formatter
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        String bookIdRaw = request.getParameter("bookID");
        String quantityRaw = request.getParameter("quantity");
        String idsRaw = request.getParameter("ids");

        BookDAO bookDAO = new BookDAO();
        CartDAO cartDAO = new CartDAO();

        try {
            if (bookIdRaw != null && quantityRaw != null) {
                // ➤ Buy Now case
                int bookID = Integer.parseInt(bookIdRaw);
                int quantity = Integer.parseInt(quantityRaw);

                BookDTO book = bookDAO.getBookByID(bookID);
                if (book == null || quantity <= 0 || quantity > book.getBookQuantity()) {
                    response.sendRedirect(request.getContextPath() + "/customer/book/detail?id=" + bookID + "&error=invalid");
                    return;
                }

                double total = quantity * book.getBookPrice();
                String formattedUnitPrice = formatter.format(book.getBookPrice());
                String formattedTotal = formatter.format(total);

                request.setAttribute("type", "buynow");
                request.setAttribute("bookId", bookID);
                request.setAttribute("bookTitle", book.getBookTitle());
                request.setAttribute("unitPrice", formattedUnitPrice);
                request.setAttribute("quantity", quantity);
                request.setAttribute("amount", formattedTotal);

            } else {
                // ➤ From Cart case
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

                    double total = item.getQuantity() * book.getBookPrice();
                    String formattedUnitPrice = formatter.format(book.getBookPrice());
                    String formattedTotal = formatter.format(total);

                    List<Integer> singleIdList = new ArrayList<>();
                    singleIdList.add(item.getCartID());

                    request.setAttribute("type", "single-cart");
                    request.setAttribute("bookTitle", book.getBookTitle());
                    request.setAttribute("quantity", item.getQuantity());
                    request.setAttribute("unitPrice", formattedUnitPrice);
                    request.setAttribute("amount", formattedTotal);
                    request.setAttribute("selectedItems", singleIdList);

                } else {
                    List<CartItemWithBook> cartItemsWithBooks = new ArrayList<>();
                    List<Integer> ids = new ArrayList<>();
                    double total = 0;

                    for (CartDTO item : selectedItems) {
                        if (!cartDAO.isStockAvailable(item.getBookID(), item.getQuantity())) {
                            response.sendRedirect(request.getContextPath() + "/cart/view?error=insufficient_stock");
                            return;
                        }

                        BookDTO book = bookDAO.getBookByID(item.getBookID());
                        if (book != null) {
                            cartItemsWithBooks.add(new CartItemWithBook(item, book, formatter));
                            total += item.getQuantity() * book.getBookPrice();
                            ids.add(item.getCartID());
                        }
                    }

                    String formattedTotal = formatter.format(total);

                    request.setAttribute("type", "multi-cart");
                    request.setAttribute("cartItemsWithBooks", cartItemsWithBooks);
                    request.setAttribute("orderItems", cartItemsWithBooks.size());
                    request.setAttribute("amount", formattedTotal);
                    request.setAttribute("selectedItems", ids);
                }
            }

            AccountDTO account = (AccountDTO) session.getAttribute("account");
            request.setAttribute("account", account);

            request.getRequestDispatcher("/WEB-INF/view/order/checkout.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Checkout failed");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles Buy Now and Cart Checkout (single or multiple items)";
    }
}
