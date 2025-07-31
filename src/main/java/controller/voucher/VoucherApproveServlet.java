/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.StaffDAO;
import dao.VoucherDAO;
import dto.AccountDTO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *
 * @author default
 */
@WebServlet(name = "VoucherApproveServlet", urlPatterns = {"/admin/voucher/approve"})
public class VoucherApproveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int vouID = Integer.parseInt(request.getParameter("vouID"));

        try {
            VoucherDAO dao = new VoucherDAO();

            HttpSession session = request.getSession();
            AccountDTO acc = (AccountDTO) session.getAttribute("account");

            if (acc == null || (acc.getRole() != 0 && acc.getRole() != 2)) {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
                return;
            }

            String approvedByUsername = acc.getUsername(); // lấy username duyệt

            // Trạng thái = 1 (đã duyệt), approvedBy = username
            dao.updateVoucherStatus(vouID, 1, approvedByUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("list");
    }
}
