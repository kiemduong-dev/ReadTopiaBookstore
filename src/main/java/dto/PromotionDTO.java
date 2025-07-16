/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Date;

/**
 *
 * @author ngtua
 */
public class PromotionDTO {

    private int proID;
    private String proName;
    private String proCode;
    private double discount;
    private Date startDate;
    private Date endDate;
    private int quantity;
    private int proStatus;
    private int createdBy;
    private Integer approvedBy;

    public PromotionDTO() {
    }

    public PromotionDTO(int proID, String proName, String proCode, double discount,
            Date startDate, Date endDate, int quantity, int proStatus,
            int createdBy, int approvedBy) {
        this.proID = proID;
        this.proName = proName;
        this.proCode = proCode;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quantity = quantity;
        this.proStatus = proStatus;
        this.createdBy = createdBy;
        this.approvedBy = approvedBy;
    }

    // Getters and Setters
    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
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

    public int getProStatus() {
        return proStatus;
    }

    public void setProStatus(int proStatus) {
        this.proStatus = proStatus;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    private int creatorRole;
    private int approverRole;

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

}
