/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.promotion;

import dao.PromotionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import util.DBContext;

/**
 *
 * @author ngtua
 */
@WebServlet(name="PromotionDeclineServlet", urlPatterns={"/admin/promotion/decline"})
public class PromotionDeclineServlet extends HttpServlet {
   
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int proID = Integer.parseInt(request.getParameter("proID"));

        try (Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);
            dao.updatePromotionStatus(proID, 2, 0); // 2 = declined, no approver
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("list");
    }
}
