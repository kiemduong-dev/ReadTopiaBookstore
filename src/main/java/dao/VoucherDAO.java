/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.VoucherDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBContext;

/**
 *
 * @author default
 */
public class VoucherDAO {

    private static final Logger LOGGER = Logger.getLogger(VoucherDAO.class.getName());

    public VoucherDAO() {
    }

    public List<VoucherDTO> getListVoucher(int offset, int limit) throws SQLException {
        List<VoucherDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher ORDER BY vouID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherDTO v = new VoucherDTO(
                            rs.getInt("vouID"),
                            rs.getString("vouName"),
                            rs.getString("vouCode"),
                            rs.getDouble("discount"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getInt("quantity"),
                            rs.getInt("vouStatus"),
                            rs.getString("createdBy"),
                            rs.getString("approvedBy")
                    );

                    int initial = rs.getInt("quantity");

                    int remaining = rs.getInt("newQuantity");
                    if (rs.wasNull()) {
                        // Nếu newQuantity NULL thì hiểu là chưa có người dùng, nên remaining = initial
                        remaining = initial;
                    }

                    v.setMaxQuantity(initial);
                    v.setQuantityUsed(initial - remaining);

                    list.add(v);
                }
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countVouchers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Voucher";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public List<VoucherDTO> searchAndFilterVouchers(String keyword, int status, int offset, int limit) throws SQLException {
        List<VoucherDTO> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Voucher WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (vouName LIKE ? OR vouCode LIKE ?");
            boolean isNumeric = keyword.matches("\\d+");
            if (isNumeric) {
                sql.append(" OR vouID = ?");
            }
            sql.append(")");
            String search = "%" + keyword + "%";
            params.add(search);
            params.add(search);
            if (isNumeric) {
                params.add(Integer.parseInt(keyword));
            }
        }

        if (status == 0 || status == 1) {
            sql.append(" AND vouStatus = ?");
            params.add(status);
        }

        sql.append(" ORDER BY vouID DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherDTO v = new VoucherDTO(
                            rs.getInt("vouID"),
                            rs.getString("vouName"),
                            rs.getString("vouCode"),
                            rs.getDouble("discount"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getInt("quantity"),
                            rs.getInt("vouStatus"),
                            rs.getString("createdBy"),
                            rs.getString("approvedBy")
                    );
                    list.add(v);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public int countSearchAndFilter(String keyword, int status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Voucher WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (vouName LIKE ? OR vouCode LIKE ?");
            boolean isNumeric = keyword.matches("\\d+");
            if (isNumeric) {
                sql.append(" OR vouID = ?");
            }
            sql.append(")");
            String search = "%" + keyword + "%";
            params.add(search);
            params.add(search);
            if (isNumeric) {
                params.add(Integer.parseInt(keyword));
            }
        }

        if (status == 0 || status == 1) {
            sql.append(" AND vouStatus = ?");
            params.add(status);
        }

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public int addVoucher(VoucherDTO v) throws SQLException {
        String sql = "INSERT INTO Voucher (vouName, vouCode, discount, startDate, endDate, quantity, vouStatus, createdBy, approvedBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, v.getVouName());
            ps.setString(2, v.getVouCode());
            ps.setDouble(3, v.getDiscount());
            ps.setDate(4, v.getStartDate());
            ps.setDate(5, v.getEndDate());
            ps.setInt(6, v.getQuantity());
            ps.setInt(7, v.getVouStatus());
            ps.setString(8, v.getCreatedBy());
            ps.setString(9, v.getApprovedBy());

            ps.executeUpdate();
            try ( ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public boolean updateVoucher(VoucherDTO v) throws SQLException {
        String sql = "UPDATE Voucher SET startDate = ?, endDate = ?, quantity = ? WHERE vouID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, v.getStartDate());
            ps.setDate(2, v.getEndDate());
            ps.setInt(3, v.getQuantity());
            ps.setInt(4, v.getVouID());
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteVoucher(int vouID) {
        String sql = "DELETE FROM Voucher WHERE vouID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vouID);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public VoucherDTO getVoucherByID(int vouID) throws SQLException {
        String sql = "SELECT * FROM Voucher WHERE vouID=?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vouID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new VoucherDTO(
                        rs.getInt("vouID"),
                        rs.getString("vouName"),
                        rs.getString("vouCode"),
                        rs.getDouble("discount"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getInt("quantity"),
                        rs.getInt("vouStatus"),
                        rs.getString("createdBy"),
                        rs.getString("approvedBy")
                );
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VoucherDTO getValidVoucherByCode(String code) throws SQLException {
        String sql = "SELECT * FROM Voucher WHERE vouCode = ? AND vouStatus = 1 AND GETDATE() BETWEEN startDate AND endDate AND quantity > 0";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VoucherDTO v = new VoucherDTO();
                    v.setVouID(rs.getInt("vouID"));
                    v.setVouCode(rs.getString("vouCode"));
                    v.setDiscount(rs.getDouble("discount"));
                    return v;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public VoucherDTO getVoucherWithRoles(int vouID) throws SQLException {
        String sql = "SELECT v.*, "
                + "ac1.role AS creatorRole, "
                + "ac2.role AS approverRole "
                + "FROM Voucher v "
                + "LEFT JOIN Staff s1 ON v.createdBy = s1.username "
                + "LEFT JOIN Account ac1 ON s1.username = ac1.username "
                + "LEFT JOIN Staff s2 ON v.approvedBy = s2.username "
                + "LEFT JOIN Account ac2 ON s2.username = ac2.username "
                + "WHERE v.vouID = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vouID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VoucherDTO v = new VoucherDTO();
                    v.setVouID(rs.getInt("vouID"));
                    v.setVouName(rs.getString("vouName"));
                    v.setVouCode(rs.getString("vouCode"));
                    v.setDiscount(rs.getDouble("discount"));
                    v.setStartDate(rs.getDate("startDate"));
                    v.setEndDate(rs.getDate("endDate"));
                    v.setQuantity(rs.getInt("quantity"));
                    v.setVouStatus(rs.getInt("vouStatus"));
                    v.setCreatedBy(rs.getString("createdBy"));
                    v.setApprovedBy(rs.getString("approvedBy"));
                    v.setCreatorRole(rs.getInt("creatorRole"));
                    v.setApproverRole(rs.getInt("approverRole"));
                    return v;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void updateVoucherStatus(int vouID, int status, String approvedBy) throws SQLException {
        String sql = "UPDATE Voucher SET vouStatus = ?, approvedBy = ? WHERE vouID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, approvedBy);
            ps.setInt(3, vouID);
            ps.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<VoucherDTO> filterByStatus(int status) throws SQLException {
        List<VoucherDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher";
        if (status == 0 || status == 1) {
            sql += " WHERE vouStatus = ?";
        }
        sql += " ORDER BY vouID DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            if (status == 0 || status == 1) {
                ps.setInt(1, status);
            }
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherDTO v = new VoucherDTO(
                            rs.getInt("vouID"),
                            rs.getString("vouName"),
                            rs.getString("vouCode"),
                            rs.getDouble("discount"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getInt("quantity"),
                            rs.getInt("vouStatus"),
                            rs.getString("createdBy"),
                            rs.getString("approvedBy")
                    );
                    list.add(v);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoucherDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public List<VoucherDTO> getValidVouchers() throws SQLException {
        List<VoucherDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Voucher WHERE vouStatus = 1 AND quantity > 0 AND CAST(GETDATE() AS DATE) BETWEEN startDate AND endDate ORDER BY vouID DESC";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VoucherDTO v = new VoucherDTO(
                        rs.getInt("vouID"),
                        rs.getString("vouName"),
                        rs.getString("vouCode"),
                        rs.getDouble("discount"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getInt("quantity"),
                        rs.getInt("vouStatus"),
                        rs.getString("createdBy"),
                        rs.getString("approvedBy")
                );
                list.add(v);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "getValidVouchers - DB error", e);
        }
        return list;
    }

    public VoucherDTO getValidVoucherByID(int id) throws SQLException {
        String sql = "SELECT * FROM Voucher WHERE vouID = ? AND vouStatus = 1 AND startDate <= GETDATE() AND endDate >= GETDATE()";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new VoucherDTO(
                            rs.getInt("vouID"),
                            rs.getString("vouName"),
                            rs.getString("vouCode"),
                            rs.getDouble("discount"),
                            rs.getDate("startDate"),
                            rs.getDate("endDate"),
                            rs.getInt("quantity"),
                            rs.getInt("vouStatus"),
                            rs.getString("createdBy"),
                            rs.getString("approvedBy")
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "getValidVoucherByID - DB error", e);
        }
        return null;
    }

    public void decreaseVoucherQuantity(int vouID) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Voucher SET quantity = quantity - 1 WHERE vouID = ? AND quantity > 0";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vouID);
            ps.executeUpdate();
        }
    }
}
