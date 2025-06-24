/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
import dao.PromotionLogDAO;
import util.DBContext;
import dto.PromotionDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Date;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "PromotionEditServlet", urlPatterns = {"/admin/promotion/edit"})
public class PromotionEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int proID = Integer.parseInt(request.getParameter("proID"));

        try ( Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);
            PromotionDTO pro = dao.getPromotionByID(proID);
            request.setAttribute("promotion", pro);
            request.getRequestDispatcher("/WEB-INF/view/admin/promotion/edit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading promotion for edit");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try ( Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);
            PromotionLogDAO logDao = new PromotionLogDAO(conn);

            PromotionDTO pro = new PromotionDTO();
            pro.setProID(Integer.parseInt(request.getParameter("proID")));
            pro.setProName(request.getParameter("proName"));
            pro.setProCode(request.getParameter("proCode"));
            pro.setDiscount(Double.parseDouble(request.getParameter("discount")));
            pro.setStartDate(Date.valueOf(request.getParameter("startDate")));
            pro.setEndDate(Date.valueOf(request.getParameter("endDate")));
            pro.setQuantity(Integer.parseInt(request.getParameter("quantity")));
            pro.setProStatus(Integer.parseInt(request.getParameter("proStatus")));
            pro.setCreatedBy(Integer.parseInt(request.getParameter("createdBy")));
            pro.setApprovedBy(Integer.parseInt(request.getParameter("approvedBy")));

            dao.updatePromotion(pro);

            int staffID = pro.getCreatedBy();
            logDao.insertLog(pro.getProID(), staffID, 2);

            response.sendRedirect("list");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error updating promotion");
        }
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
