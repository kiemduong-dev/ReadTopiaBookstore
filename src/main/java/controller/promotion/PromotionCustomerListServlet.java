/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.promotion;

import dao.PromotionDAO;
import dto.PromotionDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import util.DBContext;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "PromotionCustomerListServlet", urlPatterns = {"/customer/promotion/list"})
public class PromotionCustomerListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try ( Connection conn = new DBContext().getConnection()) {
            PromotionDAO dao = new PromotionDAO(conn);

            // Lấy danh sách promotion đã duyệt (proStatus = 1)
            List<PromotionDTO> list = dao.searchPromotions(""); // hoặc viết DAO riêng để lọc proStatus = 1
            List<PromotionDTO> approvedList = new ArrayList<>();
            for (PromotionDTO p : list) {
                if (p.getProStatus() == 1) {
                    approvedList.add(p);
                }
            }

            request.setAttribute("promotionList", approvedList);
            request.getRequestDispatcher("/WEB-INF/view/customer/promotion/promotionList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Lỗi khi tải danh sách khuyến mãi: " + e.getMessage());
        }
    }
}
