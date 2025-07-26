package controller.admin;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;


/**
 * AccountAddServlet – Handles Add Account for Admin (only Admin and Seller
 * Staff)
 *
 * @author CE181518 Dương An Kiếm
 */
/**
 * AccountAddServlet – Handles Add Account for Admin (only Admin and Staff roles
 * allowed) Author: CE181518 Dương An Kiếm
 */
@WebServlet(name = "AccountAddServlet", urlPatterns = {"/admin/account/add"})
public class AccountAddServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Handles GET request to display the account creation form.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
    }

    /**
     * Handles POST request to process submitted form and create account.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession();
            AccountDTO currentUser = (AccountDTO) session.getAttribute("account");

            if (currentUser == null || currentUser.getRole() != 0) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String username = request.getParameter("username").trim();
            String firstName = request.getParameter("firstName").trim();
            String lastName = request.getParameter("lastName").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();
            String dobRaw = request.getParameter("dob");
            int role = Integer.parseInt(request.getParameter("role"));
            int sex = Integer.parseInt(request.getParameter("sex"));

            if (role != 0 && role != 1) {
                request.setAttribute("error", "Only Admin and Staff roles are allowed for Add Account.");
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
                return;
            }

            if (accountDAO.findByUsername(username) != null) {
                request.setAttribute("error", "Username already exists.");
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
                return;
            }

            Date dob = (dobRaw != null && !dobRaw.isEmpty()) ? Date.valueOf(dobRaw) : null;
            String defaultPassword = "Mk@123456";

            AccountDTO account = new AccountDTO(
                    username, defaultPassword, firstName, lastName, dob,
                    email, phone, role, address, sex, 1, null
            );

            boolean success = accountDAO.addAccount(account);

            if (success) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to add account. Please try again.");
                request.setAttribute("account", account);
                request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input. Please check the form.");
            request.getRequestDispatcher("/WEB-INF/view/admin/account/add.jsp").forward(request, response);
        }
    }
}