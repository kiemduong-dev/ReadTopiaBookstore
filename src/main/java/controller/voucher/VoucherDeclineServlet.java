/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherDAO;
import dto.AccountDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author default
 */
@WebServlet(name = "VoucherDeclineServlet", urlPatterns = {"/admin/voucher/decline"})
public class VoucherDeclineServlet extends HttpServlet {

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

            // Set status = 2 (declined), approvedBy = NULL
            dao.updateVoucherStatus(vouID, 2, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("list");
    }
}
