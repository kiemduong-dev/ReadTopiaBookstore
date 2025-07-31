/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.StaffDAO;
import dao.VoucherDAO;
import dao.VoucherLogDAO;
import dto.AccountDTO;
import dto.VoucherDTO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 *
 * @author default
 */
@WebServlet(name = "VoucherAddServlet", urlPatterns = {"/admin/voucher/add"})
public class VoucherAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            VoucherDAO dao = new VoucherDAO();
            VoucherLogDAO logDao = new VoucherLogDAO();
            StaffDAO staffDao = new StaffDAO();

            HttpSession session = request.getSession();
            AccountDTO account = (AccountDTO) session.getAttribute("account");
            String username = account.getUsername();
            int role = account.getRole();
            int staffID = staffDao.getStaffIDByUsername(username);

            // Lấy dữ liệu từ form
            String vouName = request.getParameter("vouName");
            String vouCode = request.getParameter("vouCode");
            double discount = Double.parseDouble(request.getParameter("discount"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Kiểm tra hợp lệ
            LocalDate today = LocalDate.now();
            List<String> errors = new ArrayList<>();

            if (startDate.toLocalDate().isBefore(today)) {
                errors.add("The start date must be from today onwards!");
            }

            if (endDate.toLocalDate().isBefore(today)) {
                errors.add("The end date must be from today onwards!");
            }

            if (!endDate.toLocalDate().isAfter(startDate.toLocalDate())) {
                errors.add("The end date must be after the start date!");
            }

            if (discount <= 0 || discount >= 100) {
                errors.add("Discount percentage must be greater than 0 and less than 100!");
            }

            if (quantity < 0) {
                errors.add("The quantity cannot be negative!");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("vouName", vouName);
                request.setAttribute("vouCode", vouCode);
                request.setAttribute("discount", discount);
                request.setAttribute("startDate", startDate);
                request.setAttribute("endDate", endDate);
                request.setAttribute("quantity", quantity);
                request.getRequestDispatcher("/WEB-INF/view/admin/voucher/add.jsp").forward(request, response);
                return;
            }

            // Tạo đối tượng DTO
            VoucherDTO voucher = new VoucherDTO();
            voucher.setVouName(vouName);
            voucher.setVouCode(vouCode);
            voucher.setDiscount(discount);
            voucher.setStartDate(startDate);
            voucher.setEndDate(endDate);
            voucher.setQuantity(quantity);
            voucher.setCreatedBy(username); // đúng kiểu String

            if (role == 0) {
                voucher.setVouStatus(1); // Approved
                voucher.setApprovedBy(username); // đúng kiểu String
            } else {
                voucher.setVouStatus(0); // Pending
                voucher.setApprovedBy(null);
            }

            int newID = dao.addVoucher(voucher);

            if (newID > 0) {
                logDao.insertLog(newID, username, 1); // action 1 = create
            }

            response.sendRedirect("list");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Error adding voucher: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/voucher/add.jsp").forward(request, response);
    }
}
