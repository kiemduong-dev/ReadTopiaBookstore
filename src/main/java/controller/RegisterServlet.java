package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import util.MailUtil;

import java.io.IOException;
import java.sql.Date;
import java.util.Random;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");
        String gender = request.getParameter("sex");

        try {
            // Kiểm tra thiếu trường
            if (username == null || password == null || confirmPassword == null || email == null
                    || firstName == null || lastName == null || dobStr == null) {
                request.setAttribute("error", "⚠️ Please fill in all required fields.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Kiểm tra xác nhận mật khẩu
            if (!password.equals(confirmPassword)) {
                request.setAttribute("error", "⚠️ Passwords do not match.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Parse ngày sinh
            Date dob;
            try {
                dob = Date.valueOf(dobStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "⚠️ Invalid date format.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            int sex = "male".equalsIgnoreCase(gender) ? 1 : 0;

            AccountDAO dao = new AccountDAO();

            // Kiểm tra tồn tại username/email
            if (dao.getAccountByUsername(username) != null || dao.findByEmail(email) != null) {
                request.setAttribute("error", "⚠️ Username or email already exists.");
                request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng pendingAccount chưa hash mật khẩu
            AccountDTO pendingAcc = new AccountDTO(
                    username, password, firstName, lastName,
                    dob, email, phone,
                    1,      // role = customer
                    address,
                    sex,
                    1,      // accStatus = active
                    null    // otp code
            );

            // Tạo mã OTP và gửi email
            String otp = generateOTP();
            MailUtil.sendOTP(email, otp);

            // Lưu session để dùng sau xác minh
            session.setAttribute("otp", otp);
            session.setAttribute("otpPurpose", "register");
            session.setAttribute("pendingAccount", pendingAcc);

            // Chuyển hướng đến trang nhập mã OTP
            request.getRequestDispatcher("/WEB-INF/view/account/verify-otp-register.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Registration failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/account/register.jsp").forward(request, response);
        }
    }
}
