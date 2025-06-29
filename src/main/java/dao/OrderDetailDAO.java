package dao;

import dto.OrderDetailDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import util.DBContext;

public class OrderDetailDAO {

    public List<OrderDetailDTO> getOrderDetailsByOrderID(int orderID) {
        List<OrderDetailDTO> list = new ArrayList<>();
        String sql = "SELECT ODID, OrderID, BookID, Quantity, TotalPrice FROM OrderDetail WHERE OrderID = ?";

        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetailDTO detail = new OrderDetailDTO();
                detail.setODID(rs.getInt("ODID"));
                detail.setOrderID(rs.getInt("OrderID"));
                detail.setBookID(rs.getInt("BookID"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setTotalPrice(rs.getDouble("TotalPrice"));
                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addOrderDetail(OrderDetailDTO detail) {
        String sql = "INSERT INTO OrderDetail (orderID, bookID, quantity, totalPrice) VALUES (?, ?, ?, ?)";
        try ( Connection conn = DBContext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, detail.getOrderID());
            ps.setInt(2, detail.getBookID());   
            ps.setInt(3, detail.getQuantity());
            ps.setDouble(4, detail.getTotalPrice());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
