/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.VoucherLogDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.DBContext;

/**
 *
 * @author default
 */
public class VoucherLogDAO {

    public List<VoucherLogDTO> getAllLogs(Integer actionFilter) {
        List<VoucherLogDTO> list = new ArrayList<>();
        String sql = "SELECT vl.*, a.role FROM voucher_log vl JOIN Account a ON vl.username = a.username";

        if (actionFilter != null) {
            sql += "WHERE vou_action = ?";
        }

        try (
                 Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql.trim())) {

            if (actionFilter != null) {
                ps.setInt(1, actionFilter);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                VoucherLogDTO log = new VoucherLogDTO(
                        rs.getInt("vou_log_id"),
                        rs.getInt("vou_id"),
                        rs.getString("username"),
                        rs.getInt("vou_action"),
                        rs.getDate("vou_log_date"),
                        rs.getInt("role")
                );

                list.add(log);
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(VoucherLogDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return list;
    }

    public void insertLog(int vouID, String username, int actionCode) {
        String sql = "INSERT INTO voucher_log (vou_id, username, vou_action, vou_log_date) VALUES (?, ?, ?, GETDATE())";

        try (
                 Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vouID);
            ps.setString(2, username);
            ps.setInt(3, actionCode);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(VoucherLogDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
