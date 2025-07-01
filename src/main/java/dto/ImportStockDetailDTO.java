package dto;

public class ImportStockDetailDTO {
    private int ISDID;
    private int bookID;
    private int ISID;
    private int ISDQuantity;
    private double importPrice;
    private String bookTitle;

    public ImportStockDetailDTO() {}

    public ImportStockDetailDTO(int ISDID, int bookID, int ISID, int ISDQuantity, double importPrice) {
        this.ISDID = ISDID;
        this.bookID = bookID;
        this.ISID = ISID;
        this.ISDQuantity = ISDQuantity;
        this.importPrice = importPrice;
    }

    public double getTotalPrice() {
        return ISDQuantity * importPrice;
    }

    public int getISDID() {
        return ISDID;
    }

    public void setISDID(int ISDID) {
        this.ISDID = ISDID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getISID() {
        return ISID;
    }

    public void setISID(int ISID) {
        this.ISID = ISID;
    }

    public int getISDQuantity() {
        return ISDQuantity;
    }

    public void setISDQuantity(int ISDQuantity) {
        this.ISDQuantity = ISDQuantity;
    }

    public double getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(double importPrice) {
        this.importPrice = importPrice;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

}