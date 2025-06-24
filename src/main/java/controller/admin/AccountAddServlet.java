package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * Servlet that handles the functionality for an admin to add a new account.
 * Supports both GET (display form) and POST (handle submission).
 */
@WebServlet(name = "AccountAddServlet", urlPatterns = {"/admin/account/add"})
public class AccountAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            String username = request.getParameter("username").trim();
            String firstName = request.getParameter("firstName").trim();
            String lastName = request.getParameter("lastName").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();
            String dobRaw = request.getParameter("dob");
            int role = Integer.parseInt(request.getParameter("role"));
            int sex = Integer.parseInt(request.getParameter("sex"));

            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;

            // Mật khẩu mặc định là "1" (hash trong DAO)
            String rawPassword = "1";

            AccountDTO acc = new AccountDTO(username, rawPassword, firstName, lastName, dob,
                    email, phone, role, address, sex, 1, null); // accStatus = 1

            AccountDAO dao = new AccountDAO();
            boolean added = dao.addAccount(acc);

            if (added) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "❌ Failed to add account. Username or email may already exist.");
                request.setAttribute("account", acc);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ Invalid input. Please check the form.");
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Admin adds a new user account.";
    }
}
