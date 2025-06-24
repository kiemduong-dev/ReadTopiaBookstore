/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.PromotionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ngtua
 */
public class PromotionDAO {

    private Connection conn;

    public PromotionDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy danh sách promotion theo trang
    public List<PromotionDTO> getPromotionsWithPaging(int offset, int limit) throws SQLException {
        List<PromotionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotion ORDER BY proID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PromotionDTO p = new PromotionDTO(
                            rs.getInt("proID"),
                            rs.getString("proName"),
                            rs.getString("proCode"),
                            rs.getDouble("discount"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getInt("quantity"),
                            rs.getInt("proStatus"),
                            rs.getInt("createdBy"),
                            rs.getInt("approvedBy")
                    );
                    list.add(p);
                }
            }
        }
        return list;
    }

    public int getTotalPromotionCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Promotion";
        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Search promotions by name or code
    public List<PromotionDTO> searchPromotions(String keyword) {
        List<PromotionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotion WHERE proName LIKE ? OR proCode LIKE ?";

        // Nếu là số, thêm tìm theo ID
        boolean isNumeric = keyword.matches("\\d+");
        if (isNumeric) {
            sql += " OR proID = ?";
        }

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);

            if (isNumeric) {
                ps.setInt(3, Integer.parseInt(keyword));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PromotionDTO p = new PromotionDTO(
                        rs.getInt("proID"),
                        rs.getString("proName"),
                        rs.getString("proCode"),
                        rs.getDouble("discount"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getInt("quantity"),
                        rs.getInt("proStatus"),
                        rs.getInt("createdBy"),
                        rs.getInt("approvedBy")
                );
                list.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PromotionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    // Add promotion
    public int addPromotionReturnID(PromotionDTO pro) throws SQLException {
        String sql = "INSERT INTO Promotion (proName, proCode, discount, startDate, endDate, quantity, proStatus, createdBy, approvedBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pro.getProName());
            ps.setString(2, pro.getProCode());
            ps.setDouble(3, pro.getDiscount());
            ps.setDate(4, pro.getStartDate());
            ps.setDate(5, pro.getEndDate());
            ps.setInt(6, pro.getQuantity());
            ps.setInt(7, pro.getProStatus());
            ps.setInt(8, pro.getCreatedBy());
            ps.setInt(9, pro.getApprovedBy());

            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // proID mới được tạo
                }
            }
        }
        return -1; // lỗi
    }

    // Update promotion
    public boolean updatePromotion(PromotionDTO p) throws SQLException {
        String sql = "UPDATE Promotion SET proName=?, proCode=?, discount=?, startDate=?, endDate=?, quantity=?, proStatus=?, createdBy=?, approvedBy=? "
                + "WHERE proID=?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProName());
            ps.setString(2, p.getProCode());
            ps.setDouble(3, p.getDiscount());
            ps.setDate(4, p.getStartDate());
            ps.setDate(5, p.getEndDate());
            ps.setInt(6, p.getQuantity());
            ps.setInt(7, p.getProStatus());
            ps.setInt(8, p.getCreatedBy());
            ps.setInt(9, p.getApprovedBy());
            ps.setInt(10, p.getProID());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deletePromotion(int proID) {
        String sql = "DELETE FROM Promotion WHERE proID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proID);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(PromotionDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Get promotion by ID
    public PromotionDTO getPromotionByID(int proID) throws SQLException {
        String sql = "SELECT * FROM Promotion WHERE proID=?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PromotionDTO(
                        rs.getInt("proID"),
                        rs.getString("proName"),
                        rs.getString("proCode"),
                        rs.getDouble("discount"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getInt("quantity"),
                        rs.getInt("proStatus"),
                        rs.getInt("createdBy"),
                        rs.getInt("approvedBy")
                );
            }
        }
        return null;
    }
}
