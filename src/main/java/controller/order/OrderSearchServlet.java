    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
     */
    package controller.order;

    import dao.OrderDAO;
    import dto.OrderDTO;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import jakarta.servlet.http.HttpSession;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    /**
     *
     * @author NGUYEN THAI ANH
     */
    @WebServlet("/order/search")
    public class OrderSearchServlet extends HttpServlet {

        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            response.setContentType("text/html;charset=UTF-8");

            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("username");
            Integer role = (Integer) session.getAttribute("role");

            if (username == null || role == null || role != 0) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
            String searchUsername = request.getParameter("username");
            String bookIDStr = request.getParameter("bookID");

            OrderDAO dao = new OrderDAO();
            List<OrderDTO> orders = new ArrayList<>();

            try {
                if (searchUsername != null && !searchUsername.trim().isEmpty()) {
                    // Use existing method getOrderHistoryByUsername
                    orders = dao.getOrderHistoryByUsername(searchUsername);
                } else if (bookIDStr != null && !bookIDStr.trim().isEmpty()) {
                    // Convert String to int for bookID search
                    int bookID = Integer.parseInt(bookIDStr);
                    orders = dao.getOrdersByBookID(bookID);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid book ID format");
            } catch (Exception e) {
                System.err.println("OrderSearchServlet error: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("error", "Search failed");
            }

            request.setAttribute("orders", orders);
            request.setAttribute("searchUsername", searchUsername);
            request.setAttribute("bookID", bookIDStr);
            request.getRequestDispatcher("/WEB-INF/view/order/search.jsp").forward(request, response);
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            processRequest(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            processRequest(request, response);
        }

        @Override
        public String getServletInfo() {
            return "Handles staff searching for order history by username or book ID.";
        }
    }
