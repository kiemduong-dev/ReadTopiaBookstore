/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.PromotionLogDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ngtua
 */
public class PromotionLogDAO {

    private Connection conn;

    public PromotionLogDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PromotionLogDTO> getAllLogs(Integer actionFilter) {
        List<PromotionLogDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM promotion_log ";
        if (actionFilter != null) {
            sql += "WHERE pro_action = ?";
        }

        try ( PreparedStatement ps = conn.prepareStatement(sql.trim())) {
            if (actionFilter != null) {
                ps.setInt(1, actionFilter);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PromotionLogDTO log = new PromotionLogDTO(
                        rs.getInt("pro_log_id"),
                        rs.getInt("pro_id"),
                        rs.getInt("staff_id"),
                        rs.getInt("pro_action"),
                        rs.getDate("pro_log_date")
                );
                list.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insertLog(int proID, int staffID, int actionCode) {
        String sql = "INSERT INTO promotion_log (pro_id, staff_id, pro_action, pro_log_date) VALUES (?, ?, ?, GETDATE())";

        try ( PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, proID);
            ps.setInt(2, staffID);
            ps.setInt(3, actionCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
