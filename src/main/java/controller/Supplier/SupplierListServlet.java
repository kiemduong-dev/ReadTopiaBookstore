package controller.Supplier;

import dao.SupplierDAO;
import dto.Data;
import dto.SupplierDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * SupplierListServlet – Handles supplier list management for Admin dashboard
 * Supports: Initial view, paging, search.
 */
@WebServlet("/admin/supplier/list")
public class SupplierListServlet extends HttpServlet {

    private static final int PAGE_SIZE = 5;
    private SupplierDAO supplierDAO;

    @Override
    public void init() {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = Optional.ofNullable(request.getParameter("action")).orElse("list");

        if ("paging".equals(action)) {
            handlePaging(request, response);
        } else if ("search".equals(action)) {
            handleSearch(request, response);
        } else {
            handleInitialPage(request, response);
        }
    }

    private void handleInitialPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int total = supplierDAO.getTotalSuppliers();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        List<SupplierDTO> suppliers = supplierDAO.getSuppliersByPage(page, PAGE_SIZE);

        request.setAttribute("suppliers", suppliers);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("/WEB-INF/view/admin/supplier/list.jsp").forward(request, response);
    }

    private void handlePaging(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int page = parsePageNumber(request.getParameter("value"));
        int total = supplierDAO.getTotalSuppliers();
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        List<SupplierDTO> suppliers = supplierDAO.getSuppliersByPage(page, PAGE_SIZE);

        Data helper = new Data();
        String rowsHtml = helper.getInterfaceSupplier(suppliers);
        String paginationHtml = helper.getSupplierPaginationHTML(page, totalPages);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"rowsHtml\": \"" + escapeForJson(rowsHtml) + "\","
                + "\"paginationHtml\": \"" + escapeForJson(paginationHtml) + "\"}");
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String query = Optional.ofNullable(request.getParameter("query")).orElse("").trim();
        List<SupplierDTO> suppliers = supplierDAO.getSupplierBySupplierName(query);

        Data helper = new Data();
        String rowsHtml = helper.getInterfaceSupplier(suppliers);
        String paginationHtml = helper.getSupplierPaginationHTML(1, 1);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"rowsHtml\": \"" + escapeForJson(rowsHtml) + "\","
                + "\"paginationHtml\": \"" + escapeForJson(paginationHtml) + "\"}");
    }

    private int parsePageNumber(String value) {
        try {
            return Math.max(Integer.parseInt(value), 1);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private String escapeForJson(String html) {
        if (html == null) return "";
        return html.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "").replace("\r", "");
    }
}
