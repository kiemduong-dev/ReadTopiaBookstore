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

public class VoucherLogDTO {
    private int vouLogID;
    private int vouID;
    private String username;
    private int vouAction;
    private Date vouLogDate;
    private int role;

    // Constructors
    public VoucherLogDTO() {
    }

    public VoucherLogDTO(int vouLogID, int vouID, String username, int vouAction, Date vouLogDate, int role) {
        this.vouLogID = vouLogID;
        this.vouID = vouID;
        this.username = username;
        this.vouAction = vouAction;
        this.vouLogDate = vouLogDate;
        this.role = role;
    }

    public int getVouLogID() {
        return vouLogID;
    }

    public void setVouLogID(int vouLogID) {
        this.vouLogID = vouLogID;
    }

    public int getVouID() {
        return vouID;
    }

    public void setVouID(int vouID) {
        this.vouID = vouID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVouAction() {
        return vouAction;
    }

    public void setVouAction(int vouAction) {
        this.vouAction = vouAction;
    }

    public Date getVouLogDate() {
        return vouLogDate;
    }

    public void setVouLogDate(Date vouLogDate) {
        this.vouLogDate = vouLogDate;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    
}
