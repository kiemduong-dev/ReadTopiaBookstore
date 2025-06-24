/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ADMIN
 */
import org.mindrot.jbcrypt.BCrypt;

public class HashGenerator {

    public static void main(String[] args) {
        String password = "1";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        System.out.println("Hashed password: " + hashed);
    }
}
