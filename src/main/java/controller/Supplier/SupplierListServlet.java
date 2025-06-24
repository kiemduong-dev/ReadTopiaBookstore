package controller.supplier;

import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/supplier/list")
public class SupplierListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SupplierDAO dao = new SupplierDAO();
        List<SupplierDTO> suppliers = dao.getAllSuppliers();
        request.setAttribute("suppliers", suppliers);
        request.getRequestDispatcher("/WEB-INF/view/admin/supplier/list.jsp").forward(request, response);
    }
}
