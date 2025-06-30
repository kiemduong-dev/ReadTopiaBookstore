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
     * Validate first/last name: allow Vietnamese characters & spaces, no digits
     * or symbols
     *
     * @param name input name
     * @return true if valid
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[\\p{L} .'-]{1,50}$");
    }

    /**
     * Validate general email format (allow any domain)
     *
     * @param email input email
     * @return true if valid format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    /**
     * Validate password strength - At least 8 characters - Includes lowercase,
     * uppercase, number, special character
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
     * Validate Vietnamese phone number: - 10 digits - Starts with 03, 05, 07,
     * 08, 09
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
     * @return true if not empty or blank
     */
    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }

    /**
     * Validate gender: must be "male" or "female" (case-insensitive)
     *
     * @param gender input gender
     * @return true if valid
     */
    public static boolean isValidGender(String gender) {
        return gender != null
                && (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female"));
    }

    /**
     * Validate date of birth: - Format: dd/MM/yyyy (VN style) - User must be ≥
     * 13 years old
     *
     * @param dobString input date string in dd/MM/yyyy format
     * @return true if valid format and age >= 13
     */
    public static boolean isValidDob(String dobString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dob = LocalDate.parse(dobString, formatter);
            return Period.between(dob, LocalDate.now()).getYears() >= 13;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}
