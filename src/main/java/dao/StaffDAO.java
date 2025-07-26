package dao;

import dto.StaffDTO;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffDAO – Data Access Object for staff operations. Handles CRUD operations
 * and filters based on roles 1–3.
 * 
 * Roles allowed: 1 (Staff), 2 (Seller Staff), 3 (Warehouse Staff)
 * Only active accounts (accStatus = 1) are used.
 * 
 * @author
 */
public class StaffDAO {

    /**
     * Get all active staff with role 1, 2, 3
     * @return list of StaffDTO
     */
    public List<StaffDTO> findAllActiveStaff() {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s "
                   + "JOIN Account a ON s.username = a.username "
                   + "WHERE a.accStatus = 1 AND a.role IN (1, 2, 3)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractStaff(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
/**
 * Retrieve all active staff with role = 2 (Seller Staff) or 3 (Warehouse Staff).
 *
 * @return list of StaffDTO matching role 2 or 3
 */
public List<StaffDTO> findActiveSellerAndWarehouseStaff() {
    List<StaffDTO> list = new ArrayList<>();
    String sql = "SELECT s.staffID, a.* FROM Staff s "
               + "JOIN Account a ON s.username = a.username "
               + "WHERE a.accStatus = 1 AND (a.role = 2 OR a.role = 3)";
    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(extractStaff(rs));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    /**
     * Get all active Seller and Warehouse staff (role 2, 3)
     * @return list of StaffDTO
     */
    public List<StaffDTO> getAllActiveSellerAndWarehouseStaff() {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s "
                   + "JOIN Account a ON s.username = a.username "
                   + "WHERE a.accStatus = 1 AND a.role IN (2, 3)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractStaff(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Search staff by keyword – username, firstName, lastName, email
     * Only return roles 1, 2, 3 with accStatus = 1
     * @param keyword search string
     * @return list of matched staff
     */
    public List<StaffDTO> searchStaffs(String keyword) {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s "
                   + "JOIN Account a ON s.username = a.username "
                   + "WHERE a.accStatus = 1 AND a.role IN (1, 2, 3) "
                   + "AND (a.username LIKE ? OR a.firstName LIKE ? OR a.lastName LIKE ? OR a.email LIKE ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 4; i++) {
                ps.setString(i, "%" + keyword + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractStaff(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Wrapper method – used by Staff Manager
     * Same logic as searchStaffs (only role 1, 2, 3)
     * @param keyword search keyword
     * @return list of staff
     */
    public List<StaffDTO> searchStaffsByStaffRole(String keyword) {
        return searchStaffs(keyword);
    }

    /**
     * Get staff by ID
     * @param id staffID
     * @return StaffDTO or null
     */
    public StaffDTO getStaffByID(int id) {
        String sql = "SELECT s.staffID, a.* FROM Staff s "
                   + "JOIN Account a ON s.username = a.username "
                   + "WHERE s.staffID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add new staff (insert into Account + Staff)
     * @param s StaffDTO
     * @return true if success
     */
    public boolean addStaff(StaffDTO s) {
        String sqlAccount = "INSERT INTO Account(username, password, firstName, lastName, dob, email, phone, address, sex, role, accStatus) "
                          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlStaff = "INSERT INTO Staff(username) VALUES (?)";

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlAccount);
                 PreparedStatement ps2 = conn.prepareStatement(sqlStaff)) {

                ps1.setString(1, s.getUsername());
                ps1.setString(2, s.getPassword());
                ps1.setString(3, s.getFirstName());
                ps1.setString(4, s.getLastName());
                ps1.setDate(5, s.getDob());
                ps1.setString(6, s.getEmail());
                ps1.setString(7, s.getPhone());
                ps1.setString(8, s.getAddress());
                ps1.setInt(9, s.getSex());
                ps1.setInt(10, s.getRole());
                ps1.setInt(11, 1); // accStatus = 1

                ps2.setString(1, s.getUsername());

                ps1.executeUpdate();
                ps2.executeUpdate();
                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update staff info
     * @param s StaffDTO
     * @return true if updated
     */
    public boolean updateStaff(StaffDTO s) {
        String sql = "UPDATE Account SET firstName = ?, lastName = ?, dob = ?, email = ?, phone = ?, address = ?, sex = ?, role = ? "
                   + "WHERE username = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getFirstName());
            ps.setString(2, s.getLastName());
            ps.setDate(3, s.getDob());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getAddress());
            ps.setInt(7, s.getSex());
            ps.setInt(8, s.getRole());
            ps.setString(9, s.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete staff by staffID
     * @param staffID id
     * @return true if updated
     */
    public boolean deleteStaffByID(int staffID) {
        String getUsernameSql = "SELECT username FROM Staff WHERE staffID = ?";
        String updateSql = "UPDATE Account SET accStatus = 0 WHERE username = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(getUsernameSql)) {

            ps1.setInt(1, staffID);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    try (PreparedStatement ps2 = conn.prepareStatement(updateSql)) {
                        ps2.setString(1, username);
                        return ps2.executeUpdate() > 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Soft delete by username
     * @param username account username
     * @return true if success
     */
    public boolean deleteByUsername(String username) {
        String sql = "UPDATE Account SET accStatus = 0 WHERE username = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find staff by username
     * @param username account username
     * @return StaffDTO or null
     */
    public StaffDTO findByUsername(String username) {
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                   + "WHERE a.username = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Find staff by email
     * @param email staff email
     * @return StaffDTO or null
     */
    public StaffDTO findByEmail(String email) {
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                   + "WHERE a.email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
/**
 * Get staffID by username from Staff table
 * @param username the staff's username
 * @return staffID if found, -1 otherwise
 */
public int getStaffIDByUsername(String username) {
    String sql = "SELECT staffID FROM Staff WHERE username = ?";
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, username);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("staffID");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}

    /**
     * Build StaffDTO from ResultSet
     * @param rs result
     * @return StaffDTO
     * @throws SQLException exception
     */
    private StaffDTO extractStaff(ResultSet rs) throws SQLException {
        StaffDTO s = new StaffDTO();
        s.setStaffID(rs.getInt("staffID"));
        s.setUsername(rs.getString("username"));
        s.setFirstName(rs.getString("firstName"));
        s.setLastName(rs.getString("lastName"));
        s.setDob(rs.getDate("dob"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setRole(rs.getInt("role"));
        s.setAddress(rs.getString("address"));
        s.setSex(rs.getInt("sex"));
        s.setAccStatus(rs.getInt("accStatus"));
        return s;
    }
}
