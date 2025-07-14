package controller.admin;

import dao.DashboardDAO;
import dto.BookSoldDTO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * DashboardServlet – Handles admin dashboard data aggregation and forwarding.
 * Gathers KPIs like total revenue, orders, users, books, revenue trends, and
 * top selling books. URL mapping: /admin/dashboard
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final DashboardDAO dashboardDAO = new DashboardDAO();

    /**
     * Handles HTTP GET request to display dashboard data. Allows role = 0, 1,
     * 2, 3
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("account") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        AccountDTO account = (AccountDTO) session.getAttribute("account");
        int role = account.getRole();

        Set<Integer> allowedRoles = new HashSet<>(Arrays.asList(0, 1, 2, 3));

        if (!allowedRoles.contains(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
            return;
        }

        try {
            int totalRevenue = dashboardDAO.getTotalRevenueThisMonth();
            int totalOrders = dashboardDAO.getTotalOrdersThisMonth();
            int totalUsers = dashboardDAO.getTotalUsers();
            int totalBooks = dashboardDAO.getTotalBooks();

            Map<String, Integer> revenueMap = dashboardDAO.getRevenueLast7Days();
            Map<String, Integer> importStockMap = dashboardDAO.getImportStockLast7Days();
            List<BookSoldDTO> topBooks = dashboardDAO.getTopBooksSold();

            request.setAttribute("totalRevenue", totalRevenue);
            request.setAttribute("totalOrders", totalOrders);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("revenueLabels", new ArrayList<>(revenueMap.keySet()));
            request.setAttribute("revenueData", new ArrayList<>(revenueMap.values()));
            request.setAttribute("importStockData", new ArrayList<>(importStockMap.values()));
            request.setAttribute("topBooks", topBooks);

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
