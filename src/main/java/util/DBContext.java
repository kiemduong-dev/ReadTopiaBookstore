package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBContext - Cung cấp phương thức kết nối SQL Server.
 *
 * Đây là class tiện ích để tạo kết nối với cơ sở dữ liệu SQL Server.
 * 
 * <p><b>CHÚ Ý:</b> Trong môi trường thực tế, không nên lưu user/pass trong mã nguồn.
 * Hãy sử dụng biến môi trường hoặc file cấu hình bảo mật.</p>
 * 
 * @author 
 */
public class DBContext {

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ReadTopia;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa"; // TODO: Thay thế nếu cần
    private static final String DB_PASSWORD = "123"; // TODO: Thay thế nếu cần

    /**
     * Lấy kết nối đến SQL Server
     * 
     * @return Connection đã kết nối
     * @throws SQLException nếu xảy ra lỗi khi kết nối
     * @throws ClassNotFoundException nếu không tìm thấy driver JDBC
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Phương thức kiểm tra kết nối khi chạy riêng
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Kết nối thành công đến cơ sở dữ liệu.");
            } else {
                System.out.println("Không thể kết nối.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
