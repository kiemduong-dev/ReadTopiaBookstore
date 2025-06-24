package controller.supplier;
import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/supplier/detail")
public class SupplierDetailServlet extends HttpServlet {

    private SupplierDAO supplierDAO;

    @Override
    public void init() {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            SupplierDTO supplier = supplierDAO.getSupplierById(id);
            request.setAttribute("supplier", supplier);
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        }
    }
}
