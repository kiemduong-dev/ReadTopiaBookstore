package controller.supplier;

import dao.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/admin/supplier/delete")
public class SupplierDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        HttpSession session = request.getSession();

        if (idRaw != null) {
            try {
                int id = Integer.parseInt(idRaw);
                SupplierDAO dao = new SupplierDAO();

                if (dao.hasImportStock(id)) {
                    session.setAttribute("errorMessage",
                        "Cannot delete this supplier because it has associated import records.");
                } else {
                    dao.deleteSupplier(id);
                    session.setAttribute("successMessage", "Supplier deleted successfully!");
                }

            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid supplier ID.");
            }
        } else {
            session.setAttribute("errorMessage", "Supplier ID is missing.");
        }

        response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
    }
}
