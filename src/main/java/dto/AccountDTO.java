package dto;

import java.sql.Date;

/**
 * DTO đại diện cho bảng Account trong database. Dùng để truyền dữ liệu giữa
 * View - Controller - DAO trong CapyBook.
 */
public class AccountDTO {

    private String username;
    private String password;  // Đã mã hóa BCrypt trong DB
    private String firstName;
    private String lastName;
    private Date dob;
    private String email;
    private String phone;
    private int role;         // 0: admin/staff, 1: customer
    private String address;
    private int sex;          // 0: female, 1: male
    private int accStatus;    // 0: inactive, 1: active
    private String code;      // Dùng cho OTP/reset

    // === Constructors ===
    public AccountDTO() {
    }

    public AccountDTO(String username, String password, String firstName, String lastName,
            Date dob, String email, String phone, int role, String address,
            int sex, int accStatus, String code) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.sex = sex;
        this.accStatus = accStatus;
        this.code = code;
    }

    public AccountDTO(AccountDTO other) {
        this.username = other.username;
        this.password = other.password;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.dob = other.dob;
        this.email = other.email;
        this.phone = other.phone;
        this.role = other.role;
        this.address = other.address;
        this.sex = other.sex;
        this.accStatus = other.accStatus;
        this.code = other.code;
    }

    // === Getters & Setters ===
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // === Utility Methods ===
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isActive() {
        return this.accStatus == 1;
    }

    @Override
    public String toString() {
        return "AccountDTO{"
                + "username='" + username + '\''
                + ", fullName='" + getFullName() + '\''
                + ", dob=" + dob
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", role=" + role
                + ", sex=" + sex
                + ", accStatus=" + accStatus
                + '}';
    }
}
