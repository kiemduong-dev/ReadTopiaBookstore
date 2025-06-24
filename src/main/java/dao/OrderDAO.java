/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.OrderDTO;
import dto.OrderDetailDTO;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

/**
 *
 * @author NGUYEN THAI ANH
 */
public class OrderDAO {

    // Tạo đơn hàng mới
    public int createOrder(OrderDTO order) {
        String sql = "INSERT INTO [Order] (proID, username, staffID, orderDate, orderAddress, orderStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Set proID (có thể null)
            if (order.getProID() != null) {
                ps.setInt(1, order.getProID());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, order.getUsername());
            if (order.getStaffID() != null) {
                ps.setInt(3, order.getStaffID());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            if (order.getOrderDate() != null) {
                ps.setTimestamp(4, new Timestamp(order.getOrderDate().getTime()));
            } else {
                ps.setNull(4, java.sql.Types.TIMESTAMP);
            }
            ps.setString(5, order.getOrderAddress());
            ps.setInt(6, order.getOrderStatus());

            int affected = ps.executeUpdate();

            if (affected > 0) {
                try ( ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderID = rs.getInt(1);
                        System.out.println("✅ Created order: " + orderID);
                        return orderID;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ createOrder error: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    // Cập nhật địa chỉ đơn hàng
    public boolean updateOrderAddress(OrderDTO order) {
        String sql = "UPDATE [Order] SET orderAddress = ? WHERE orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getOrderAddress());
            ps.setInt(2, order.getOrderID());

            boolean result = ps.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ Updated order address: " + order.getOrderID());
            }
            return result;

        } catch (Exception e) {
            System.err.println("❌ updateOrderAddress error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Thêm chi tiết đơn hàng
    public boolean addOrderDetail(OrderDetailDTO detail) {
        String sql = "INSERT INTO OrderDetail (orderID, bookID, quantity, totalPrice) VALUES (?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getOrderID());
            ps.setInt(2, detail.getBookID());
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getTotalPrice());

            boolean result = ps.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ Added order detail: " + detail.toString());
            }
            return result;
        } catch (Exception e) {
            System.err.println("❌ addOrderDetail error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Lấy lịch sử đơn hàng của khách hàng
    public List<OrderDTO> getOrderHistoryByUsername(String username) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.*, \n"
                + "       (SELECT SUM(totalPrice) \n"
                + "        FROM OrderDetail od \n"
                + "        WHERE od.orderID = o.orderID) AS totalAmount\n"
                + "FROM [Order] o\n"
                + "WHERE o.username = ?\n"
                + "ORDER BY o.orderDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractOrderFromResultSet(rs));
                }
            }

            System.out.println("✅ Retrieved " + list.size() + " orders for user: " + username);
        } catch (Exception e) {
            System.err.println("❌ getOrderHistoryByUsername error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Tìm đơn hàng theo bookID
    public List<OrderDTO> getOrdersByBookID(int bookID) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT DISTINCT o.*, \n"
                + "       (SELECT SUM(totalPrice) FROM OrderDetail od WHERE od.orderID = o.orderID) AS totalAmount\n"
                + "FROM [Order] o\n"
                + "JOIN OrderDetail od ON o.orderID = od.orderID\n"
                + "WHERE od.bookID = ?\n"
                + "ORDER BY o.orderDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractOrderFromResultSet(rs));
                }
            }

            System.out.println("✅ Retrieved " + list.size() + " orders for bookID: " + bookID);
        } catch (Exception e) {
            System.err.println("❌ getOrdersByBookID error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả đơn hàng (cho staff)
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.*, \n"
                + "       (SELECT SUM(totalPrice) FROM OrderDetail od WHERE od.orderID = o.orderID) AS totalAmount \n"
                + "FROM [Order] o \n"
                + "ORDER BY o.orderDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrderDTO order = extractOrderFromResultSet(rs);
                order.setTotalAmount(calculateTotalAmount(order.getOrderID()));
                list.add(order);
            }

            System.out.println("✅ Retrieved " + list.size() + " total orders");
        } catch (Exception e) {
            System.err.println("❌ getAllOrders error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Tìm đơn hàng theo ID
    public List<OrderDTO> searchOrdersByID(int orderID) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.*, \n"
                + "       (SELECT SUM(totalPrice) FROM OrderDetail od WHERE od.orderID = o.orderID) AS totalAmount\n"
                + "FROM [Order] o\n"
                + "WHERE o.orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractOrderFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ searchOrdersByID error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Tìm đơn hàng theo khoảng thời gian
    public List<OrderDTO> searchOrdersByDateRange(Date startDate, Date endDate) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT o.*, \n"
                + "       (SELECT SUM(totalPrice) FROM OrderDetail od WHERE od.orderID = o.orderID) AS totalAmount\n"
                + "FROM [Order] o\n"
                + "WHERE o.orderDate BETWEEN ? AND ?\n"
                + "ORDER BY o.orderDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, startDate);
            ps.setDate(2, endDate);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractOrderFromResultSet(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("❌ searchOrdersByDateRange error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Lấy đơn hàng theo ID
    public OrderDTO getOrderByID(int orderID) {
        String sql = "SELECT o.*, \n"
                + "       (SELECT SUM(totalPrice) FROM OrderDetail od WHERE od.orderID = o.orderID) AS totalAmount\n"
                + "FROM [Order] o\n"
                + "WHERE o.orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractOrderFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ getOrderByID error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderID, int newStatus) {
        String sql = "UPDATE [Order] SET orderStatus = ? WHERE orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, newStatus);
            ps.setInt(2, orderID);

            boolean result = ps.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ Updated order status: orderID=" + orderID + ", newStatus=" + newStatus);
            }
            return result;
        } catch (Exception e) {
            System.err.println("❌ updateOrderStatus error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Lấy chi tiết đơn hàng
    public List<OrderDetailDTO> getOrderDetails(int orderID) {
        List<OrderDetailDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderDetail WHERE orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetailDTO detail = new OrderDetailDTO();
                    detail.setODID(rs.getInt("ODID"));
                    detail.setOrderID(rs.getInt("orderID"));
                    detail.setBookID(rs.getInt("bookID"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setTotalPrice(rs.getDouble("totalPrice"));
                    list.add(detail);
                }
            }

            System.out.println("✅ Retrieved " + list.size() + " order details for orderID: " + orderID);
        } catch (Exception e) {
            System.err.println("❌ getOrderDetails error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Helper method để extract OrderDTO từ ResultSet
    private OrderDTO extractOrderFromResultSet(ResultSet rs) throws Exception {
        OrderDTO order = new OrderDTO();
        order.setOrderID(rs.getInt("orderID"));
        order.setProID(rs.getInt("proID"));
        order.setUsername(rs.getString("username"));
        order.setStaffID(rs.getInt("staffID"));
        order.setOrderDate(rs.getTimestamp("orderDate"));
        order.setOrderAddress(rs.getString("orderAddress"));
        order.setOrderStatus(rs.getInt("orderStatus"));
        order.setTotalAmount(rs.getDouble("totalAmount"));
        return order;
    }

    public double calculateTotalAmount(int orderID) {
        double total = 0;
        String sql = "SELECT SUM(totalPrice) FROM OrderDetail WHERE orderID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

}
