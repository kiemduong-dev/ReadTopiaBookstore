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

/**
 * DashboardServlet
 *
 * Handles admin dashboard data aggregation and forwarding to the view. Gathers
 * KPIs like total revenue, orders, users, books, revenue trends, and top
 * selling books.
 *
 * URL mapping: /admin/dashboard
 *
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    /**
     * Handles HTTP GET request to display dashboard data.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve dashboard metrics from database
            int totalRevenue = dashboardDAO.getTotalRevenueThisMonth();
            int totalOrders = dashboardDAO.getTotalOrdersThisMonth();
            int totalUsers = dashboardDAO.getTotalUsers();
            int totalBooks = dashboardDAO.getTotalBooks();
            Map<String, Integer> revenueMap = dashboardDAO.getRevenueLast7Days();
            List<BookSoldDTO> topBooks = dashboardDAO.getTopBooksSold();

            // Set data to request scope for JSP rendering
            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("revenueLabels", new ArrayList<>(revenueMap.keySet()));
            request.setAttribute("revenueData", new ArrayList<>(revenueMap.values()));
            request.setAttribute("topBooks", topBooks);

            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
