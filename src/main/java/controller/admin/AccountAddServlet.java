package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

/**
 * AccountAddServlet
 *
 * Handles the functionality for an admin to add a new user account.
 * Supports GET to display the add form and POST to handle form submission.
 * 
 * URL mapping: /admin/account/add
 * 
 * Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountAddServlet", urlPatterns = {"/admin/account/add"})
public class AccountAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Handles GET request to display the account creation form.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
    }

    /**
     * Handles POST request to process submitted form and create account.
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

            // Default password is "1" (hashed in DAO)
            String rawPassword = "1";

            AccountDTO acc = new AccountDTO(
                    username, rawPassword, firstName, lastName, dob,
                    email, phone, role, address, sex, 1, null // accStatus = 1 (active)
            );

            AccountDAO dao = new AccountDAO();
            boolean added = dao.addAccount(acc);

            if (added) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to add account. Username or email may already exist.");
                request.setAttribute("account", acc);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input. Please check the form.");
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
        }
    }

    /**
     * Returns servlet description.
     *
     * @return a brief description of this servlet
     */
    @Override
    public String getServletInfo() {
        return "Admin adds a new user account.";
    }
}
