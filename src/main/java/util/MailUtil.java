package util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Utility class for sending OTP emails with HTML styling.
 */
public class MailUtil {

    private static final String FROM_EMAIL = "duongankiemdz@gmail.com";
    private static final String APP_PASSWORD = "vfviafcvijutxqmz"; // 🔐 Recommend moving to config/env in production

    /**
     * Sends an OTP email to a user using Gmail SMTP.
     *
     * @param toEmail Recipient's email address
     * @param otp One-time password (6 digits)
     * @throws MessagingException If sending fails
     * @throws UnsupportedEncodingException If encoding fails
     */
    public static void sendOTP(String toEmail, String otp) throws MessagingException, UnsupportedEncodingException {
        
        System.out.println("📧 Starting to send OTP to: " + toEmail);
        
        // Validate inputs
        if (toEmail == null || toEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be null or empty");
        }
        if (otp == null || otp.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP cannot be null or empty");
        }

        try {
            // Step 1: Configure Gmail SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            props.put("mail.debug", "false"); // Set to true for debugging

            System.out.println("✅ SMTP properties configured");

            // Step 2: Authenticate Gmail session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            System.out.println("✅ Gmail session created");

            // Step 3: Create the email content
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "ReadTopia Support"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("🔐 Your ReadTopia OTP Code");

            String htmlContent = buildOTPEmailContent(otp);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            System.out.println("✅ Email message prepared");

            // Step 4: Send email
            Transport.send(message);
            System.out.println("✅ OTP email sent successfully to: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ MessagingException while sending OTP: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            System.err.println("❌ UnsupportedEncodingException while sending OTP: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while sending OTP: " + e.getMessage());
            e.printStackTrace();
            throw new MessagingException("Failed to send OTP email", e);
        }
    }

    /**
     * Builds the HTML content for OTP email
     */
    private static String buildOTPEmailContent(String otp) {
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
                + "<h2>📨 OTP Verification</h2>"
                + "<p>Hello,</p>"
                + "<p>You requested to create an account on <strong>ReadTopia</strong>.</p>"
                + "<p>Your verification code is:</p>"
                + "<div class='otp'>" + otp + "</div>"
                + "<div class='warning'>"
                + "<strong>⚠️ Important:</strong><br>"
                + "• This code will expire in 5 minutes<br>"
                + "• Do not share this code with anyone<br>"
                + "• If you didn't request this, please ignore this email"
                + "</div>"
                + "<div class='footer'>Thanks,<br>ReadTopia Team</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}