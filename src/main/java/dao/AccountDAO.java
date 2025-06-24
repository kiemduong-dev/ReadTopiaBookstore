package dao;

import dto.AccountDTO;
import util.DBContext;
import util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // === THÊM TÀI KHOẢN MỚI ===
    public boolean addAccount(AccountDTO acc) {
        String sql = "INSERT INTO Account (username, password, firstName, lastName, email, phone, role, address, accStatus, dob, sex, code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, acc.getUsername());
            ps.setString(2, SecurityUtil.hashPassword(acc.getPassword()));
            ps.setString(3, acc.getFirstName());
            ps.setString(4, acc.getLastName());
            ps.setString(5, acc.getEmail());
            ps.setString(6, acc.getPhone());
            ps.setInt(7, acc.getRole());
            ps.setString(8, acc.getAddress());
            ps.setInt(9, acc.getAccStatus());
            ps.setDate(10, acc.getDob());
            ps.setInt(11, acc.getSex());
            ps.setString(12, acc.getCode());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === ĐĂNG NHẬP ===
    public AccountDTO login(String username, String password) {
        String sql = "SELECT * FROM Account WHERE username = ? AND accStatus = 1";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                if (SecurityUtil.checkPassword(password, hashed)) {
                    return extractAccount(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // === TÌM THEO EMAIL ===
    public AccountDTO findByEmail(String email) {
        String sql = "SELECT * FROM Account WHERE email = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAccount(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // === TÌM THEO USERNAME ===
    public AccountDTO getAccountByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractAccount(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // === CẬP NHẬT HỒ SƠ NGƯỜI DÙNG ===
    public boolean updateProfile(AccountDTO acc) {
        String sql = "UPDATE Account SET firstName=?, lastName=?, email=?, phone=?, address=? WHERE username=?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, acc.getFirstName());
            ps.setString(2, acc.getLastName());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPhone());
            ps.setString(5, acc.getAddress());
            ps.setString(6, acc.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === ĐỔI MẬT KHẨU BẰNG EMAIL ===
    public boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE Account SET password = ? WHERE email = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, SecurityUtil.hashPassword(newPassword));
            ps.setString(2, email);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === ĐỔI MẬT KHẨU BẰNG USERNAME ===
    public boolean updatePasswordByUsername(String username, String newPassword) {
        String sql = "UPDATE Account SET password = ? WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, SecurityUtil.hashPassword(newPassword));
            ps.setString(2, username);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === ĐỔI MẬT KHẨU CÓ KIỂM TRA MẬT KHẨU CŨ ===
    public boolean updatePasswordByOld(String username, String oldPass, String newPass) {
        String sql = "SELECT password FROM Account WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String currentHashed = rs.getString("password");
                if (SecurityUtil.checkPassword(oldPass, currentHashed)) {
                    return updatePasswordByUsername(username, newPass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === ADMIN CẬP NHẬT THÔNG TIN NGƯỜI DÙNG ===
    public boolean updateAccountByAdmin(AccountDTO acc) {
        String sql = "UPDATE Account SET firstName=?, lastName=?, email=?, phone=?, address=?, role=?, sex=?, dob=? WHERE username=?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, acc.getFirstName());
            ps.setString(2, acc.getLastName());
            ps.setString(3, acc.getEmail());
            ps.setString(4, acc.getPhone());
            ps.setString(5, acc.getAddress());
            ps.setInt(6, acc.getRole());
            ps.setInt(7, acc.getSex());
            ps.setDate(8, acc.getDob());
            ps.setString(9, acc.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAccountStatus(AccountDTO acc) {
        String sql = "UPDATE Account SET accStatus = ? WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, acc.getAccStatus());
            ps.setString(2, acc.getUsername());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === LẤY TOÀN BỘ TÀI KHOẢN ===
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractAccount(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<AccountDTO> searchAccounts(String keyword) {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE LOWER(username) LIKE ? "
                + "OR LOWER(firstName + ' ' + lastName) LIKE ? "
                + "OR LOWER(email) LIKE ? "
                + "OR phone LIKE ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword.toLowerCase() + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, search);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractAccount(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // === LƯU MÃ OTP ĐỂ RESET MẬT KHẨU ===
    public void saveOTPForReset(String username, String otp) {
        String sql = "UPDATE Account SET code = ? WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otp);
            ps.setString(2, username);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === XOÁ MÃ OTP ===
    public void clearOTP(String username) {
        String sql = "UPDATE Account SET code = NULL WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === XÁC THỰC MÃ OTP ===
    public boolean verifyOTP(String username, String otp) {
        String sql = "SELECT 1 FROM Account WHERE username = ? AND code = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, otp);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // === LẤY MÃ OTP THEO USERNAME ===
    public String getOTPCode(String username) {
        String sql = "SELECT code FROM Account WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("code");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // === CHUYỂN KẾT QUẢ THÀNH DTO ===
    private AccountDTO extractAccount(ResultSet rs) throws SQLException {
        return new AccountDTO(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getDate("dob"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getInt("role"),
                rs.getString("address"),
                rs.getInt("sex"),
                rs.getInt("accStatus"),
                rs.getString("code")
        );
    }
}
