package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "StaffEditServlet", urlPatterns = {"/admin/staff/edit"})
public class StaffEditServlet extends HttpServlet {

    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("staffID");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("list");
            return;
        }

        try {
            int staffID = Integer.parseInt(idParam.trim());
            StaffDTO staff = staffDAO.getStaffByID(staffID);

            if (staff == null || (staff.getRole() != 2 && staff.getRole() != 3)) {
                response.sendRedirect("list");
                return;
            }

            request.setAttribute("staff", staff);
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int staffID = Integer.parseInt(request.getParameter("staffID"));
            String username = request.getParameter("username").trim();
            String firstName = request.getParameter("firstName").trim();
            String lastName = request.getParameter("lastName").trim();
            String dobParam = request.getParameter("dob");
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            int sex = Integer.parseInt(request.getParameter("sex"));
            int role = Integer.parseInt(request.getParameter("role"));
            String address = request.getParameter("address").trim();

            if (role != 2 && role != 3) {
                request.setAttribute("error", "Only Seller Staff or Warehouse Staff roles are allowed.");

                StaffDTO staff = new StaffDTO();
                staff.setStaffID(staffID);
                staff.setUsername(username);
                staff.setFirstName(firstName);
                staff.setLastName(lastName);
                staff.setDob(dobParam != null && !dobParam.isEmpty() ? Date.valueOf(dobParam) : null);
                staff.setEmail(email);
                staff.setPhone(phone);
                staff.setSex(sex);
                staff.setRole(role);
                staff.setAddress(address);

                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
                return;
            }

            StaffDTO staff = new StaffDTO();
            staff.setStaffID(staffID);
            staff.setUsername(username);
            staff.setFirstName(firstName);
            staff.setLastName(lastName);
            staff.setDob(dobParam != null && !dobParam.isEmpty() ? Date.valueOf(dobParam) : null);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setSex(sex);
            staff.setRole(role);
            staff.setAddress(address);
            staff.setAccStatus(1);

            boolean updated = staffDAO.updateStaff(staff);

            if (updated) {
                response.sendRedirect("list");
            } else {
                request.setAttribute("error", "Failed to update staff.");
                request.setAttribute("staff", staff);
                request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid input. Please check all fields.");
            request.getRequestDispatcher("/WEB-INF/view/admin/staff/edit.jsp").forward(request, response);
        }
    }
}
