/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

public class NotificationDTO {

    private int notID;
    private int staffID;
    private String notTitle;
    private int receiver;
    private String notDescription;
    private int notStatus;

    public NotificationDTO() {
    }

    public NotificationDTO(int notID, int staffID, String notTitle, int receiver, String notDescription, int notStatus) {
        this.notID = notID;
        this.staffID = staffID;
        this.notTitle = notTitle;
        this.receiver = receiver;
        this.notDescription = notDescription;
        this.notStatus = notStatus;
    }

    public int getNotID() {
        return notID;
    }

    public void setNotID(int notID) {
        this.notID = notID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public String getNotTitle() {
        return notTitle;
    }

    public void setNotTitle(String notTitle) {
        this.notTitle = notTitle;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getNotDescription() {
        return notDescription;
    }

    public void setNotDescription(String notDescription) {
        this.notDescription = notDescription;
    }

    public int getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(int notStatus) {
        this.notStatus = notStatus;
    }
}
