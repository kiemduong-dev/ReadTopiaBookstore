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
public class PromotionLogDTO {
    private int proLogID;
    private int proID;
    private int staffID;
    private int proAction;     // 1: Add, 2: Edit, 3: Delete
    private Date proLogDate;

    public PromotionLogDTO() {
    }

    public PromotionLogDTO(int proLogID, int proID, int staffID, int proAction, Date proLogDate) {
        this.proLogID = proLogID;
        this.proID = proID;
        this.staffID = staffID;
        this.proAction = proAction;
        this.proLogDate = proLogDate;
    }

    public int getProLogID() {
        return proLogID;
    }

    public void setProLogID(int proLogID) {
        this.proLogID = proLogID;
    }

    public int getProID() {
        return proID;
    }

    public void setProID(int proID) {
        this.proID = proID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public int getProAction() {
        return proAction;
    }

    public void setProAction(int proAction) {
        this.proAction = proAction;
    }

    public Date getProLogDate() {
        return proLogDate;
    }

    public void setProLogDate(Date proLogDate) {
        this.proLogDate = proLogDate;
    }
    
    
}
