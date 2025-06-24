package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.SupplierDTO;
import util.DBContext;
import static util.DBContext.getConnection;

public class SupplierDAO {

    public List<SupplierDTO> getAllSuppliers() {
        List<SupplierDTO> list = new ArrayList<>();
        String query = "SELECT * FROM Supplier";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query);  ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                SupplierDTO s = new SupplierDTO(
                        rs.getInt("supID"),
                        rs.getString("supName"),
                        rs.getString("supPassword"),
                        rs.getString("supEmail"),
                        rs.getString("supPhone"),
                        rs.getString("supAddress"),
                        rs.getInt("supStatus") == 1,
                        rs.getString("supImage")
                );
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println("getAllSuppliers: " + e);
        }
        return list;
    }

    public SupplierDTO getSupplierById(int id) {
        String query = "SELECT * FROM Supplier WHERE supID = ?";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new SupplierDTO(
                            rs.getInt("supID"),
                            rs.getString("supName"),
                            rs.getString("supPassword"),
                            rs.getString("supEmail"),
                            rs.getString("supPhone"),
                            rs.getString("supAddress"),
                            rs.getInt("supStatus") == 1,
                            rs.getString("supImage")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("getSupplierById: " + e);
        }
        return null;
    }

    public void addSupplier(String name, String password, String email, String phone, String address, boolean status, String image) {
        String query = "INSERT INTO Supplier (supName, supPassword, supEmail, supPhone, supAddress, supStatus, supImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, password);
            pst.setString(3, email); 
            pst.setString(4, phone);
            pst.setString(5, address);
            pst.setInt(6, status ? 1 : 0);
            pst.setString(7, image);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("addSupplier: " + e);
        }
    }

    public void editSupplier(SupplierDTO supplier) {
        String query = "UPDATE Supplier SET supName = ?, supPassword = ?, supEmail = ?, supPhone = ?, supAddress = ?, supStatus = ?, supImage = ? WHERE supID = ?";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, supplier.getSupName());
            pst.setString(2, supplier.getPassword());
            pst.setString(3, supplier.getEmail());
            pst.setString(4, supplier.getPhone());
            pst.setString(5, supplier.getAddress());
            pst.setInt(6, supplier.isStatus() ? 1 : 0);
            pst.setString(7, supplier.getImage());
            pst.setInt(8, supplier.getId());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("editSupplier: " + e);
        }
    }

    public void deleteSupplier(int id) {
        String query = "DELETE FROM Supplier WHERE supID = ?";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteSupplier: " + e);
        }
    }

    public List<SupplierDTO> getSupplierBySupplierName(String value) {
        List<SupplierDTO> list = new ArrayList<>();
        String query = "SELECT * FROM Supplier WHERE supName LIKE ?";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, "%" + value + "%");
            try ( ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    SupplierDTO s = new SupplierDTO(
                            rs.getInt("supID"),
                            rs.getString("supName"),
                            rs.getString("supPassword"),
                            rs.getString("supEmail"),
                            rs.getString("supPhone"),
                            rs.getString("supAddress"),
                            rs.getInt("supStatus") == 1,
                            rs.getString("supImage")
                    );
                    list.add(s);
                }
            }
        } catch (Exception e) {
            System.out.println("getSupplierBySupplierName: " + e);
        }
        return list;
    }

    public int getTotalSupplier() {
        String query = "SELECT COUNT(supID) AS total FROM Supplier";
        try (
                 Connection con = new DBContext().getConnection();  PreparedStatement pst = con.prepareStatement(query);  ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println("getTotalSupplier: " + e);
        }
        return -1;
    }
    
    public int getSupplierIDByName(String name) {
    String sql = "SELECT supID FROM Supplier WHERE supName = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("supID");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
}


    public static void main(String[] args) {
        SupplierDAO dao = new SupplierDAO();
        List<SupplierDTO> suppliers = dao.getAllSuppliers();
        for (SupplierDTO s : suppliers) {
            System.out.println(s);
        }
    }
}
