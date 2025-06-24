/*
 * Click nbproject://nbproject/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbproject://nbproject/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author NGUYEN THAI ANH
 */
public class PaymentDTO {

    private String paymentId;
    private String orderId;
    private double amount;
    private String status;

    public PaymentDTO() {
    }

    public PaymentDTO(String paymentId, String orderId, double amount, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    // Getters and Setters
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}