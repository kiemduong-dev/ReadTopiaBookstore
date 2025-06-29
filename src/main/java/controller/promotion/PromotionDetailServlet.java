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

        try {
            int proID = Integer.parseInt(proIDParam);

            try ( Connection conn = new DBContext().getConnection()) {
                PromotionDAO dao = new PromotionDAO(conn);
                PromotionDTO promotion = dao.getPromotionWithRoles(proID);

                if (promotion == null) {
                    request.setAttribute("error", "Promotion not found.");
                    request.getRequestDispatcher("/WEB-INF/view/admin/promotion/detail.jsp").forward(request, response);
                    return;
                }

                request.setAttribute("promotion", promotion);
                request.getRequestDispatcher("/WEB-INF/view/admin/promotion/detail.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid promotion ID.");
            request.getRequestDispatcher("/WEB-INF/view/admin/promotion/detail.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException("Error retrieving promotion detail", ex);
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
