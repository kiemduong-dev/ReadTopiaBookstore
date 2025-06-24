/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
import dao.PromotionLogDAO;
import util.DBContext;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "PromotionDeleteServlet", urlPatterns = {"/admin/promotion/delete"})
public class PromotionDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("proID");

        try {
            int proID = Integer.parseInt(idParam);

            // Gán tạm staffID
            int staffID = 1;

            // Kết nối DB
            try (Connection conn = new DBContext().getConnection()) {
                PromotionDAO dao = new PromotionDAO(conn);
                boolean deleted = dao.deletePromotion(proID);

                if (deleted) {
                    // Ghi log xóa
                    PromotionLogDAO logDAO = new PromotionLogDAO(conn);
                    logDAO.insertLog(proID, staffID, 3); // 3 = xóa

                    response.sendRedirect(request.getContextPath() + "/admin/promotion/list");
                } else {
                    response.getWriter().println("Xóa thất bại.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Lỗi khi xóa khuyến mãi: " + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Xử lý yêu cầu xóa khuyến mãi và ghi log hành động.";
    }
}
