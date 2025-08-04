package util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ValidationUtil – Provides static methods to validate input fields for
 * registration/login/update.
 *
 * @author SE
 */
public class ValidationUtil {

    /**
     * Validate username: not null, alphanumeric only, max 30 characters
     *
     * @param username input username
     * @return true if valid
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{1,30}$");
    }

/**
 * Validate name: allow Vietnamese letters, spaces; no digits or special characters
 *
 * @param name input name
 * @return true if valid
 */
public static boolean isValidName(String name) {
    return name != null && name.matches("^[\\p{L}]+( [\\p{L}]+)*$");
}

/**
 * Validate email format
 *
 * @param email input email
 * @return true if valid
 */
public static boolean isValidEmail(String email) {
    if (email == null) return false;
    String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})*$";
    return email.matches(regex);
}

    /**
     * Validate strong password: min 8 chars, must include Aa#1
     *
     * @param password input password
     * @return true if valid
     */
    public static boolean isValidPassword(String password) {
        return password != null
                && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$");
    }

    /**
     * Validate confirm password match
     *
     * @param password password
     * @param confirm confirmation
     * @return true if both match
     */
    public static boolean isConfirmPasswordMatch(String password, String confirm) {
        return password != null && password.equals(confirm);
    }

    /**
     * Validate Vietnamese phone number format
     *
     * @param phone input phone
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(03|05|07|08|09)\\d{8}$");
    }

    /**
     * Validate address not blank
     *
     * @param address input address
     * @return true if valid
     */
    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Validate gender is either "0" or "1"
     *
     * @param gender input gender
     * @return true if valid
     */
    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equals("0") || gender.equals("1"));
    }

    /**
     * Validate date of birth is in dd/MM/yyyy format and age >= 13
     *
     * @param dobString input dob (dd/MM/yyyy)
     * @return true if valid
     */
    public static boolean isValidDob(String dobString) {
        try {
            LocalDate dob = LocalDate.parse(dobString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return Period.between(dob, LocalDate.now()).getYears() >= 13;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
