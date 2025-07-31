/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.VoucherLogDAO;
import dto.VoucherLogDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.DBContext;

/**
 *
 * @author default
 */
@WebServlet(name = "VoucherLogServlet", urlPatterns = {"/admin/voucher/logs"})
public class VoucherLogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String actionParam = request.getParameter("action");
        Integer actionFilter = null;

        // Chuyển đổi action filter nếu có
        if (actionParam != null && !actionParam.isEmpty()) {
            try {
                actionFilter = Integer.parseInt(actionParam);
            } catch (NumberFormatException e) {
                // Giữ nguyên null nếu lỗi
            }
        }

        try  {
            VoucherLogDAO dao = new VoucherLogDAO();
            List<VoucherLogDTO> logs = dao.getAllLogs(actionFilter);

            request.setAttribute("logs", logs);
            request.setAttribute("actionFilter", actionFilter);
            request.getRequestDispatcher("/WEB-INF/view/admin/voucher/logs.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi truy vấn dữ liệu logs.");
        }
    }
}
