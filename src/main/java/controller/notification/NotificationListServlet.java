/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.notification;

import dao.NotificationDAO;
import util.DBContext;
import dto.NotificationDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationListServlet", urlPatterns = {"/admin/notification/list"})
public class NotificationListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try ( Connection conn = DBContext.getConnection()) {
            NotificationDAO dao = new NotificationDAO(conn);

            String search = request.getParameter("search");

            List<NotificationDTO> notifications;

            if (search != null && !search.trim().isEmpty()) {
                notifications = dao.searchByTitle(search.trim());
            } else {
                notifications = dao.getAllNotifications();
            }

            request.setAttribute("notifications", notifications);
            request.setAttribute("search", search);

            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải danh sách thông báo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        }
    }

}
