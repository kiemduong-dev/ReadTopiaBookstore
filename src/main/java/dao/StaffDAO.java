    package dao;

    import dto.StaffDTO;
    import util.DBContext;
    import util.SecurityUtil;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;

    /**
     * DAO class for Staff operations Lớp DAO xử lý các thao tác với nhân viên và
     * tài khoản liên quan
     */
    public class StaffDAO {

        /**
         * Get all staff with account info. Lấy toàn bộ nhân viên kèm thông tin tài
         * khoản
         */
        public List<StaffDTO> findAll() {
            List<StaffDTO> list = new ArrayList<>();
            String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username";
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
         * Search staffs by keyword in username, firstName, lastName (case
         * insensitive). Tìm kiếm nhân viên theo từ khóa (không phân biệt hoa
         * thường)
         */
        public List<StaffDTO> searchStaffs(String keyword) {
            List<StaffDTO> list = new ArrayList<>();
            String sql = "SELECT s.staffID, a.* FROM Staff s JOIN Account a ON s.username = a.username "
                    + "WHERE LOWER(a.username) LIKE ? OR LOWER(a.firstName) LIKE ? OR LOWER(a.lastName) LIKE ?";
            try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

                String kw = "%" + keyword.toLowerCase() + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
                ps.setString(3, kw);

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
         * Get a single staff by staffID. Lấy nhân viên theo staffID
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
         * Add a new staff (including account creation). Thêm nhân viên mới (bao gồm
         * tạo tài khoản)
         */
        public boolean addStaff(StaffDTO staff) {
            String insertAccount = "INSERT INTO Account (username, password, firstName, lastName, dob, email, phone, role, sex, address, accStatus, code) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String insertStaff = "INSERT INTO Staff (username) VALUES (?)";

            try ( Connection conn = new DBContext().getConnection()) {
                conn.setAutoCommit(false);

                try ( PreparedStatement ps1 = conn.prepareStatement(insertAccount);  PreparedStatement ps2 = conn.prepareStatement(insertStaff)) {

                    ps1.setString(1, staff.getUsername());
                    ps1.setString(2, SecurityUtil.hashPassword(staff.getPassword())); // Mã hóa mật khẩu
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
         * Update existing staff info in Account table. Cập nhật thông tin nhân viên
         * trong bảng Account
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
         * Delete staff by username only from Staff table. Xóa nhân viên theo
         * username (chỉ xóa trong bảng Staff)
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
         * Delete staff by staffID (delete from Staff and Account tables). Xóa nhân
         * viên theo staffID, bao gồm xóa tài khoản trong Account
         */
        public boolean deleteStaffByID(int staffID) {
            String selectUsername = "SELECT username FROM Staff WHERE staffID = ?";
            String deleteStaff = "DELETE FROM Staff WHERE staffID = ?";
            String deleteAccount = "DELETE FROM Account WHERE username = ?";

            try ( Connection conn = new DBContext().getConnection()) {
                conn.setAutoCommit(false);

                String username = null;

                // Lấy username theo staffID
                try ( PreparedStatement ps = conn.prepareStatement(selectUsername)) {
                    ps.setInt(1, staffID);
                    try ( ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            username = rs.getString("username");
                        } else {
                            return false; // staffID không tồn tại
                        }
                    }
                }

                // Xóa Staff
                try ( PreparedStatement ps1 = conn.prepareStatement(deleteStaff);  PreparedStatement ps2 = conn.prepareStatement(deleteAccount)) {

                    ps1.setInt(1, staffID);
                    ps1.executeUpdate();

                    // Xóa Account theo username
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
         * Extract StaffDTO object from ResultSet. Chuyển đổi dữ liệu ResultSet sang
         * đối tượng StaffDTO
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
    }
