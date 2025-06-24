package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "HomePageServlet", urlPatterns = {"/home"})
public class HomePageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        // Không set attribute gì vì chưa có dữ liệu sách
        request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
    }
}
