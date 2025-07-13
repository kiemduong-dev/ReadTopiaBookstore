package controller.stock;

import dao.BookDAO;
import dao.ImportStockDAO;
import dao.StaffDAO;
import dao.SupplierDAO;
import dto.ImportStockDTO;
import dto.ImportStockDetailDTO;
import dto.Data;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

@WebServlet("/admin/stock/list")
public class ImportStockServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private ImportStockDAO dao;

    @Override
    public void init() {
        dao = new ImportStockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = Optional.ofNullable(request.getParameter("action")).orElse("list");

        try {
            switch (action) {
                case "add":
                    forwardAddPage(request, response);
                    break;
                case "paging":
                    handlePagingAjax(request, response);
                    break;
                default:
                    handleListPage(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/stock/list.jsp").forward(request, response);
        }
    }

    private void handleListPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int total = dao.getTotalImportStockCount();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;

        List<ImportStockDTO> stocks = dao.getImportStocksByPage(page, PAGE_SIZE);
        request.setAttribute("importList", stocks);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("/WEB-INF/view/admin/stock/list.jsp").forward(request, response);
    }

    private void handlePagingAjax(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("value"));
            if (page < 1) page = 1;
        } catch (NumberFormatException ignored) {}

        int total = dao.getTotalImportStockCount();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        List<ImportStockDTO> stocks = dao.getImportStocksByPage(page, PAGE_SIZE);
        Data helper = new Data();

        Map<String, String> result = new HashMap<>();
        result.put("rowsHtml", helper.getInterfaceImportStock(stocks));
        result.put("paginationHtml", helper.getStockPaginationHTML(page, totalPages));

        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int supID = Integer.parseInt(request.getParameter("sup"));
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            Date importDate = Date.valueOf(request.getParameter("importDate"));

            String[] bookIDs = request.getParameterValues("bookIDList[]");
            String[] quantities = request.getParameterValues("quantityList[]");
            String[] prices = request.getParameterValues("priceList[]");

            if (bookIDs == null || quantities == null || prices == null ||
                    bookIDs.length == 0 || bookIDs.length != quantities.length || quantities.length != prices.length) {
                request.setAttribute("errorMessage", "❌ Please fill in all book details.");
                forwardAddPage(request, response);
                return;
            }

            List<ImportStockDetailDTO> detailList = new ArrayList<>();
            for (int i = 0; i < bookIDs.length; i++) {
                int bookID = Integer.parseInt(bookIDs[i]);
                int quantity = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(prices[i]);

                if (quantity <= 0 || price <= 0) {
                    request.setAttribute("errorMessage", "❌ Quantity and price must be positive.");
                    forwardAddPage(request, response);
                    return;
                }

                detailList.add(new ImportStockDetailDTO(0, bookID, 0, quantity, price));
            }

            ImportStockDTO stock = new ImportStockDTO(0, supID, importDate, staffID, 1);
            dao.insertFullImportStock(stock, detailList);

            response.sendRedirect(request.getContextPath() + "/admin/stock/list?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Failed to save. Please check your input.");
            forwardAddPage(request, response);
        }
    }

   private void forwardAddPage(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    SupplierDAO supplierDAO = new SupplierDAO();
    StaffDAO staffDAO = new StaffDAO();
    BookDAO bookDAO = new BookDAO();

    request.setAttribute("supplierList", supplierDAO.getActiveSuppliers());
    request.setAttribute("staffList", staffDAO.getAllActiveSellerAndWarehouseStaff()); 
    request.setAttribute("bookList", bookDAO.getAllBooks());

    request.getRequestDispatcher("/WEB-INF/view/admin/stock/add.jsp").forward(request, response);
}

}
