package dto;

import java.sql.Date;

public class ImportStockDTO {
    private int id;
    private int supplierID;
    private int staffID;
    private Date importDate;
    private int status;
    private String supplierName;
    private String staffName;
    private double totalPrice;

    public ImportStockDTO() {}

    public ImportStockDTO(int id, int supplierID, Date importDate, int staffID, int status) {
        this.id = id;
        this.supplierID = supplierID;
        this.importDate = importDate;
        this.staffID = staffID;
        this.status = status;
 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
