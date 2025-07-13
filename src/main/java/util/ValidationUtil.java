package util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * ValidationUtil – Provides static methods to validate input fields for
 * registration/login/update
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
     * Validate first/last name: alphabet letters & spaces only, no digits or
     * symbols
     *
     * @param name input name
     * @return true if valid
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]{1,50}$");
    }

    /**
     * Validate email format
     *
     * @param email input email
     * @return true if valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Validate password strength
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
     * @param password original password
     * @param confirm confirm password
     * @return true if match
     */
    public static boolean isConfirmPasswordMatch(String password, String confirm) {
        return password != null && password.equals(confirm);
    }

    /**
     * Validate Vietnamese phone number
     *
     * @param phone input phone number
     * @return true if valid
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^(03|05|07|08|09)\\d{8}$");
    }

    /**
     * Validate address is not blank
     *
     * @param address input address
     * @return true if valid
     */
    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Validate gender: must be "0" or "1"
     *
     * @param gender input gender
     * @return true if valid
     */
    public static boolean isValidGender(String gender) {
        return gender != null && (gender.equals("0") || gender.equals("1"));
    }

    /**
     * Validate date of birth: format yyyy-MM-dd and age >= 13
     *
     * @param dobString input date string yyyy-MM-dd
     * @return true if valid
     */
    public static boolean isValidDob(String dobString) {
        try {
            LocalDate dob = LocalDate.parse(dobString);
            return Period.between(dob, LocalDate.now()).getYears() >= 13;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
