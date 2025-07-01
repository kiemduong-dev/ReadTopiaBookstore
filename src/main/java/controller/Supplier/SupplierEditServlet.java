package controller.Supplier;

import dao.SupplierDAO;
import dto.SupplierDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@WebServlet("/admin/supplier/edit")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,     // 2MB
    maxFileSize = 1024 * 1024 * 10,          // 10MB
    maxRequestSize = 1024 * 1024 * 50        // 50MB
)
public class SupplierEditServlet extends HttpServlet {

    private SupplierDAO supplierDAO;

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@gmail\\.com$|^[\\w.-]+@fpt\\.edu\\.vn$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,]+$");

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                SupplierDTO supplier = supplierDAO.getSupplierById(id);
                request.setAttribute("supplier", supplier);
                request.getRequestDispatcher("/WEB-INF/view/admin/supplier/edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // Get form data
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            int status = Integer.parseInt(request.getParameter("status")); 

            SupplierDTO currentSupplier = supplierDAO.getSupplierById(id);
            String currentImage = currentSupplier.getSupImage();

            Part filePart = request.getPart("imageFile");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String imagePath;

            if (fileName != null && !fileName.isEmpty()) {
                String uploadDir = getServletContext().getRealPath("/") + "uploads";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                File file = new File(uploadDir, fileName);
                filePart.write(file.getAbsolutePath());

                imagePath = "uploads/" + fileName;
            } else {
                imagePath = currentImage; 
            }

            StringBuilder errorMsg = new StringBuilder();
            boolean hasError = false;

            if (name == null || !NAME_PATTERN.matcher(name).matches()) {
                hasError = true;
                errorMsg.append("❌ Name must only contain letters and spaces.<br>");
            }

            if (password == null || password.length() < 6) {
                hasError = true;
                errorMsg.append("❌ Password must be at least 6 characters.<br>");
            }

            if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                hasError = true;
                errorMsg.append("❌ Email must end with @gmail.com or @fpt.edu.vn.<br>");
            }

            if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
                hasError = true;
                errorMsg.append("❌ Phone must be 10 digits and start with 0.<br>");
            }

            if (address == null || !ADDRESS_PATTERN.matcher(address).matches()) {
                hasError = true;
                errorMsg.append("❌ Address must not contain numbers or special characters.<br>");
            }

            SupplierDTO supplier = new SupplierDTO(id, name, email, phone, address, password, imagePath, status);

            if (hasError) {
                request.setAttribute("error", errorMsg.toString());
                request.setAttribute("supplier", supplier);
                request.getRequestDispatcher("/WEB-INF/view/admin/supplier/edit.jsp").forward(request, response);
                return;
            }

            supplierDAO.editSupplier(supplier);
            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "❌ An error occurred while updating supplier.");
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/edit.jsp").forward(request, response);
        }
    }
}
