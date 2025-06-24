/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import util.DBContext;
import dto.NotificationDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private Connection conn;

    public NotificationDAO(Connection conn) {
        this.conn = conn;
    }

    public void addNotification(NotificationDTO noti) throws SQLException {
        String sql = "INSERT INTO Notification (staffID, notTitle, receiver, notDescription, notStatus) VALUES (?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noti.getStaffID());
            ps.setString(2, noti.getNotTitle());
            ps.setInt(3, noti.getReceiver());
            ps.setString(4, noti.getNotDescription());
            ps.setInt(5, noti.getNotStatus());
            ps.executeUpdate();
        }
    }

// Lấy thông báo có phân trang
    public List<NotificationDTO> getNotificationsWithPaging(int offset, int limit) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification ORDER BY notID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotStatus(rs.getInt("notStatus"));
                    list.add(noti);
                }
            }
        }
        return list;
    }

    public int getTotalNotificationCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notification";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public void deleteNotification(int id) throws SQLException {
        String sql = "DELETE FROM Notification WHERE notID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<NotificationDTO> searchByTitle(String keyword) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE notTitle LIKE ? ORDER BY notID DESC";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotStatus(rs.getInt("notStatus"));
                    list.add(noti);
                }
            }
        }
        return list;
    }

    public NotificationDTO getNotificationById(int id) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE notID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotStatus(rs.getInt("notStatus"));
                    return noti;
                }
            }
        }
        return null;
    }
}
