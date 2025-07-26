package util;

import java.security.SecureRandom;
import java.util.*;

/**
 * PasswordUtil – Utility class for generating secure random passwords
 * that meet the required complexity constraints.
 * Ensures:
 * - At least one uppercase letter
 * - At least one lowercase letter
 * - At least one digit
 * - At least one special character
 * 
 * @author
 */
public class PasswordUtil {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@#$!%^&*()-_+=<>?";
    private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a valid random password with at least 8 characters.
     * Ensures complexity requirements are satisfied.
     *
     * @param length Desired password length (minimum 8)
     * @return A secure random password
     * @throws IllegalArgumentException If length < 8
     */
    public static String generateRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }

        List<Character> chars = new ArrayList<Character>();

        // Ensure required character types
        chars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        chars.add(LOWER.charAt(random.nextInt(LOWER.length())));
        chars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        chars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            chars.add(ALL.charAt(random.nextInt(ALL.length())));
        }

        // Shuffle to avoid predictable positions
        Collections.shuffle(chars);

        // Convert to String manually (Java 7 safe)
        StringBuilder sb = new StringBuilder();
        for (Character c : chars) {
            sb.append(c);
        }

        return sb.toString();
    }
}
