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
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author default
 */
@WebServlet(name = "VoucherEditServlet", urlPatterns = {"/admin/voucher/edit"})
public class VoucherEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int vouID = Integer.parseInt(request.getParameter("vouID"));

        try {
            VoucherDAO dao = new VoucherDAO();
            VoucherDTO voucher = dao.getVoucherByID(vouID);
            request.setAttribute("voucher", voucher);
            request.getRequestDispatcher("/WEB-INF/view/admin/voucher/edit.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error loading voucher for edit");
        }
    }

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
            int staffID = staffDao.getStaffIDByUsername(username);

            int vouID = Integer.parseInt(request.getParameter("vouID"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            List<String> errors = new ArrayList<>();
            LocalDate today = LocalDate.now();

            if (startDate.toLocalDate().isBefore(today)) {
                errors.add("The start date must be from today onwards!");
            }

            if (endDate.toLocalDate().isBefore(today)) {
                errors.add("The end date must be from today onwards!");
            }

            if (!endDate.toLocalDate().isAfter(startDate.toLocalDate())) {
                errors.add("The end date must be after the start date!");
            }

            if (quantity < 0) {
                errors.add("The quantity cannot be negative!");
            }

            if (!errors.isEmpty()) {
                VoucherDTO voucher = new VoucherDTO();
                voucher.setVouID(vouID);
                voucher.setStartDate(startDate);
                voucher.setEndDate(endDate);
                voucher.setQuantity(quantity);

                request.setAttribute("voucher", voucher);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/WEB-INF/view/admin/voucher/edit.jsp").forward(request, response);
                return;
            }

            VoucherDTO voucher = new VoucherDTO();
            voucher.setVouID(vouID);
            voucher.setStartDate(startDate);
            voucher.setEndDate(endDate);
            voucher.setQuantity(quantity);

            dao.updateVoucher(voucher);
            logDao.insertLog(vouID, username, 2); // 2 = edit

            response.sendRedirect("list");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error updating voucher");
        }
    }
}
