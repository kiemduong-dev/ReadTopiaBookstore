/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.NotificationDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBContext;

public class NotificationDAO {

    public void addNotification(NotificationDTO noti) throws SQLException {
        String sql = "INSERT INTO Notification (staffID, notTitle, receiver, notDescription, notCreateDay) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, noti.getStaffID());
            ps.setString(2, noti.getNotTitle());
            ps.setInt(3, noti.getReceiver());
            ps.setString(4, noti.getNotDescription());
            ps.setTimestamp(5, noti.getNotCreatedDate()); // ✅ đã là Timestamp
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<NotificationDTO> getAllNotifications(int offset, int limit) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification ORDER BY notID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
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
                    noti.setNotCreatedDate(rs.getTimestamp("notCreateDay")); // ✅
                    list.add(noti);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countNotifications() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notification";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void deleteNotification(int id) throws SQLException {
        String sql = "DELETE FROM Notification WHERE notID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<NotificationDTO> searchByTitle(String keyword) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE notTitle LIKE ? ORDER BY notID DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotCreatedDate(rs.getTimestamp("notCreateDay")); // ✅
                    list.add(noti);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public NotificationDTO getNotificationById(int id) throws SQLException {
        String sql = "SELECT * FROM Notification WHERE notID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotCreatedDate(rs.getTimestamp("notCreateDay")); // ✅
                    return noti;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<NotificationDTO> getNotificationsForRole(int role) throws SQLException {
        List<NotificationDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE receiver = ? OR receiver = 4 ORDER BY notID DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, role);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotificationDTO noti = new NotificationDTO();
                    noti.setNotID(rs.getInt("notID"));
                    noti.setStaffID(rs.getInt("staffID"));
                    noti.setNotTitle(rs.getString("notTitle"));
                    noti.setReceiver(rs.getInt("receiver"));
                    noti.setNotDescription(rs.getString("notDescription"));
                    noti.setNotCreatedDate(rs.getTimestamp("notCreateDay")); // ✅
                    list.add(noti);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NotificationDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
