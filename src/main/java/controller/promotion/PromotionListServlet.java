/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
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
import java.util.List;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "PromotionListServlet", urlPatterns = {"/admin/promotion/list"})
public class PromotionListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");

        try ( Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);
            List<PromotionDTO> list;

            if (keyword != null && !keyword.trim().isEmpty()) {
                list = dao.searchPromotions(keyword);
            } else {
                list = dao.getAllPromotions(); // bỏ phân trang
            }

            request.setAttribute("promotionList", list);
            request.setAttribute("search", keyword);
            request.getRequestDispatcher("/WEB-INF/view/admin/promotion/list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading promotions");
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
