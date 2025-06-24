package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * AccountEditServlet
 *
 * Handles the admin functionality for editing an existing user account.
 * Supports GET to load the edit form and POST to process updates.
 * 
 * URL mapping: /admin/account/edit
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountEditServlet", urlPatterns = {"/admin/account/edit"})
public class AccountEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO dao = new AccountDAO();

    /**
     * Handles GET request to load account data and forward to edit form.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

    /**
     * Handles POST request to process account update submitted by admin.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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
