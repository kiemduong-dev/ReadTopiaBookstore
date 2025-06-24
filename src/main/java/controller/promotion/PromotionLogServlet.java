/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.promotion;

import dao.PromotionLogDAO;
import util.DBContext;
import dto.PromotionLogDTO;
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
@WebServlet(name="PromotionLogServlet", urlPatterns={"/admin/promotion/logs"})
public class PromotionLogServlet extends HttpServlet {
   
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String actionParam = request.getParameter("action");
    Integer actionFilter = null;

    // Chuyển đổi action filter nếu có
    if (actionParam != null && !actionParam.isEmpty()) {
        try {
            actionFilter = Integer.parseInt(actionParam);
        } catch (NumberFormatException e) {
            // Nếu không hợp lệ, vẫn giữ actionFilter = null
        }
    }

    // Kết nối cơ sở dữ liệu và lấy danh sách log
    try (Connection conn = new DBContext().getConnection()) {
        PromotionLogDAO dao = new PromotionLogDAO(conn);
        List<PromotionLogDTO> logs = dao.getAllLogs(actionFilter);

        // Gửi dữ liệu sang JSP
        request.setAttribute("logs", logs);
        request.setAttribute("actionFilter", actionFilter);
        request.getRequestDispatcher("/WEB-INF/view/admin/promotion/logs.jsp").forward(request, response);

    } catch (Exception e) {
        e.printStackTrace();
        // Nếu xảy ra lỗi, trả mã lỗi 500
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection or query failed.");
    }
}
    
    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
