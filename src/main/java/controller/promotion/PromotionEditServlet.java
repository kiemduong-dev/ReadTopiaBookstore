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

            int proID = Integer.parseInt(request.getParameter("proID"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int createdBy = Integer.parseInt(request.getParameter("createdBy")); // hoặc từ session

            // Kiểm tra ngày
            Date currentDate = new Date(System.currentTimeMillis());

            if (startDate.before(currentDate) || endDate.before(currentDate)) {
                request.setAttribute("error", "Start date and end date must be today or in the future.");
                request.setAttribute("promotion", new PromotionDTO(proID, null, null, 0.0, startDate, endDate, quantity, 0, createdBy, 0));
                request.getRequestDispatcher("/WEB-INF/view/admin/promotion/edit.jsp").forward(request, response);
                return;
            }

            PromotionDTO pro = new PromotionDTO();
            pro.setProID(proID);
            pro.setStartDate(startDate);
            pro.setEndDate(endDate);
            pro.setQuantity(quantity);

            dao.updatePromotion(pro);

            logDao.insertLog(proID, createdBy, 2); // 2 = edit

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
