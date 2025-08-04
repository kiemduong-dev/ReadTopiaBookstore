package dao;

import dto.CartDTO;
import util.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NGUYEN THAI ANH
 */
public class CartDAO {

    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(CartDTO cart) {
        String sql = "INSERT INTO Cart (username, bookID, quantity) VALUES (?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cart.getUsername());
            ps.setInt(2, cart.getBookID());
            ps.setInt(3, cart.getQuantity());

            int result = ps.executeUpdate();
            System.out.println("Added to cart: " + cart.toString());
            return result > 0;

        } catch (Exception e) {
            System.err.println("addToCart error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra xem người dùng đã có book này trong giỏ chưa
    public CartDTO findByUsernameAndBookID(String username, int bookID) {
        String sql = "SELECT * FROM Cart WHERE username = ? AND bookID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setInt(2, bookID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCartFromResultSet(rs);
                }
            }

        } catch (Exception e) {
            System.err.println("findByUsernameAndBookID error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Lấy cart item theo cartID
    public CartDTO findByCartID(int cartID) {
        String sql = "SELECT * FROM Cart WHERE cartID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCartFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("findByCartID error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật số lượng sách trong giỏ
    public boolean updateCart(CartDTO cart) {
        String sql = "UPDATE Cart SET quantity = ? WHERE cartID = ? AND username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cart.getQuantity());
            ps.setInt(2, cart.getCartID());
            ps.setString(3, cart.getUsername());

            int rowsAffected = ps.executeUpdate();
            System.out.println("Updated cart: cartID=" + cart.getCartID()
                    + ", quantity=" + cart.getQuantity()
                    + ", rowsAffected=" + rowsAffected);
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("updateCart error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật số lượng theo username và bookID
    public boolean updateCartQuantity(String username, int bookID, int quantity) {
        String sql = "UPDATE Cart SET quantity = ? WHERE username = ? AND bookID = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setString(2, username);
            ps.setInt(3, bookID);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("updateCartQuantity error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Lấy toàn bộ giỏ hàng của user
    public List<CartDTO> getCartByUsername(String username) {
        List<CartDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Cart WHERE username = ? ORDER BY cartID DESC";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractCartFromResultSet(rs));
                }
            }

            System.out.println("Retrieved " + list.size() + " cart items for user: " + username);
        } catch (Exception e) {
            System.err.println("getCartByUsername error: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Đếm số lượng items trong giỏ
    public int getCartItemCount(String username) {
        String sql = "SELECT SUM(quantity) as total FROM Cart WHERE username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception e) {
            System.err.println("getCartItemCount error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Xóa một mục khỏi giỏ
    public boolean deleteFromCart(int cartID, String username) {
        String sql = "DELETE FROM Cart WHERE cartID = ? AND username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cartID);
            ps.setString(2, username);

            int result = ps.executeUpdate();
            System.out.println("Deleted from cart: cartID=" + cartID + ", result=" + result);
            return result > 0;

        } catch (Exception e) {
            System.err.println("deleteFromCart error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
// Xóa nhiều mục khỏi giỏ dựa trên danh sách cartID

    public boolean deleteMultipleFromCart(List<Integer> cartIDs, String username) {
        if (cartIDs == null || cartIDs.isEmpty()) {
            return false;
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < cartIDs.size(); i++) {
            placeholders.append("?");
            if (i < cartIDs.size() - 1) {
                placeholders.append(",");
            }
        }

        String sql = "DELETE FROM Cart WHERE username = ? AND cartID IN (" + placeholders + ")";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            for (int i = 0; i < cartIDs.size(); i++) {
                ps.setInt(i + 2, cartIDs.get(i));
            }

            int result = ps.executeUpdate();
            System.out.println("✅ Deleted " + result + " cart items for user " + username);
            return result > 0;

        } catch (Exception e) {
            System.err.println("❌ deleteMultipleFromCart error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Xóa toàn bộ giỏ của người dùng
    public boolean clearCartByUsername(String username) {
        String sql = "DELETE FROM Cart WHERE username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            int result = ps.executeUpdate();
            System.out.println("Cleared cart for user: " + username + ", deleted " + result + " items");
            return result > 0;

        } catch (Exception e) {
            System.err.println("clearCartByUsername error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra tồn kho trước khi thêm vào giỏ
    public boolean isStockAvailable(int bookID, int requestedQuantity) {
        String sql = "SELECT bookQuantity FROM Book WHERE bookID = ? AND bookStatus = 1";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int availableStock = rs.getInt("bookQuantity");
                    return availableStock >= requestedQuantity;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ isStockAvailable error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Helper method để extract CartDTO từ ResultSet
    private CartDTO extractCartFromResultSet(ResultSet rs) throws Exception {
        CartDTO cart = new CartDTO();
        cart.setCartID(rs.getInt("cartID"));
        cart.setUsername(rs.getString("username"));
        cart.setBookID(rs.getInt("bookID"));
        cart.setQuantity(rs.getInt("quantity"));
        return cart;
    }

    // Lấy tổng giá trị giỏ hàng
    public double getCartTotal(String username) {
        String sql = "SELECT SUM(c.quantity * b.bookPrice) as total "
                + "FROM Cart c JOIN Book b ON c.bookID = b.bookID "
                + "WHERE c.username = ?";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            System.err.println("getCartTotal error: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
}
