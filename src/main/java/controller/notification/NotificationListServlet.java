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
import java.util.List;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationListServlet", urlPatterns = {"/admin/notification/list"})
public class NotificationListServlet extends HttpServlet {

    private final int PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            NotificationDAO dao = new NotificationDAO();

            String search = request.getParameter("search");
            String pageParam = request.getParameter("page");
            int page = 1;
            if (pageParam != null && pageParam.matches("\\d+")) {
                page = Integer.parseInt(pageParam);
            }

            List<NotificationDTO> notifications;
            int totalNotifications = dao.countNotifications();
            int totalPages = (int) Math.ceil((double) totalNotifications / PAGE_SIZE);

            if (search != null && !search.trim().isEmpty()) {
                // Nếu có search, không phân trang
                notifications = dao.searchByTitle(search.trim());
            } else {
                int offset = (page - 1) * PAGE_SIZE;
                notifications = dao.getAllNotifications(offset, PAGE_SIZE);
            }

            request.setAttribute("notifications", notifications);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("search", search);

            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi tải danh sách thông báo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        }
    }
}
