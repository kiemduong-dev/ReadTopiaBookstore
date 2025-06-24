package controller.supplier;

import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/supplier/edit")
public class SupplierEditServlet extends HttpServlet {

    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                SupplierDTO supplier = supplierDAO.getSupplierById(id);
                request.setAttribute("supplier", supplier);
                request.getRequestDispatcher("/WEB-INF/view/admin/supplier/edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            String image = request.getParameter("image");
            SupplierDTO supplier = new SupplierDTO(id, name, password, email, phone, address, status, image);
            supplierDAO.editSupplier(supplier);

            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update supplier.");
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/edit.jsp").forward(request, response);
        }
    }
}
