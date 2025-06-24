/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
import dto.PromotionDTO;
import java.io.IOException;
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
@WebServlet(name = "PromotionDetailServlet", urlPatterns = {"/admin/promotion/detail"})
public class PromotionDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String proIDParam = request.getParameter("proID");
        if (proIDParam == null || proIDParam.isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try ( Connection conn = new DBContext().getConnection()) {
            int proID = Integer.parseInt(proIDParam);
            PromotionDAO dao = new PromotionDAO(conn);
            PromotionDTO promotion = dao.getPromotionByID(proID);

            if (promotion != null) {
                request.setAttribute("promotion", promotion);
                request.getRequestDispatcher("/WEB-INF/view/admin/promotion/detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading promotion detail");
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
