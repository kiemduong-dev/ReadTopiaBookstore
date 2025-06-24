/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
import dao.PromotionLogDAO;
import dao.StaffDAO;
import dto.AccountDTO;
import util.DBContext;
import dto.PromotionDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "PromotionAddServlet", urlPatterns = {"/admin/promotion/add"})
public class PromotionAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try ( Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);
            PromotionLogDAO logDao = new PromotionLogDAO(conn);

            PromotionDTO pro = new PromotionDTO();
            pro.setProName(request.getParameter("proName"));
            pro.setProCode(request.getParameter("proCode"));
            pro.setDiscount(Double.parseDouble(request.getParameter("discount")));
            pro.setStartDate(Date.valueOf(request.getParameter("startDate")));
            pro.setEndDate(Date.valueOf(request.getParameter("endDate")));
            pro.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            pro.setProStatus(Integer.parseInt(request.getParameter("proStatus")));

            pro.setCreatedBy(1);
            pro.setApprovedBy(1);

            int newProID = dao.addPromotionReturnID(pro);

            if (newProID > 0) {
                logDao.insertLog(newProID, 1, 1);
            }

            response.sendRedirect("list");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error adding promotion: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/promotion/add.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
