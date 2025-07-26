package controller.supplier;

import dao.SupplierDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@WebServlet("/admin/supplier/add")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class SupplierAddServlet extends HttpServlet {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@gmail\\.com$|^[\\w.-]+@fpt\\.edu\\.vn$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s,]+$");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/admin/supplier/add.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String statusStr = request.getParameter("status");
        String image = request.getParameter("image");

        boolean hasError = false;

        // Lưu error message theo field
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            request.setAttribute("nameError", "❌ Name must only contain letters and spaces.");
            hasError = true;
        }
        if (password == null || password.length() < 6) {
            request.setAttribute("passwordError", "❌ Password must be at least 6 characters.");
            hasError = true;
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            request.setAttribute("emailError", "❌ Invalid email format.");
            hasError = true;
        }
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            request.setAttribute("phoneError", "❌ Phone must be 10 digits and start with 0.");
            hasError = true;
        }
        if (address == null || !ADDRESS_PATTERN.matcher(address).matches()) {
            request.setAttribute("addressError", "❌ Address must only contain letters, commas and spaces.");
            hasError = true;
        }

        // Giữ lại dữ liệu nếu có lỗi
        request.setAttribute("name", name);
        request.setAttribute("password", password);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
        request.setAttribute("status", statusStr);
        request.setAttribute("image", image);

        if (hasError) {
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/add.jsp").forward(request, response);
            return;
        }

        try {
            // Upload file
            Part imagePart = request.getPart("imageFile");
            String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String storedFileName = System.currentTimeMillis() + "_" + fileName;
            String imagePath = "uploads/" + storedFileName;
            imagePart.write(uploadDir + File.separator + storedFileName);

            // Lưu vào DB
            boolean status = Boolean.parseBoolean(statusStr);
            SupplierDAO dao = new SupplierDAO();
            dao.addSupplier(name, password, email, phone, address, status, imagePath);

            response.sendRedirect(request.getContextPath() + "/admin/supplier/list");
        } catch (Exception e) {
            request.setAttribute("error", "Upload failed: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/admin/supplier/add.jsp").forward(request, response);
        }
    }
}
