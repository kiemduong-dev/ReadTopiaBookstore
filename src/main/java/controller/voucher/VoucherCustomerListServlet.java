/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import dto.VoucherDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author default
 */
@WebServlet(name = "VoucherCustomerListServlet", urlPatterns = {"/customer/voucher/list"})
public class VoucherCustomerListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try  {
            VoucherDAO dao = new VoucherDAO();

            // Lấy danh sách voucher đã duyệt (status = 1)
            List<VoucherDTO> list = dao.getListVoucher(0, 5); // hoặc tạo method riêng getApprovedVouchers()
            List<VoucherDTO> approvedList = new ArrayList<>();

            for (VoucherDTO v : list) {
                if (v.getVouStatus() == 1) {
                    approvedList.add(v);
                }
            }

            request.setAttribute("voucherList", approvedList);
            request.getRequestDispatcher("/WEB-INF/view/customer/voucher/voucherList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Lỗi khi tải danh sách voucher: " + e.getMessage());
        }
    }
}
