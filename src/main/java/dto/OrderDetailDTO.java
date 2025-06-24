/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author NGUYEN THAI ANH
 */
public class OrderDetailDTO {

    private int ODID;
    private int orderID;
    private int bookID;
    private int quantity;
    private double totalPrice;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int ODID, int orderID, int bookID, int quantity, double totalPrice) {
        this.ODID = ODID;
        this.orderID = orderID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getODID() {
        return ODID;
    }

    public void setODID(int ODID) {
        this.ODID = ODID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
