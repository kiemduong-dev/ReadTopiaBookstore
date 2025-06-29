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

    public List<NotificationDTO> getAllNotifications() throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification ORDER BY notID DESC";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
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
        return list;
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

    public List<NotificationDTO> getNotificationsForRole(int role) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE receiver = ? OR receiver = 4 ORDER BY notID DESC";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, role); // Đảm bảo role là 0,1,2,3 đúng như bạn định nghĩa
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

}
