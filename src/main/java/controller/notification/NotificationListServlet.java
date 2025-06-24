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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try ( Connection conn = new DBContext().getConnection()) {
            NotificationDAO dao = new NotificationDAO(conn);

            String search = request.getParameter("search");
            int page = 1;
            int recordsPerPage = 5;

            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            List<NotificationDTO> notifications;
            int totalRecords;

            if (search != null && !search.trim().isEmpty()) {
                notifications = dao.searchByTitle(search.trim()); // nếu cần phân trang search, cần thêm dao method khác
                totalRecords = notifications.size();
            } else {
                int offset = (page - 1) * recordsPerPage;
                notifications = dao.getNotificationsWithPaging(offset, recordsPerPage);
                totalRecords = dao.getTotalNotificationCount();
            }

            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

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
