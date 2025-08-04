package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import util.MailUtil;
import util.PasswordUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * AccountAddServlet – Handles account creation by Admin (role 0 only). Only
 * Admin or Staff Manager roles (0, 1) can be created.
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountAddServlet", urlPatterns = {"/admin/account/add"})
public class AccountAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO currentUser = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        // ✅ Admin check
        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // ✅ Get and trim input
        String username = request.getParameter("username").trim();
        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        String dobRaw = request.getParameter("dob").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String address = request.getParameter("address").trim();
        int role = safeParseInt(request.getParameter("role"));
        int sex = safeParseInt(request.getParameter("sex"));

        // ✅ Validate input
        if (!ValidationUtil.isValidUsername(username)
                || !ValidationUtil.isValidName(firstName)
                || !ValidationUtil.isValidName(lastName)
                || !ValidationUtil.isValidEmail(email)
                || !ValidationUtil.isValidPhone(phone)
                || !ValidationUtil.isValidAddress(address)
                || !ValidationUtil.isValidGender(String.valueOf(sex))
                || !ValidationUtil.isValidDob(dobRaw)) {

            request.setAttribute("error", "Invalid input. Please check all fields.");
            bindAccountToRequest(request, username, null, firstName, lastName, null, email, phone, role, address, sex);
            request.setAttribute("dobRaw", dobRaw);
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
            return;
        }

        try {
            // ✅ Only allow roles 0 (Admin) or 1 (Staff Manager)
            if (role != 0 && role != 1) {
                request.setAttribute("error", "Only Admin or Staff Manager roles are allowed.");
                bindAccountToRequest(request, username, null, firstName, lastName, null, email, phone, role, address, sex);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
                return;
            }

            // ✅ Username must be unique
            if (accountDAO.findByUsername(username) != null) {
                request.setAttribute("error", "Username already exists.");
                bindAccountToRequest(request, username, null, firstName, lastName, null, email, phone, role, address, sex);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
                return;
            }

            // ✅ Parse DOB
            LocalDate dobLocal = LocalDate.parse(dobRaw, formatter);
            Date dob = Date.valueOf(dobLocal);

            // ✅ Generate random password and hash
            String rawPassword = PasswordUtil.generateRandomPassword(10);
            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

            // ✅ Create account object with code = null (no OTP)
            AccountDTO account = new AccountDTO(
                    username, hashedPassword, firstName, lastName,
                    dob, email, phone, role, address, sex, 1, null
            );

            // ✅ Insert account
            if (accountDAO.addAccountByAdmin(account)) {
                try {
                    MailUtil.sendPassword(email, username, rawPassword);
                    session.setAttribute("message", "Account \"" + username + "\" has been added successfully.");
                } catch (Exception e) {
                    session.setAttribute("message", "Account added, but failed to send email.");
                    e.printStackTrace();
                }
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to add account. Please try again.");
                bindAccountToRequest(request, username, null, firstName, lastName, dob, email, phone, role, address, sex);
                request.setAttribute("dobRaw", dobRaw);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
            }

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "Invalid date format. Use dd/MM/yyyy.");
            request.setAttribute("dobRaw", dobRaw);
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
        }
    }

    private void bindAccountToRequest(HttpServletRequest request, String username, String password,
            String firstName, String lastName, Date dob, String email, String phone,
            int role, String address, int sex) {
        AccountDTO account = new AccountDTO(username, password, firstName, lastName,
                dob, email, phone, role, address, sex, 1, null);
        request.setAttribute("account", account);
    }

    private int safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles account creation by Admin (roles 0 and 1 only)";
    }
}
