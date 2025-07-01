/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.promotion;

import dao.PromotionDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import util.DBContext;

/**
 *
 * @author ngtua
 */
@WebServlet(name="PromotionApproveServlet", urlPatterns={"/admin/promotion/approve"})
public class PromotionApproveServlet extends HttpServlet {
   
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int proID = Integer.parseInt(request.getParameter("proID"));

        try (Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);

            HttpSession session = request.getSession();
            AccountDTO acc = (AccountDTO) session.getAttribute("account");
            int staffID = new StaffDAO().getStaffIDByUsername(acc.getUsername());

            dao.updatePromotionStatus(proID, 1, staffID); // 1 = approved
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("list");
    }
}
