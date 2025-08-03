/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import dto.VoucherDTO;
import java.io.IOException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBContext;

/**
 *
 * @author default
 */
@WebServlet(name = "VoucherDetailServlet", urlPatterns = {"/admin/voucher/detail"})
public class VoucherDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vouIDParam = request.getParameter("vouID");
        if (vouIDParam == null || vouIDParam.isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int vouID = Integer.parseInt(vouIDParam);

            try ( Connection conn = new DBContext().getConnection()) {
                VoucherDAO dao = new VoucherDAO();
                VoucherDTO voucher = dao.getVoucherWithRoles(vouID); // method cần tồn tại trong DAO

                if (voucher == null) {
                    request.setAttribute("error", "Voucher not found.");
                    request.getRequestDispatcher("/WEB-INF/view/admin/voucher/detail.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("voucher", voucher);
                request.getRequestDispatcher("/WEB-INF/view/admin/voucher/detail.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid voucher ID.");
            request.getRequestDispatcher("/WEB-INF/view/admin/voucher/detail.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error retrieving voucher detail", ex);
        }
    }
}
