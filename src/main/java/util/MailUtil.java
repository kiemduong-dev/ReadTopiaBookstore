package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * MailUtil - Utility class for sending OTP emails using Gmail SMTP. This class
 * provides a static method to send HTML-formatted OTP messages.
 *
 * @author CE181518 Dương An Kiếm
 */
public class MailUtil {

    // Sender's email and app-specific password (should be moved to environment variables in production)
    private static final String FROM_EMAIL = "duongankiemdz@gmail.com";
    private static final String APP_PASSWORD = "vfviafcvijutxqmz";

    /**
     * Sends an OTP (One-Time Password) email to the specified recipient.
     *
     * @param toEmail The recipient's email address.
     * @param otp The 6-digit one-time password.
     * @throws MessagingException If an error occurs while sending the email.
     * @throws UnsupportedEncodingException If encoding the sender name fails.
     */
    public static void sendOtp(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException {

        // Validate email and OTP inputs
        if (toEmail == null || toEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be null or empty.");
        }
        if (otp == null || otp.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP cannot be null or empty.");
        }

        try {
            // Set mail server properties for Gmail SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.debug", "false");

            // Create authenticated mail session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            // Construct email message with HTML content
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "ReadTopia Support"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your ReadTopia OTP Code");
            message.setContent(buildOtpEmailContent(otp), "text/html; charset=utf-8");

            // Send the email
            Transport.send(message);

        } catch (MessagingException e) {
            // Re-throw specific exception for upper layers to handle
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (Exception e) {
            // Wrap unexpected exceptions
            throw new MessagingException("Unexpected error occurred while sending OTP email.", e);
        }
    }

    /**
     * Builds the HTML content for the OTP email.
     *
     * @param otp The one-time password to include in the email.
     * @return The HTML content as a string.
     */
    private static String buildOtpEmailContent(String otp) {
        return "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f6f6f6; padding: 20px; }"
                + ".container { background-color: #fff; padding: 20px; border-radius: 10px; max-width: 500px; margin: auto; box-shadow: 0 0 10px rgba(0,0,0,0.1); }"
                + ".otp { font-size: 32px; font-weight: bold; color: #4caf50; letter-spacing: 3px; margin: 20px 0; text-align: center; }"
                + ".footer { font-size: 12px; color: #888; margin-top: 30px; }"
                + ".warning { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 10px; border-radius: 5px; margin: 15px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h2>OTP Verification</h2>"
                + "<p>Hello,</p>"
                + "<p>You have requested to create an account on <strong>ReadTopia</strong>.</p>"
                + "<p>Your verification code is:</p>"
                + "<div class='otp'>" + otp + "</div>"
                + "<div class='warning'>"
                + "<strong>Important:</strong><br>"
                + "• This code will expire in 5 minutes.<br>"
                + "• Do not share this code with anyone.<br>"
                + "• If you did not request this, please ignore this email."
                + "</div>"
                + "<div class='footer'>Thank you,<br>ReadTopia Team</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
/**
 * Sends a welcome email with username and random password.
 *
 * @param toEmail Recipient email
 * @param username The staff's username
 * @param password The staff's password (generated)
 * @throws MessagingException If email fails
 * @throws UnsupportedEncodingException If sender encoding fails
 */
public static void sendPassword(String toEmail, String username, String password)
        throws MessagingException, UnsupportedEncodingException {

    if (toEmail == null || toEmail.trim().isEmpty() || password == null || password.trim().isEmpty()) {
        throw new IllegalArgumentException("Email or password cannot be empty.");
    }

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(FROM_EMAIL, "ReadTopia HR"));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
    message.setSubject("Your ReadTopia Account Credentials");
    message.setContent(buildPasswordEmailContent(username, password), "text/html; charset=utf-8");

    Transport.send(message);
}

/**
 * Builds the HTML content with username and password
 */
private static String buildPasswordEmailContent(String username, String password) {
    return "<html>"
            + "<head><style>"
            + "body { font-family: Arial; background-color: #f4f4f4; padding: 20px; }"
            + ".container { background: #fff; padding: 20px; border-radius: 10px; max-width: 500px; margin: auto; }"
            + ".info { font-size: 18px; margin: 15px 0; }"
            + ".footer { font-size: 12px; color: #888; margin-top: 30px; }"
            + "</style></head>"
            + "<body><div class='container'>"
            + "<h2>Welcome to ReadTopia 🎉</h2>"
            + "<p>Your account has been successfully created.</p>"
            + "<div class='info'><strong>Username:</strong> " + username + "</div>"
            + "<div class='info'><strong>Password:</strong> " + password + "</div>"
            + "<p>Please log in and change your password as soon as possible.</p>"
            + "<div class='footer'>Thank you,<br/>ReadTopia Team</div>"
            + "</div></body></html>";
}

}