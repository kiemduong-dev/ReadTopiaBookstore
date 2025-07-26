

package controller.supplier;

import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/**
 * Display detail of a specific supplier by ID.
 */
@WebServlet("/admin/supplier/detail")
public class SupplierDetailServlet extends HttpServlet {

    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    /**
     * Handles the GET request to view supplier details.
     * @param request  HttpServletRequest from JSP
     * @param response HttpServletResponse to client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
                return;
            }

            int id = Integer.parseInt(idParam);
            SupplierDTO supplier = supplierDAO.getSupplierById(id);

            if (supplier == null) {
                request.setAttribute("errorMessage", "Supplier not found!");
                request.getRequestDispatcher("/WEB-INF/view/admin/supplier/detail.jsp").forward(request, response);
                return;
            }

            request.setAttribute("supplier", supplier);
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid supplier ID.");
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        }
    }
}
