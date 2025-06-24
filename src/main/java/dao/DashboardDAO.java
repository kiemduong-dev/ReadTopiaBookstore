/**
 * DashboardDAO - Provides data access methods for retrieving analytics and statistics
 * to be displayed on the admin dashboard, including revenue, orders, users, and top books.
 *
 * @author CE181518 Dương An Kiếm
 */
package dao;

import dto.BookSoldDTO;
import util.DBContext;

import java.sql.*;
import java.util.*;

public class DashboardDAO {

    /**
     * Establishes a database connection.
     *
     * @return the database connection
     * @throws Exception if a connection cannot be established
     */
    private Connection getConnection() throws Exception {
        return new DBContext().getConnection();
    }

    /**
     * Retrieves total revenue for the current month.
     *
     * @return total revenue in integer
     * @throws Exception if a database error occurs
     */
    public int getTotalRevenueThisMonth() throws Exception {
        String sql = "SELECT ISNULL(SUM(od.totalPrice), 0) " +
                     "FROM OrderDetail od " +
                     "JOIN [Order] o ON od.orderID = o.orderID " +
                     "WHERE MONTH(o.orderDate) = MONTH(GETDATE()) AND YEAR(o.orderDate) = YEAR(GETDATE())";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves total number of orders for the current month.
     *
     * @return number of orders
     * @throws Exception if a database error occurs
     */
    public int getTotalOrdersThisMonth() throws Exception {
        String sql = "SELECT COUNT(*) FROM [Order] " +
                     "WHERE MONTH(orderDate) = MONTH(GETDATE()) AND YEAR(orderDate) = YEAR(GETDATE())";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves the total number of users with role = 1 (customers).
     *
     * @return total number of customers
     * @throws Exception if a database error occurs
     */
    public int getTotalUsers() throws Exception {
        String sql = "SELECT COUNT(*) FROM Account WHERE role = 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves total number of books.
     *
     * @return number of books
     * @throws Exception if a database error occurs
     */
    public int getTotalBooks() throws Exception {
        String sql = "SELECT COUNT(*) FROM Book";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    /**
     * Retrieves revenue data for the last 7 days.
     *
     * @return map containing date as key and revenue as value
     * @throws Exception if a database error occurs
     */
    public Map<String, Integer> getRevenueLast7Days() throws Exception {
        String sql = "SELECT CONVERT(varchar, o.orderDate, 23) AS day, ISNULL(SUM(od.totalPrice), 0) AS revenue " +
                     "FROM [Order] o " +
                     "LEFT JOIN OrderDetail od ON o.orderID = od.orderID " +
                     "WHERE o.orderDate >= DATEADD(day, -6, CAST(GETDATE() AS date)) " +
                     "GROUP BY CONVERT(varchar, o.orderDate, 23) " +
                     "ORDER BY day ASC";

        Map<String, Integer> result = new LinkedHashMap<>();

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -6);
            List<String> last7Days = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
                last7Days.add(date.toString());
                cal.add(Calendar.DATE, 1);
            }

            Map<String, Integer> dbData = new HashMap<>();
            while (rs.next()) {
                dbData.put(rs.getString("day"), rs.getInt("revenue"));
            }

            for (String day : last7Days) {
                result.put(day, dbData.getOrDefault(day, 0));
            }
        }
        return result;
    }

    /**
     * Retrieves top 5 best-selling books based on quantity sold.
     *
     * @return list of BookSoldDTO objects
     * @throws Exception if a database error occurs
     */
    public List<BookSoldDTO> getTopBooksSold() throws Exception {
        String sql = "SELECT TOP 5 b.bookTitle, b.author, b.publisher, SUM(od.quantity) AS sold, b.bookPrice " +
                     "FROM Book b " +
                     "JOIN OrderDetail od ON b.bookID = od.bookID " +
                     "GROUP BY b.bookTitle, b.author, b.publisher, b.bookPrice " +
                     "ORDER BY sold DESC";

        List<BookSoldDTO> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BookSoldDTO dto = new BookSoldDTO();
                dto.setTitle(rs.getString("bookTitle"));
                dto.setAuthor(rs.getString("author"));
                dto.setPublisher(rs.getString("publisher"));
                dto.setSold(rs.getInt("sold"));
                dto.setPrice(rs.getInt("bookPrice"));
                list.add(dto);
            }
        }
        return list;
    }
}
