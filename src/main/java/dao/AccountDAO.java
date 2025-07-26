/**
 * AccountDAO - Provides data access functionalities for managing user accounts.
 * This class supports account creation, login verification, OTP management,
 * password update, and administrative operations on accounts.
 *
 * @author CE181518 Dương An Kiếm
 */
package dao;

import dto.AccountDTO;
import util.DBContext;
import util.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

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
            ps.setString(12, acc.getCode() != null ? acc.getCode() : ""); // Đảm bảo không truyền null
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }
// CHUẨN HÓA THÊM METHOD DƯỚI

    public boolean addAccountByAdmin(AccountDTO acc) {
        String sql = "INSERT INTO Account (username, password, firstName, lastName, email, phone, role, address, accStatus, dob, sex, code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());
            ps.setString(3, acc.getFirstName());
            ps.setString(4, acc.getLastName());
            ps.setString(5, acc.getEmail());
            ps.setString(6, acc.getPhone());
            ps.setInt(7, acc.getRole());
            ps.setString(8, acc.getAddress());
            ps.setInt(9, acc.getAccStatus());
            ps.setDate(10, acc.getDob());
            ps.setInt(11, acc.getSex());
            ps.setString(12, null); // ✅ code = null → KHÔNG CÓ OTP

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies login credentials.
     *
     * @param username the username
     * @param password the raw password
     * @return AccountDTO if credentials match, null otherwise
     */
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

    /**
     * Finds account by email.
     *
     * @param email the email to search
     * @return AccountDTO if found, null otherwise
     */
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

    /**
     * Find account by username
     *
     * @param username // Username of the account
     * @return AccountDTO object if found, null otherwise
     * @throws Exception // If database error occurs
     */
    public AccountDTO findByUsername(String username) throws Exception {
        AccountDTO account = null;
        String sql = "SELECT username, password, firstName, lastName, dob, email, phone, role, address, sex, accStatus, code "
                + "FROM Account WHERE username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    account = new AccountDTO(
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
                            rs.getString("code") // ✅ sửa ở đây
                    );
                }
            }
        }
        return account;
    }

    /**
     * Gets account by username.
     *
     * @param username the username to search
     * @return AccountDTO if found, null otherwise
     */
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

    /**
     * Updates user profile.
     *
     * @param acc the AccountDTO with updated info
     * @return true if update successful, false otherwise
     */
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

    /**
     * Updates user password using email.
     *
     * @param email the user email
     * @param newPassword the new password
     * @return true if updated successfully
     */
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

    /**
     * Updates password by username.
     *
     * @param username the username
     * @param newPassword the new password
     * @return true if successful
     */
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

    /**
     * Changes password if the old password matches.
     *
     * @param username the username
     * @param oldPass the old password
     * @param newPass the new password
     * @return true if successful
     */
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

/**
 * Admin updates all user account fields.
 *
 * @param acc the updated account
 * @return true if successful
 */
public boolean updateAccountByAdmin(AccountDTO acc) {
    String sql = "UPDATE Account SET firstName=?, lastName=?, email=?, phone=?, address=?, role=?, sex=?, dob=? WHERE username=?";
    try (Connection conn = new DBContext().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        // Kiểm tra các giá trị đầu vào
        if (acc.getUsername() == null || acc.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

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
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Unexpected Error: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}


    /**
     * Updates account status.
     *
     * @param acc the account to update
     * @return true if updated
     */
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

    /**
     * Retrieves all user accounts.
     *
     * @return list of AccountDTO
     */
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE accStatus = 1";
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

    /**
     * Searches accounts by keyword.
     *
     * @param keyword the search keyword
     * @return list of matched accounts
     */
    public List<AccountDTO> searchAccounts(String keyword) {
        List<AccountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE accStatus IN (0, 1) AND ("
                + "LOWER(username) LIKE ? "
                + "OR LOWER(CONCAT(firstName, ' ', lastName)) LIKE ? "
                + "OR LOWER(email) LIKE ? "
                + "OR phone LIKE ?)";
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

    /**
     * Saves OTP code for a specific user.
     *
     * @param username the username
     * @param otp the OTP code
     */
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

    /**
     * Clears OTP code for a specific user.
     *
     * @param username the username
     */
    public void clearOTP(String username) {
        String sql = "UPDATE Account SET code = NULL WHERE username = ?";
        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies if the OTP code matches the one stored.
     *
     * @param username the username
     * @param otp the OTP code
     * @return true if match
     */
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

    /**
     * Retrieves OTP code for a user.
     *
     * @param username the username
     * @return the OTP code or null
     */
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

    /**
     * Converts result set to AccountDTO.
     *
     * @param rs the ResultSet
     * @return AccountDTO instance
     * @throws SQLException if data cannot be read
     */
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
