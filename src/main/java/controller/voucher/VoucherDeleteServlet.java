/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import dao.VoucherLogDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "VoucherDeleteServlet", urlPatterns = {"/admin/voucher/delete"})
public class VoucherDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("vouID");

        try {
            int vouID = Integer.parseInt(idParam);

            // Gán staffID tạm (hoặc lấy từ session)
            int staffID = 1;

            VoucherDAO dao = new VoucherDAO();
            boolean deleted = dao.deleteVoucher(vouID);

            if (deleted) {
                VoucherLogDAO logDAO = new VoucherLogDAO();
                logDAO.insertLog(vouID, idParam, 3); // 3 = xóa

                response.sendRedirect(request.getContextPath() + "/admin/voucher/list");
            } else {
                response.getWriter().println("Xóa voucher thất bại.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi khi xóa voucher: " + e.getMessage());
        }
    }
}
