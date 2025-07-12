package dao;

import dto.StaffDTO;
import util.DBContext;
import util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StaffDAO - Handles all data operations related to staff management and linked
 * account details.
 *
 * @author CE181518 Dương An Kiếm
 */
public class StaffDAO {

    /**
     * Retrieve all staff records with account information.
     *
     * @return list of all staff records.
     */
    public List<StaffDTO> findAll() {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                + "WHERE a.accStatus = 1 AND (a.role = 1 OR a.role = 2 OR a.role = 3)";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractStaff(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<StaffDTO> findAllActiveStaffSellerWarehouse() {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                + "WHERE a.accStatus = 1 AND (a.role = 1 OR a.role = 2 OR a.role = 3)";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractStaff(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Search seller staff or warehouse staff by keyword (username, first name,
     * last name). Only includes role 2 and 3 with accStatus = 1.
     *
     * @param keyword the keyword to search for
     * @return list of matched staff records
     */
    public List<StaffDTO> searchStaffs(String keyword) {
        List<StaffDTO> list = new ArrayList<>();
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                + "WHERE a.accStatus = 1 AND (a.role = 2 OR a.role = 3) "
                + "AND (LOWER(a.username) LIKE ? OR LOWER(a.firstName) LIKE ? OR LOWER(a.lastName) LIKE ?)";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 3; i++) {
                ps.setString(i, kw);
            }
            try ( ResultSet rs = ps.executeQuery()) {
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
     * Retrieve a staff record by staffID.
     *
     * @param staffID the staff ID to search.
     * @return the matched StaffDTO object, or null if not found.
     */
    public StaffDTO getStaffByID(int staffID) {
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username WHERE s.staffID = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, staffID);

            try ( ResultSet rs = ps.executeQuery()) {
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
     * Add a new staff (includes account creation).
     *
     * @param staff the staff object to be added.
     * @return true if insertion is successful, otherwise false.
     */
    public boolean addStaff(StaffDTO staff) {
        String insertAccount = "INSERT INTO Account (username, password, firstName, lastName, dob, email, phone, role, sex, address, accStatus, code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertStaff = "INSERT INTO Staff (username) VALUES (?)";

        try ( Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);

            try ( PreparedStatement ps1 = conn.prepareStatement(insertAccount);  PreparedStatement ps2 = conn.prepareStatement(insertStaff)) {

                ps1.setString(1, staff.getUsername());
                ps1.setString(2, SecurityUtil.hashPassword(staff.getPassword()));
                ps1.setString(3, staff.getFirstName());
                ps1.setString(4, staff.getLastName());
                ps1.setDate(5, staff.getDob());
                ps1.setString(6, staff.getEmail());
                ps1.setString(7, staff.getPhone());
                ps1.setInt(8, staff.getRole());
                ps1.setInt(9, staff.getSex());
                ps1.setString(10, staff.getAddress());
                ps1.setInt(11, staff.getAccStatus());
                ps1.setString(12, staff.getCode());
                ps1.executeUpdate();

                ps2.setString(1, staff.getUsername());
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
     * Update existing staff's account information.
     *
     * @param staff the staff object with updated data.
     * @return true if update is successful, otherwise false.
     */
    public boolean updateStaff(StaffDTO staff) {
        String sql = "UPDATE Account SET firstName=?, lastName=?, dob=?, email=?, phone=?, role=?, sex=?, address=?, accStatus=?, code=? WHERE username=?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFirstName());
            ps.setString(2, staff.getLastName());
            ps.setDate(3, staff.getDob());
            ps.setString(4, staff.getEmail());
            ps.setString(5, staff.getPhone());
            ps.setInt(6, staff.getRole());
            ps.setInt(7, staff.getSex());
            ps.setString(8, staff.getAddress());
            ps.setInt(9, staff.getAccStatus());
            ps.setString(10, staff.getCode());
            ps.setString(11, staff.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete staff by username (from Staff table only).
     *
     * @param username the username of the staff to be deleted.
     * @return true if deletion is successful, otherwise false.
     */
    public boolean deleteByUsername(String username) {
        String sql = "DELETE FROM Staff WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete staff by staffID, including related account in Account table.
     *
     * @param staffID the ID of the staff to be deleted.
     * @return true if deletion is successful, otherwise false.
     */
    public boolean deleteStaffByID(int staffID) {
        String selectUsername = "SELECT username FROM Staff WHERE staffID = ?";
        String deleteStaff = "DELETE FROM Staff WHERE staffID = ?";
        String deleteAccount = "DELETE FROM Account WHERE username = ?";

        try ( Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            String username = null;

            try ( PreparedStatement ps = conn.prepareStatement(selectUsername)) {
                ps.setInt(1, staffID);
                try ( ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        username = rs.getString("username");
                    } else {
                        return false;
                    }
                }
            }

            try ( PreparedStatement ps1 = conn.prepareStatement(deleteStaff);  PreparedStatement ps2 = conn.prepareStatement(deleteAccount)) {

                ps1.setInt(1, staffID);
                ps1.executeUpdate();

                ps2.setString(1, username);
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
     * Extract a StaffDTO object from a ResultSet row.
     *
     * @param rs the ResultSet from the executed query.
     * @return a populated StaffDTO object.
     * @throws SQLException if a database error occurs.
     */
    private StaffDTO extractStaff(ResultSet rs) throws SQLException {
        StaffDTO s = new StaffDTO();
        s.setStaffID(rs.getInt("staffID"));
        s.setUsername(rs.getString("username"));
        s.setPassword(rs.getString("password"));
        s.setFirstName(rs.getString("firstName"));
        s.setLastName(rs.getString("lastName"));
        s.setDob(rs.getDate("dob"));
        s.setEmail(rs.getString("email"));
        s.setPhone(rs.getString("phone"));
        s.setRole(rs.getInt("role"));
        s.setSex(rs.getInt("sex"));
        s.setAddress(rs.getString("address"));
        s.setAccStatus(rs.getInt("accStatus"));
        s.setCode(rs.getString("code"));
        return s;
    }

    public int getStaffIDByUsername(String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT staffID FROM Staff WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("staffID");
            }
            throw new SQLException("Username không tồn tại trong bảng Staff: " + username);
        }
    }

    public StaffDTO findByUsername(String username) {
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username WHERE a.username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StaffDTO findByEmail(String email) {
        String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username WHERE a.email = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractStaff(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StaffDTO> getAllStaff() {
        List<StaffDTO> activeStaff = new ArrayList<>();
        for (StaffDTO s : findAll()) {
            if (s.getAccStatus() == 1 && (s.getRole() == 2 || s.getRole() == 3)) {
                activeStaff.add(s);
            }
        }
        return activeStaff;
    }

}
