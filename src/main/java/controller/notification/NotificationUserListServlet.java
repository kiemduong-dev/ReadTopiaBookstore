/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.notification;

import dao.NotificationDAO;
import dto.NotificationDTO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;
import util.DBContext;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationUserListServlet", urlPatterns = {"/user/notification/list"})
public class NotificationUserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer role = (Integer) session.getAttribute("role");

        if (role == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            NotificationDAO dao = new NotificationDAO();
            List<NotificationDTO> notifications = dao.getNotificationsForRole(role);

            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/WEB-INF/view/user/notification/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error loading notifications: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/user/notification/list.jsp").forward(request, response);
        }
    }
}
