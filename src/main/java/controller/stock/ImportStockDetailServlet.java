package controller.stock;

import dao.ImportStockDAO;
import dto.ImportStockDetailDTO;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/ImportStockDetailServlet")
public class ImportStockDetailServlet extends HttpServlet {
    private ImportStockDAO dao;

    @Override
    public void init() {
        dao = new ImportStockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int isid = Integer.parseInt(request.getParameter("isid"));
            List<ImportStockDetailDTO> details = dao.getImportStockDetailsByISID(isid);
            request.setAttribute("details", details);
            request.setAttribute("isid", isid);
            request.getRequestDispatcher("/WEB-INF/view/admin/stock/detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
