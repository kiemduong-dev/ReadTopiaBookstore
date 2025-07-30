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
import util.DBContext;

/**
 *
 * @author ngtua
 */
public class PromotionDAO {

    private Connection conn;

    public PromotionDAO(Connection conn) {
        this.conn = conn;
    }

    public PromotionDAO() {
        try {
            this.conn = DBContext.getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // hoặc log ra lỗi
        }
    }

    // Lấy danh sách promotion theo trang
    public List<PromotionDTO> getAllPromotions() throws SQLException {
        List<PromotionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotion ORDER BY proID DESC"; // Không dùng OFFSET/FETCH

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

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

        return list;
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
            ps.setObject(9, pro.getApprovedBy(), Types.INTEGER); // hỗ trợ null

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
        String sql = "UPDATE Promotion SET startDate = ?, endDate = ?, quantity = ? WHERE proID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, p.getStartDate());
            ps.setDate(2, p.getEndDate());
            ps.setInt(3, p.getQuantity());
            ps.setInt(4, p.getProID());
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
        try (
                 Connection conn = DBContext.getConnection(); 
                  PreparedStatement ps = conn.prepareStatement(sql)) {
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
        } catch (Exception e) {
            e.printStackTrace(); // THÊM ĐỂ DEBUG
        }
        return null;
    }

    public PromotionDTO getValidPromotionByCode(String code) throws Exception {
        String sql = "SELECT * FROM Promotion WHERE proCode = ? AND proStatus = 1 AND GETDATE() BETWEEN startDate AND endDate AND quantity > 0";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PromotionDTO promo = new PromotionDTO();
                    promo.setProID(rs.getInt("proID"));
                    promo.setProCode(rs.getString("proCode"));
                    promo.setDiscount(rs.getInt("discount"));
                    // bạn có thể thêm các field khác nếu cần
                    return promo;
                }
            }
        }
        return null;
    }

    public PromotionDTO getPromotionWithRoles(int proID) throws SQLException {
        String sql
                = "SELECT p.*, "
                + "       ac1.role AS creatorRole, "
                + "       ac2.role AS approverRole "
                + "FROM Promotion p "
                + "LEFT JOIN Staff s1 ON p.createdBy = s1.staffID "
                + "LEFT JOIN Account ac1 ON s1.username = ac1.username "
                + "LEFT JOIN Staff s2 ON p.approvedBy = s2.staffID "
                + "LEFT JOIN Account ac2 ON s2.username = ac2.username "
                + "WHERE p.proID = ?";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PromotionDTO pro = new PromotionDTO();
                    pro.setProID(rs.getInt("proID"));
                    pro.setProName(rs.getString("proName"));
                    pro.setProCode(rs.getString("proCode"));
                    pro.setDiscount(rs.getDouble("discount"));
                    pro.setStartDate(rs.getDate("startDate"));
                    pro.setEndDate(rs.getDate("endDate"));
                    pro.setQuantity(rs.getInt("quantity"));
                    pro.setProStatus(rs.getInt("proStatus"));
                    pro.setCreatedBy(rs.getInt("createdBy"));
                    pro.setApprovedBy(rs.getInt("approvedBy"));

                    pro.setCreatorRole(rs.getInt("creatorRole"));
                    pro.setApproverRole(rs.getInt("approverRole"));
                    System.out.println("promotion createdBy = " + pro.getCreatedBy());
                    System.out.println("promotion creatorRole = " + pro.getCreatorRole());

                    return pro;
                }
            }
        }

        return null;
    }

    public void updatePromotionStatus(int proID, int status, int approvedBy) throws SQLException {
        String sql = "UPDATE Promotion SET proStatus = ?, approvedBy = ? WHERE proID = ?";
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, approvedBy);
            ps.setInt(3, proID);
            ps.executeUpdate();
        }
    }

    // Filter promotions by status: -1 (all), 1 (active), 0 (inactive)
    public List<PromotionDTO> filterByStatus(int status) throws SQLException {
        List<PromotionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotion";
        if (status == 0 || status == 1) {
            sql += " WHERE proStatus = ?";
        }
        sql += " ORDER BY proID DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status == 0 || status == 1) {
                ps.setInt(1, status);
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
        }
        return list;
    }

    // PromotionDAO.java
    public List<PromotionDTO> getValidPromotions() throws SQLException {
        List<PromotionDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Promotion \n"
                + "WHERE proStatus = 1 \n"
                + "AND quantity > 0 \n"
                + "AND CAST(GETDATE() AS DATE) BETWEEN startDate AND endDate\n"
                + "ORDER BY proID DESC";

        try ( PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

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
        return list;
    }

    public PromotionDTO getValidPromotionByID(int id) throws Exception {
        String sql = "SELECT * FROM Promotion WHERE proID = ? AND proStatus = 1 AND startDate <= GETDATE() AND endDate >= GETDATE()";

        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();

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
        return null;
    }

}
