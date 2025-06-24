/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.notification;

import dao.NotificationDAO;
import util.DBContext;
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
@WebServlet(name = "NotificationDeleteServlet", urlPatterns = {"/admin/notification/delete"})
public class NotificationDeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = new DBContext().getConnection()) {
            int notID = Integer.parseInt(request.getParameter("id"));
            NotificationDAO dao = new NotificationDAO(conn);
            dao.deleteNotification(notID);

            response.sendRedirect(request.getContextPath() + "/admin/notification/list");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi xoá thông báo: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/notification/list.jsp").forward(request, response);
        }
    }
}
