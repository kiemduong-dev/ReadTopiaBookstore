/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.notification;

import dao.NotificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ngtua
 */
@WebServlet(name = "NotificationDeleteServlet", urlPatterns = {"/admin/notification/delete"})
public class NotificationDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int notID = Integer.parseInt(request.getParameter("notID")); // Lưu ý: đúng tên input hidden trong form
            NotificationDAO dao = new NotificationDAO();
            dao.deleteNotification(notID);
            HttpSession session = request.getSession();

            session.setAttribute("successMessage", "Notification deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/admin/notification/list");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xoá thông báo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        }
    }

}
