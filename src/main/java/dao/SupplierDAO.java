package dao;

import dto.SupplierDTO;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static util.DBContext.getConnection;

/**
 * Supplier Management – Data access for Supplier table
 * @author G6
 */
public class SupplierDAO {

    /**
     * Get all suppliers (only those still in DB)
     * @return list of suppliers
     */
    public List<SupplierDTO> getAllSuppliers() {
        List<SupplierDTO> list = new ArrayList<>();
        String query = "SELECT * FROM Supplier ORDER BY supID";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                list.add(extractSupplier(rs));
            }
        } catch (Exception e) {
            System.out.println("getAllSuppliers: " + e);
        }
        return list;
    }

    /**
     * Get total number of suppliers
     * @return total count
     */
    public int getTotalSupplierCount() {
        String query = "SELECT COUNT(*) FROM Supplier";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.out.println("getTotalSupplierCount: " + e);
        }
        return 0;
    }

    /**
     * Get supplier by ID
     * @param id supplier ID
     * @return supplier object or null
     */
    public SupplierDTO getSupplierById(int id) {
        String query = "SELECT * FROM Supplier WHERE supID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return extractSupplier(rs);
            }
        } catch (Exception e) {
            System.out.println("getSupplierById: " + e);
        }
        return null;
    }

    /**
     * Search suppliers by name (accent-insensitive)
     * @param name supplier name keyword
     * @return list of matching suppliers
     */
    public List<SupplierDTO> getSupplierBySupplierName(String name) {
        List<SupplierDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE supName COLLATE SQL_Latin1_General_CP1_CI_AI LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractSupplier(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("getSupplierBySupplierName: " + e);
        }
        return list;
    }

    /**
     * Add new supplier
     * @param name supplier name
     * @param password encrypted password
     * @param email email
     * @param phone phone
     * @param address address
     * @param status true = active, false = inactive
     * @param image image URL
     */
    public void addSupplier(String name, String password, String email, String phone, String address, boolean status, String image) {
        String query = "INSERT INTO Supplier (supName, supPassword, supEmail, supPhone, supAddress, supStatus, supImage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

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

    /**
     * Edit supplier info
     * @param supplier supplier object
     */
    public void editSupplier(SupplierDTO supplier) {
        String query = "UPDATE Supplier SET supName = ?, supPassword = ?, supEmail = ?, supPhone = ?, supAddress = ?, supStatus = ?, supImage = ? WHERE supID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, supplier.getSupName());
            pst.setString(2, supplier.getSupPassword());
            pst.setString(3, supplier.getSupEmail());
            pst.setString(4, supplier.getSupPhone());
            pst.setString(5, supplier.getSupAddress());
            pst.setInt(6, supplier.getSupStatus());
            pst.setString(7, supplier.getSupImage());
            pst.setInt(8, supplier.getSupID());
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("editSupplier: " + e);
        }
    }

    /**
     * Hard delete supplier from DB
     * @param id supplier ID
     */
    public void deleteSupplier(int id) {
        String query = "DELETE FROM Supplier WHERE supID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteSupplier: " + e);
        }
    }

   public List<SupplierDTO> getSuppliersByPage(int page, int pageSize) {
    List<SupplierDTO> list = new ArrayList<>();
    String query = "SELECT * FROM Supplier WHERE supStatus = 1 ORDER BY supID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    int offset = (page - 1) * pageSize;

    try (Connection con = DBContext.getConnection();
         PreparedStatement pst = con.prepareStatement(query)) {

        pst.setInt(1, offset);
        pst.setInt(2, pageSize);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(extractSupplier(rs));
            }
        }
    } catch (Exception e) {
        System.out.println("getSuppliersByPage: " + e.getMessage());
    }

    return list;
}

    /**
     * Extract supplier from ResultSet
     * @param rs result set
     * @return SupplierDTO
     * @throws SQLException if error
     */
    private SupplierDTO extractSupplier(ResultSet rs) throws SQLException {
        return new SupplierDTO(
                rs.getInt("supID"),
                rs.getString("supName"),
                rs.getString("supEmail"),
                rs.getString("supPhone"),
                rs.getString("supAddress"),
                rs.getString("supPassword"),
                rs.getString("supImage"),
                rs.getInt("supStatus")
        );
    }


  public int getTotalSuppliers() {
    String query = "SELECT COUNT(*) FROM Supplier WHERE supStatus = 1";
    try (Connection con = DBContext.getConnection();
         PreparedStatement pst = con.prepareStatement(query);
         ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (Exception e) {
        System.out.println("getTotalSuppliers: " + e.getMessage());
    }
    return 0;
}

    
   
     public List<SupplierDTO> getActiveSuppliers() {
    List<SupplierDTO> list = new ArrayList<>();
    String sql = "SELECT supID, supName FROM Supplier WHERE supStatus = 1 ORDER BY supName";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            SupplierDTO s = new SupplierDTO();
            s.setSupID(rs.getInt("supID"));
            s.setSupName(rs.getString("supName"));
            list.add(s);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
     public boolean hasImportStock(int supplierId) {
        String sql = "SELECT COUNT(*) FROM ImportStock WHERE supID = ?";
        try ( Connection con = getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("hasImportStock: " + e);
        }
        return false;
    }
}
