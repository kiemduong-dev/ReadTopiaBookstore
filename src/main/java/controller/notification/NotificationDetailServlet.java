/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.notification;

import dao.NotificationDAO;
import util.DBContext;
import dto.NotificationDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationDetailServlet", urlPatterns = {"/admin/notification/detail"})
public class NotificationDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = new DBContext().getConnection()) {
            String idRaw = request.getParameter("id");
            int id = Integer.parseInt(idRaw);

            NotificationDAO dao = new NotificationDAO(conn);
            NotificationDTO notification = dao.getNotificationById(id);

            if (notification == null) {
                request.setAttribute("error", "Không tìm thấy thông báo.");
            } else {
                request.setAttribute("notification", notification);
            }

            request.getRequestDispatcher("/WEB-INF/view/admin/notification/detail.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải chi tiết thông báo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/detail.jsp").forward(request, response);
        }
    }
}
