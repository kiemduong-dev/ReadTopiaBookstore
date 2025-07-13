package dao;

import dto.*;
import java.sql.*;
import java.util.*;
import static util.DBContext.getConnection;

public class ImportStockDAO {

    /**
     * Insert a full import stock record with its details.
     */
    public boolean insertFullImportStock(ImportStockDTO stock, List<ImportStockDetailDTO> details)
            throws SQLException, ClassNotFoundException {
        String insertStockSQL = "INSERT INTO ImportStock (supID, importDate, ISStatus, staffID) VALUES (?, ?, ?, ?)";
        String insertDetailSQL = "INSERT INTO ImportStockDetail (bookID, ISID, ISDQuantity, importPrice) VALUES (?, ?, ?, ?)";
        String updateStockSQL = "UPDATE Book SET bookQuantity = bookQuantity + ? WHERE bookID = ?";

        try ( Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            int isid;
            try ( PreparedStatement ps = conn.prepareStatement(insertStockSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, stock.getSupplierID());
                ps.setDate(2, stock.getImportDate());
                ps.setInt(3, stock.getStatus());
                ps.setInt(4, stock.getStaffID());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    isid = rs.getInt(1);
                } else {
                    conn.rollback();
                    throw new SQLException("Cannot get ISID.");
                }
            }

            try ( PreparedStatement psDetail = conn.prepareStatement(insertDetailSQL);  PreparedStatement psUpdate = conn.prepareStatement(updateStockSQL)) {
                for (ImportStockDetailDTO d : details) {
                    psDetail.setInt(1, d.getBookID());
                    psDetail.setInt(2, isid);
                    psDetail.setInt(3, d.getISDQuantity());
                    psDetail.setDouble(4, d.getImportPrice());
                    psDetail.addBatch();

                    psUpdate.setInt(1, d.getISDQuantity());
                    psUpdate.setInt(2, d.getBookID());
                    psUpdate.addBatch();
                }
                psDetail.executeBatch();
                psUpdate.executeBatch();
            }

            conn.commit();
            return true;
        }
    }

    /**
     * Get all import stock records without pagination
     */
    public List<ImportStockDTO> getAllImportStocks() {
        List<ImportStockDTO> list = new ArrayList<>();
        String sql = "SELECT i.ISID, i.importDate, i.ISStatus, s.supID, s.supName, st.staffID, "
                + "a.firstName + ' ' + a.lastName AS staffName, "
                + "SUM(ISNULL(d.ISDQuantity * d.importPrice, 0)) AS totalPrice "
                + "FROM ImportStock i "
                + "JOIN Supplier s ON i.supID = s.supID "
                + "JOIN Staff st ON i.staffID = st.staffID "
                + "JOIN Account a ON st.username = a.username "
                + "LEFT JOIN ImportStockDetail d ON i.ISID = d.ISID "
                + "GROUP BY i.ISID, i.importDate, i.ISStatus, s.supID, s.supName, st.staffID, a.firstName, a.lastName "
                + "ORDER BY i.ISID ASC"; // Changed to sort by ID in ascending order

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ImportStockDTO stock = new ImportStockDTO();
                stock.setId(rs.getInt("ISID"));
                stock.setImportDate(rs.getDate("importDate"));
                stock.setStatus(rs.getInt("ISStatus"));
                stock.setSupplierID(rs.getInt("supID"));
                stock.setSupplierName(rs.getString("supName"));
                stock.setStaffID(rs.getInt("staffID"));
                stock.setStaffName(rs.getString("staffName"));
                stock.setTotalPrice(rs.getDouble("totalPrice"));
                list.add(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get the total count of import stock records.
     */
    public int getTotalImportStockCount() {
        int total = 0;
        String query = "SELECT COUNT(*) FROM ImportStock";

        try ( Connection conn = getConnection();  PreparedStatement pst = conn.prepareStatement(query);  ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1); // Get total count
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    /**
     * Get details of a specific import stock by its ID
     */
    public List<ImportStockDetailDTO> getImportStockDetailsByISID(int isid) {
        List<ImportStockDetailDTO> list = new ArrayList<>();
        String sql = "SELECT d.ISDID, d.bookID, d.ISID, d.ISDQuantity, d.importPrice, b.bookTitle "
                + "FROM ImportStockDetail d "
                + "JOIN Book b ON d.bookID = b.bookID "
                + "WHERE d.ISID = ?";

        try ( Connection conn = getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, isid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ImportStockDetailDTO dto = new ImportStockDetailDTO(
                        rs.getInt("ISDID"),
                        rs.getInt("bookID"),
                        rs.getInt("ISID"),
                        rs.getInt("ISDQuantity"),
                        rs.getDouble("importPrice")
                );
                dto.setBookTitle(rs.getString("bookTitle"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ImportStockDTO> getImportStocksByPage(int page, int pageSize) {
    List<ImportStockDTO> list = new ArrayList<>();
    String sql = "SELECT i.ISID, i.importDate, i.ISStatus, s.supID, s.supName, st.staffID, " +
                 "a.firstName + ' ' + a.lastName AS staffName, " +
                 "SUM(ISNULL(d.ISDQuantity * d.importPrice, 0)) AS totalPrice " +
                 "FROM ImportStock i " +
                 "JOIN Supplier s ON i.supID = s.supID " +
                 "JOIN Staff st ON i.staffID = st.staffID " +
                 "JOIN Account a ON st.username = a.username " +
                 "LEFT JOIN ImportStockDetail d ON i.ISID = d.ISID " +
                 "GROUP BY i.ISID, i.importDate, i.ISStatus, s.supID, s.supName, st.staffID, a.firstName, a.lastName " +
                 "ORDER BY i.ISID ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, (page - 1) * pageSize);
        ps.setInt(2, pageSize);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ImportStockDTO stock = new ImportStockDTO();
            stock.setId(rs.getInt("ISID"));
            stock.setImportDate(rs.getDate("importDate"));
            stock.setStatus(rs.getInt("ISStatus"));
            stock.setSupplierID(rs.getInt("supID"));
            stock.setSupplierName(rs.getString("supName"));
            stock.setStaffID(rs.getInt("staffID"));
            stock.setStaffName(rs.getString("staffName"));
            stock.setTotalPrice(rs.getDouble("totalPrice"));
            list.add(stock);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
