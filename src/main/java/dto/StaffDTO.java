package dto;

import java.io.Serializable;
import java.sql.Date;

/**
 * StaffDTO - Represents a staff member in the system.
 *
 * This class extends AccountDTO and adds a unique staffID to represent
 * staff-specific information. It is used as a Data Transfer Object (DTO) for
 * transferring staff data between application layers.
 *
 * @author CE181518 Dương An Kiếm
 */
public class StaffDTO extends AccountDTO implements Serializable {

    /**
     * Unique identifier for staff member.
     */
    private int staffID;

    // ===== Constructors =====
    /**
     * Default constructor for StaffDTO. Initializes a new staff object with
     * default values.
     */
    public StaffDTO() {
        super();
    }

    /**
     * Constructs a StaffDTO with only the staff ID.
     *
     * @param staffID The unique identifier for the staff member
     */
    public StaffDTO(int staffID) {
        super();
        this.staffID = staffID;
    }

    /**
     * Constructs a StaffDTO using account-level fields only. Used when account
     * details are known, but staffID is not yet assigned.
     *
     * @param username Username of the staff
     * @param password Password (hashed)
     * @param firstName First name
     * @param lastName Last name
     * @param dob Date of birth
     * @param email Email address
     * @param phone Phone number
     * @param role Role (0 = admin/staff, 1 = customer)
     * @param address Address of residence
     * @param sex Gender (0 = female, 1 = male)
     * @param accStatus Account status (0 = inactive, 1 = active)
     * @param code Verification or OTP code
     */
    public StaffDTO(String username, String password, String firstName, String lastName,
            Date dob, String email, String phone, int role, String address,
            int sex, int accStatus, String code) {
        super(username, password, firstName, lastName, dob, email, phone, role, address, sex, accStatus, code);
    }

    /**
     * Constructs a StaffDTO with both staffID and all account fields.
     *
     * @param staffID Unique staff identifier
     * @param username Username
     * @param password Password
     * @param firstName First name
     * @param lastName Last name
     * @param dob Date of birth
     * @param email Email
     * @param phone Phone number
     * @param role Role
     * @param address Address
     * @param sex Gender
     * @param accStatus Account status
     * @param code OTP or verification code
     */
    public StaffDTO(int staffID, String username, String password, String firstName, String lastName,
            Date dob, String email, String phone, int role, String address,
            int sex, int accStatus, String code) {
        super(username, password, firstName, lastName, dob, email, phone, role, address, sex, accStatus, code);
        this.staffID = staffID;
    }

    /**
     * Constructs a StaffDTO using an existing AccountDTO and a staff ID. Useful
     * for upgrading a user to staff without retyping fields.
     *
     * @param staffID The staff identifier
     * @param account The AccountDTO object to copy data from
     */
    public StaffDTO(int staffID, AccountDTO account) {
        super(account.getUsername(), account.getPassword(), account.getFirstName(), account.getLastName(),
                account.getDob(), account.getEmail(), account.getPhone(), account.getRole(),
                account.getAddress(), account.getSex(), account.getAccStatus(), account.getCode());
        this.staffID = staffID;
    }

    // ===== Getters & Setters =====
    /**
     * Gets the staff ID.
     *
     * @return The unique staff ID
     */
    public int getStaffID() {
        return staffID;
    }

    /**
     * Sets the staff ID.
     *
     * @param staffID The unique staff ID to assign
     */
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    // ===== Utility Methods =====
    /**
     * Returns a string representation of the staff object for debugging or
     * logging.
     *
     * @return A readable string of staff information
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
