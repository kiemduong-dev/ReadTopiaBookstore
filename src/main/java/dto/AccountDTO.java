package dto;

import java.sql.Date;

/**
 * CapyBook - Represents the Account entity in the system. Used to transfer data
 * between View, Controller, and DAO layers.
 *
 * @author CE181518 Dương An Kiếm
 */
public class AccountDTO {

    private int staffID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date dob;
    private String email;
    private String phone;
    private int role;       // 0 = admin/staff, 1 = customer
    private String address;
    private int sex;        // 0 = female, 1 = male
    private int accStatus;  // 0 = inactive, 1 = active
    private String code;    // OTP or password reset code

    /**
     * Default constructor.
     */
    public AccountDTO() {
    }

    /**
     * Parameterized constructor.
     *
     * @param username the username
     * @param password the encrypted password
     * @param firstName user's first name
     * @param lastName user's last name
     * @param dob date of birth
     * @param email user email
     * @param phone user phone number
     * @param role account role (0 = admin/staff, 1 = customer)
     * @param address user's address
     * @param sex user's gender (0 = female, 1 = male)
     * @param accStatus account status (0 = inactive, 1 = active)
     * @param code OTP or password reset code
     */
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

    /**
     * Copy constructor.
     *
     * @param other another AccountDTO instance to copy from
     */
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

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return date of birth
     */
    public Date getDob() {
        return dob;
    }

    /**
     * @param dob the date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return role
     */
    public int getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * @return account status
     */
    public int getAccStatus() {
        return accStatus;
    }

    /**
     * @param accStatus the account status to set
     */
    public void setAccStatus(int accStatus) {
        this.accStatus = accStatus;
    }

    /**
     * @return OTP or reset code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get full name by combining first and last name.
     *
     * @return full name string
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Check if the account is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this.accStatus == 1;
    }

    public int getStaffID() {
        return staffID;
    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    /**
     * Convert object to string for logging/debugging.
     *
     * @return string representation of the object
     */
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
