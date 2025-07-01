package dto;

public class SupplierDTO {
    private int supID;
    private String supName;
    private String supEmail;
    private String supPhone;
    private String supAddress;
    private String supPassword;
    private String supImage;
    private int supStatus;

    public SupplierDTO() {}

    public SupplierDTO(int supID, String supName, String supEmail, String supPhone,
                       String supAddress, String supPassword, String supImage, int supStatus) {
        this.supID = supID;
        this.supName = supName;
        this.supEmail = supEmail;
        this.supPhone = supPhone;
        this.supAddress = supAddress;
        this.supPassword = supPassword;
        this.supImage = supImage;
        this.supStatus = supStatus;
    }
        

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getSupEmail() {
        return supEmail;
    }

    public void setSupEmail(String supEmail) {
        this.supEmail = supEmail;
    }

    public String getSupPhone() {
        return supPhone;
    }

    public void setSupPhone(String supPhone) {
        this.supPhone = supPhone;
    }

    public String getSupAddress() {
        return supAddress;
    }

    public void setSupAddress(String supAddress) {
        this.supAddress = supAddress;
    }

    public String getSupPassword() {
        return supPassword;
    }

    public void setSupPassword(String supPassword) {
        this.supPassword = supPassword;
    }

    public String getSupImage() {
        return supImage;
    }

    public void setSupImage(String supImage) {
        this.supImage = supImage;
    }

    public int getSupStatus() {
        return supStatus;
    }

    public void setSupStatus(int supStatus) {
        this.supStatus = supStatus;
    }

    
}

