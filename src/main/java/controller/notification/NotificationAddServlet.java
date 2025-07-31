/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.notification;

import dao.NotificationDAO;
import dto.NotificationDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationAddServlet", urlPatterns = {"/admin/notification/add"})
public class NotificationAddServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/notification/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String title = request.getParameter("title");
            int receiver = Integer.parseInt(request.getParameter("receiver"));
            String description = request.getParameter("description");

            int staffID = 1;
            Timestamp sqlTimestamp = new Timestamp(new Date().getTime()); // ✅ chứa ngày + giờ

            NotificationDTO noti = new NotificationDTO(0, staffID, title, receiver, description, sqlTimestamp);
            NotificationDAO dao = new NotificationDAO();
            dao.addNotification(noti);

            response.sendRedirect(request.getContextPath() + "/admin/notification/list");
        } catch (Exception e) {
            request.setAttribute("error", "Thêm thông báo thất bại: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/add.jsp").forward(request, response);
        }
    }
}
