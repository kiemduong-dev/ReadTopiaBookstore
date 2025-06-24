package controller;

import dao.AccountDAO;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "EditProfileServlet", urlPatterns = {"/edit-profile"})
public class EditProfileServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        AccountDTO acc = (session != null) ? (AccountDTO) session.getAttribute("account") : null;

        if (acc == null) {
            response.sendRedirect("login");
            return;
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("user", acc);
            request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
            return;
        }

        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String dobStr = request.getParameter("dob");
            String gender = request.getParameter("sex");

            int sex = "male".equalsIgnoreCase(gender) ? 1 : 0;

            if (firstName == null || email == null || dobStr == null || firstName.isEmpty() || email.isEmpty() || dobStr.isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields.");
            } else {
                Date dob = Date.valueOf(dobStr);

                // Cập nhật dữ liệu
                acc.setFirstName(firstName);
                acc.setLastName(lastName);
                acc.setEmail(email);
                acc.setPhone(phone);
                acc.setAddress(address);
                acc.setSex(sex);
                acc.setDob(dob);

                AccountDAO dao = new AccountDAO();
                boolean success = dao.updateProfile(acc);

                if (success) {
                    session.setAttribute("account", acc); // update lại session
                    request.setAttribute("success", "Profile updated successfully.");
                } else {
                    request.setAttribute("error", "❌ Failed to update profile. Please try again.");
                }
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "❌ Invalid date format.");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ An unexpected error occurred.");
        }

        request.setAttribute("user", acc);
        request.getRequestDispatcher("/WEB-INF/view/account/editProfile.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles editing and updating user profile information.";
    }
}
