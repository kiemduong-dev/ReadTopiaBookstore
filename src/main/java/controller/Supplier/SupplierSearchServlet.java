package controller.supplier;

import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/supplier/search")
public class SupplierSearchServlet extends HttpServlet {
    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String query = request.getParameter("query");
        List<SupplierDTO> results;

        if (query == null || query.trim().isEmpty()) {
            results = supplierDAO.getAllSuppliers();
        } else {
            results = supplierDAO.getSupplierBySupplierName(query.trim());
        }

        request.setAttribute("suppliers", results);
        request.setAttribute("searchQuery", query); // giữ lại giá trị tìm kiếm
        request.getRequestDispatcher("/WEB-INF/view/admin/supplier/list.jsp").forward(request, response);
    }
}
