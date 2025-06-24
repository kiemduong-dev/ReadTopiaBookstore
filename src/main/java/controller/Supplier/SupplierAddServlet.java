package controller.supplier;

import dao.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/supplier/add")
public class SupplierAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/supplier/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String statusStr = request.getParameter("status");
        String image = request.getParameter("image");

        boolean status = Boolean.parseBoolean(statusStr);

        SupplierDAO dao = new SupplierDAO();
        dao.addSupplier(name, password, email , phone, address, status, image);

        // Chuyển hướng về danh sách nhà cung cấp
        response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
    }
}
