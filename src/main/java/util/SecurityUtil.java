package util;

import org.mindrot.jbcrypt.BCrypt;

public class SecurityUtil {

    // Hash password với cost = 10
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    // So sánh mật khẩu thường với mật khẩu đã hash
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
