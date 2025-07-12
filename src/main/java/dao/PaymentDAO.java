/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.PaymentDTO;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NGUYEN THAI ANH
 */
public class PaymentDAO {

    // Lấy thông tin thanh toán theo ID
    public PaymentDTO getPaymentById(String paymentId) {
        String sql = "SELECT * FROM Payment WHERE paymentId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentId);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractPaymentFromResultSet(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ getPaymentById error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Lấy thanh toán theo orderID
    public List<PaymentDTO> getPaymentsByOrderId(String orderId) {
        List<PaymentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE orderId = ? ORDER BY paymentDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orderId);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractPaymentFromResultSet(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("❌ getPaymentsByOrderId error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật thông tin thanh toán
    public boolean updatePayment(PaymentDTO payment) {
        String sql = "UPDATE Payment SET status = ? WHERE paymentId = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getStatus());
            ps.setString(2, payment.getPaymentId());

            boolean result = ps.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ Updated payment: " + payment.getPaymentId());
            }
            return result;

        } catch (Exception e) {
            System.err.println("❌ updatePayment error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Lấy tất cả thanh toán
    public List<PaymentDTO> getAllPayments() {
        List<PaymentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment ORDER BY paymentDate DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractPaymentFromResultSet(rs));
            }

        } catch (Exception e) {
            System.err.println("❌ getAllPayments error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Helper method để extract PaymentDTO từ ResultSet
    private PaymentDTO extractPaymentFromResultSet(ResultSet rs) throws Exception {
        PaymentDTO payment = new PaymentDTO();
        payment.setPaymentId(rs.getString("paymentId"));
        payment.setOrderId(rs.getInt("orderId"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setStatus(rs.getInt("status"));
        return payment;
    }

    public boolean insertPayment(PaymentDTO payment) throws Exception {
        String sql = "INSERT INTO Payment (paymentId, orderId, amount, status, paymentMethod) VALUES (?, ?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, payment.getPaymentId());
            ps.setInt(2, payment.getOrderId());
            ps.setDouble(3, payment.getAmount());
            ps.setInt(4, payment.getStatus());
            ps.setString(5, payment.getPaymentMethod());

            return ps.executeUpdate() > 0;
        }
    }
}
