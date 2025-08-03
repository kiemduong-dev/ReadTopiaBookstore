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

        if (idRaw != null) {
            try {
                int id = Integer.parseInt(idRaw);
                SupplierDAO dao = new SupplierDAO();
                dao.deleteSupplier(id);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID: " + e.getMessage());
            }
        }
  HttpSession session = request.getSession();
            session.setAttribute("successMessage", " Supplier Delete successfully!");
        response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
    }
}
