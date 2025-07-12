package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * AccountEditServlet – Handles editing of user accounts by admin. Only Admin
 * role is allowed to perform this action. Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/admin/account/edit"})
public class AccountEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO dao = new AccountDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        AccountDTO currentUser = (AccountDTO) session.getAttribute("account");

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = request.getParameter("username");

        if (username == null || username.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        AccountDTO acc = dao.getAccountByUsername(username.trim());

        if (acc == null) {
            response.sendRedirect("list");
            return;
        }

        request.setAttribute("account", acc);
        request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        AccountDTO currentUser = (AccountDTO) session.getAttribute("account");

        if (currentUser == null || currentUser.getRole() != 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

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

            if (role < 0 || role > 4) {
                request.setAttribute("error", "Invalid role value.");
                request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
                return;
            }

            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;

            AccountDTO acc = new AccountDTO();
            acc.setUsername(username);
            acc.setFirstName(firstName);
            acc.setLastName(lastName);
            acc.setEmail(email);
            acc.setPhone(phone);
            acc.setAddress(address);
            acc.setRole(role);
            acc.setSex(sex);
            acc.setDob(dob);

            boolean updated = dao.updateAccountByAdmin(acc);

            if (updated) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update account. Please try again.");
                request.setAttribute("account", acc);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input. Please check the fields.");
            request.getRequestDispatcher("/WEB-INF/view/admin/account/edit.jsp").forward(request, response);
        }
    }
}
