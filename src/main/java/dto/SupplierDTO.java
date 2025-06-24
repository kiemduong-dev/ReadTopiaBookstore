package dto;

public class SupplierDTO {

    private int id;
    private String supName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private boolean status;
    private String image;

    public SupplierDTO() {
    }

    public SupplierDTO(int id, String name, String password, String email, String phone, String address, boolean status, String image) {
        this.id = id;
        this.supName = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

   

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
