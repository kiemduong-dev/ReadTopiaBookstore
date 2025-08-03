/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import dto.VoucherDTO;
import java.io.IOException;
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
@WebServlet(name = "VoucherListServlet", urlPatterns = {"/admin/voucher/list"})
public class VoucherListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");
        String statusParam = request.getParameter("status");
        int status = -1;
        int page = 1;
        int pageSize = 5;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (statusParam != null && (statusParam.equals("0") || statusParam.equals("1"))) {
            status = Integer.parseInt(statusParam);
        }

        try {
            VoucherDAO dao = new VoucherDAO();

            int offset = (page - 1) * pageSize;
            List<VoucherDTO> list = dao.searchAndFilterVouchers(keyword, status, offset, pageSize);
            int totalVouchers = dao.countSearchAndFilter(keyword, status);
            int totalPages = (int) Math.ceil((double) totalVouchers / pageSize);

            request.setAttribute("voucherList", list);
            request.setAttribute("search", keyword);
            request.setAttribute("status", status);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            request.getRequestDispatcher("/WEB-INF/view/admin/voucher/list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading vouchers");
        }
    }
}
