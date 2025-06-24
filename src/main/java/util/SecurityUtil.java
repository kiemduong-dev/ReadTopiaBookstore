package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Security Utility - Provides methods for password hashing and verification
 * using the BCrypt algorithm with a fixed cost factor.
 *
 * This utility helps securely store and validate user passwords by generating
 * strong hashed representations that are computationally expensive to reverse.
 *
 * @author CE181518 Dương An Kiếm
 */
public class SecurityUtil {

    /**
     * Hashes a plaintext password using the BCrypt algorithm.
     *
     * @param password The plaintext password to be hashed
     * @return A hashed password string
     * @throws IllegalArgumentException if the password is null
     */
    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password must not be null.");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    /**
     * Verifies whether the given plaintext password matches the hashed
     * password.
     *
     * @param plainPassword The plaintext password provided by the user
     * @param hashedPassword The previously hashed password from the database
     * @return true if both passwords match, false otherwise
     * @throws IllegalArgumentException if any argument is null
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            throw new IllegalArgumentException("Arguments must not be null.");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
