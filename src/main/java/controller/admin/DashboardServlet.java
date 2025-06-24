package controller.admin;

import dao.DashboardDAO;
import dto.BookSoldDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ DB
            int totalRevenue = dashboardDAO.getTotalRevenueThisMonth();
            int totalOrders = dashboardDAO.getTotalOrdersThisMonth();
            int totalUsers = dashboardDAO.getTotalUsers();
            int totalBooks = dashboardDAO.getTotalBooks();
            Map<String, Integer> revenueMap = dashboardDAO.getRevenueLast7Days();
            List<BookSoldDTO> topBooks = dashboardDAO.getTopBooksSold();

            // Đưa dữ liệu vào request scope để JSP dùng
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("revenueLabels", new ArrayList<>(revenueMap.keySet()));
            request.setAttribute("revenueData", new ArrayList<>(revenueMap.values()));
            request.setAttribute("topBooks", topBooks);

            // Chuyển tiếp đến trang Dashboard JSP
            request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            // Gửi mã lỗi 500 kèm thông báo chi tiết hơn cho dev dễ debug
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
