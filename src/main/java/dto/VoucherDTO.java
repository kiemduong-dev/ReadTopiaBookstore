/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Date;

/**
 *
 * @author default
 */
public class VoucherDTO {

    private int vouID;
    private String vouName;
    private String vouCode;
    private double discount;
    private Date startDate;
    private Date endDate;
    private int quantity;
    private int vouStatus;
    private String createdBy;        // Sửa từ int → String
    private String approvedBy;       // Sửa từ Integer → String

    // Optional fields for display purposes (not in DB)
    private int creatorRole;
    private int approverRole;

    private int maxQuantity;
    private int quantityUsed;

    public VoucherDTO() {
    }

    public VoucherDTO(int vouID, String vouName, String vouCode, double discount,
                      Date startDate, Date endDate, int quantity, int vouStatus,
                      String createdBy, String approvedBy) {
        this.vouID = vouID;
        this.vouName = vouName;
        this.vouCode = vouCode;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.vouStatus = vouStatus;
        this.createdBy = createdBy;
        this.approvedBy = approvedBy;
    }

    // Getters & Setters

    public int getVouID() {
        return vouID;
    }

    public void setVouID(int vouID) {
        this.vouID = vouID;
    }

    public String getVouName() {
        return vouName;
    }

    public void setVouName(String vouName) {
        this.vouName = vouName;
    }

    public String getVouCode() {
        return vouCode;
    }

    public void setVouCode(String vouCode) {
        this.vouCode = vouCode;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVouStatus() {
        return vouStatus;
    }

    public void setVouStatus(int vouStatus) {
        this.vouStatus = vouStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public int getCreatorRole() {
        return creatorRole;
    }

    public void setCreatorRole(int creatorRole) {
        this.creatorRole = creatorRole;
    }

    public int getApproverRole() {
        return approverRole;
    }

    public void setApproverRole(int approverRole) {
        this.approverRole = approverRole;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(int quantityUsed) {
        this.quantityUsed = quantityUsed;
    }
}
