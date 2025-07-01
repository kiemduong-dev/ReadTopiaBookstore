package controller.stock;

import dao.BookDAO;
import dao.ImportStockDAO;
import dao.StaffDAO;
import dao.SupplierDAO;
import dto.ImportStockDTO;
import dto.ImportStockDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Import Stock – Handle listing & adding import stock records
 * @author ReadTopia
 */
@WebServlet("/admin/stock/list")
public class ImportStockServlet extends HttpServlet {

    private ImportStockDAO dao;

    @Override
    public void init() {
        dao = new ImportStockDAO();
    }

    /**
     * Display either Add Form or List of Import Stocks
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = Optional.ofNullable(request.getParameter("action")).orElse("list");

        try {
            if (action.equals("add")) {
                SupplierDAO supplierDAO = new SupplierDAO();
                StaffDAO staffDAO = new StaffDAO();
                BookDAO bookDAO = new BookDAO();

                request.setAttribute("supplierList", supplierDAO.getAllSuppliers());
                request.setAttribute("staffList", staffDAO.getAllStaff());
                request.setAttribute("bookList", bookDAO.getAllBooks());

                request.getRequestDispatcher("/WEB-INF/view/admin/stock/add.jsp").forward(request, response);

            } else {
                // Lấy tất cả nhập kho mà không phân trang
                List<ImportStockDTO> importList = dao.getAllImportStocks(); // Bỏ phân trang
                request.setAttribute("importList", importList);
                request.getRequestDispatcher("/WEB-INF/view/admin/stock/list.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/stock/list.jsp").forward(request, response);
        }
    }

    /**
     * Handle Insert New Import Stock
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int supID = Integer.parseInt(request.getParameter("sup"));
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            Date importDate = Date.valueOf(request.getParameter("importDate"));

            // ✅ Fix: đúng với name="xxx[]" bên JSP
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

    /**
     * Helper: Load dropdown data and forward to Add page
     */
    private void forwardAddPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SupplierDAO supplierDAO = new SupplierDAO();
        StaffDAO staffDAO = new StaffDAO();
        BookDAO bookDAO = new BookDAO();

        request.setAttribute("supplierList", supplierDAO.getAllSuppliers());
        request.setAttribute("staffList", staffDAO.getAllStaff());
        request.setAttribute("bookList", bookDAO.getAllBooks());

        request.getRequestDispatcher("/WEB-INF/view/admin/stock/add.jsp").forward(request, response);
    }
}
