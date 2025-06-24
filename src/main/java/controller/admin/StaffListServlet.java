package controller.admin;

import dao.StaffDAO;
import dto.StaffDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * StaffListServlet
 *
 * Handles the display of staff list for admin, with optional keyword-based
 * searching.
 *
 * @author CE181518 Dương An Kiếm
 */
@WebServlet(name = "StaffListServlet", urlPatterns = {"/admin/staff/list"})
public class StaffListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Process GET or POST request to display staff list.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if servlet-specific error occurs
     * @throws IOException if I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String keyword = request.getParameter("keyword");

        StaffDAO dao = new StaffDAO();
        List<StaffDTO> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Search staff by keyword
            list = dao.searchStaffs(keyword.trim());
            request.setAttribute("keyword", keyword.trim());
        } else {
            // Get all staff if no keyword provided
            list = dao.findAll();
        }

        request.setAttribute("staffs", list);
        request.getRequestDispatcher("/WEB-INF/view/admin/staff/list.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP GET method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Admin - Handles staff list display and keyword search.";
    }
}
