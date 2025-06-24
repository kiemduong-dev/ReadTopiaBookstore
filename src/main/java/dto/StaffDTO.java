/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author ADMIN
 */
import java.io.Serializable;
import java.sql.Date;

import java.io.Serializable;
import java.sql.Date;

/**
 * StaffDTO is a Data Transfer Object representing a staff member. It extends
 * AccountDTO to inherit all account-related fields and adds a staff-specific
 * identifier (staffID).
 */
public class StaffDTO extends AccountDTO implements Serializable {

    // Unique identifier for staff, typically referencing a Staff table
    private int staffID;

    // ===== Constructors =====
    /**
     * Default constructor. Used when creating a StaffDTO instance without
     * initialization.
     */
    public StaffDTO() {
        super(); // Call parent (AccountDTO) default constructor
    }

    /**
     * Constructor to initialize StaffDTO with only staff ID. Useful in cases
     * where only staffID is known (e.g., delete or lookup).
     *
     * @param staffID Unique staff identifier
     */
    public StaffDTO(int staffID) {
        super(); // Call parent constructor
        this.staffID = staffID;
    }

    /**
     * Constructor to initialize only the account-level fields (no staffID).
     * Used when registering or modifying account info before assigning staff
     * ID.
     *
     * @param username Account username
     * @param password Account password
     * @param firstName User's first name
     * @param lastName User's last name
     * @param dob Date of birth
     * @param email Email address
     * @param phone Phone number
     * @param role User role (0 = admin/staff, 1 = customer)
     * @param address Physical address
     * @param sex Gender (0 = female, 1 = male)
     * @param accStatus Account status (0 = inactive, 1 = active)
     * @param code One-time password or verification code
     */
    public StaffDTO(String username, String password, String firstName, String lastName,
            Date dob, String email, String phone, int role, String address,
            int sex, int accStatus, String code) {
        super(username, password, firstName, lastName, dob, email, phone, role, address, sex, accStatus, code);
    }

    /**
     * Constructor that initializes all fields for both staff and account.
     * Typically used when loading a full staff record from the database.
     *
     * @param staffID Unique staff identifier
     * @param username Account username
     * @param password Account password
     * @param firstName First name
     * @param lastName Last name
     * @param dob Date of birth
     * @param email Email address
     * @param phone Phone number
     * @param role Role value
     * @param address Address string
     * @param sex Gender value
     * @param accStatus Account status
     * @param code Code used for OTP or verification
     */
    public StaffDTO(int staffID, String username, String password, String firstName, String lastName,
            Date dob, String email, String phone, int role, String address,
            int sex, int accStatus, String code) {
        super(username, password, firstName, lastName, dob, email, phone, role, address, sex, accStatus, code);
        this.staffID = staffID;
    }

    /**
     * Constructor that promotes an existing AccountDTO to StaffDTO by attaching
     * staff ID. Useful when transforming user to staff context without
     * duplicating fields.
     *
     * @param staffID Unique staff ID
     * @param account Existing AccountDTO object
     */
    public StaffDTO(int staffID, AccountDTO account) {
        super(account.getUsername(), account.getPassword(), account.getFirstName(), account.getLastName(),
                account.getDob(), account.getEmail(), account.getPhone(), account.getRole(),
                account.getAddress(), account.getSex(), account.getAccStatus(), account.getCode());
        this.staffID = staffID;
    }

    // ===== Getter & Setter =====
    /**
     * Retrieves the staff ID associated with this StaffDTO.
     *
     * @return staffID integer value
     */
    public int getStaffID() {
        return staffID;
    }

    /**
     * Sets or updates the staff ID.
     *
     * @param staffID Unique staff identifier to assign
     */
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    // ===== Debugging Support =====
    /**
     * Returns a human-readable string representation of this StaffDTO object.
     * Useful for debugging or logging staff information.
     *
     * @return formatted String containing all staff fields
     */
    @Override
    public String toString() {
        return "StaffDTO{"
                + "staffID=" + staffID
                + ", username='" + getUsername() + '\''
                + ", fullName='" + getFullName() + '\''
                + ", dob=" + getDob()
                + ", email='" + getEmail() + '\''
                + ", phone='" + getPhone() + '\''
                + ", role=" + getRole()
                + ", address='" + getAddress() + '\''
                + ", sex=" + getSex()
                + ", status=" + getAccStatus()
                + ", code='" + getCode() + '\''
                + '}';
    }
}
