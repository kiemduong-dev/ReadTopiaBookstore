/**
 * CategoryDAO - Data Access Object for Category entities.
 * Handles CRUD operations and parent-child relationships.
 *
 * Author: CE182018 Vuong Chi Bao
 */
package dao;

import dto.CategoryDTO;
import util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // --------------------------------------
    // CRUD Operations
    // --------------------------------------
    /**
     * Retrieves all active categories with their parent names (if any).
     *
     * @return list of active CategoryDTO objects
     */
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                + "FROM Category c "
                + "LEFT JOIN Category p ON c.parentID = p.catID "
                + "WHERE c.catStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }

        } catch (Exception e) {
            System.err.println("Error in getAllCategories: " + e.getMessage());
        }

        return categories;
    }

    /**
     * Retrieves a single category by its ID.
     *
     * @param categoryId the category ID
     * @return CategoryDTO if found, otherwise null
     */
    public CategoryDTO getCategoryById(int categoryId) {
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                + "FROM Category c "
                + "LEFT JOIN Category p ON c.parentID = p.catID "
                + "WHERE c.catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToCategory(rs);
            }

        } catch (Exception e) {
            System.err.println("Error in getCategoryById: " + e.getMessage());
        }

        return null;
    }

    /**
     * Adds a new category to the database.
     *
     * @param category the CategoryDTO to add
     * @return true if insertion is successful, false otherwise
     */
    public boolean addCategory(CategoryDTO category) {
        String sql = "INSERT INTO Category (catName, catDescription, catStatus, parentID) VALUES (?, ?, 1, ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryDescription());

            if (category.getParentID() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, category.getParentID());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error in addCategory: " + e.getMessage());
        }

        return false;
    }

    /**
     * Updates an existing category.
     *
     * @param category the updated CategoryDTO
     * @return true if update is successful, false otherwise
     */
    public boolean updateCategory(CategoryDTO category) {
        String sql = "UPDATE Category SET catName = ?, catDescription = ?, parentID = ? WHERE catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryDescription());

            if (category.getParentID() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, category.getParentID());
            }

            ps.setInt(4, category.getCategoryID());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error in updateCategory: " + e.getMessage());
        }

        return false;
    }

    /**
     * Soft deletes a category (sets catStatus = 0).
     *
     * @param categoryId ID of the category to delete
     * @return true if deletion is successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        String sql = "UPDATE Category SET catStatus = 0 WHERE catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Error in deleteCategory: " + e.getMessage());
        }

        return false;
    }

    // --------------------------------------
    // Search & Utility Methods
    // --------------------------------------
    /**
     * Searches categories by keyword (name or description).
     *
     * @param keyword search term
     * @return list of matching CategoryDTO objects
     */
    public List<CategoryDTO> searchCategories(String keyword) {
        List<CategoryDTO> categories = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                + "FROM Category c "
                + "LEFT JOIN Category p ON c.parentID = p.catID "
                + "WHERE c.catStatus = 1 AND (c.catName LIKE ? OR c.catDescription LIKE ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }

        } catch (Exception e) {
            System.err.println("Error in searchCategories: " + e.getMessage());
        }

        return categories;
    }

    /**
     * Checks if a category name already exists (case-insensitive).
     *
     * @param name the category name to check
     * @return true if name exists, false otherwise
     */
    public boolean isCategoryNameExists(String name) {
        String sql = "SELECT 1 FROM Category WHERE LOWER(catName) = LOWER(?) AND catStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.err.println("Error in isCategoryNameExists: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retrieves all active categories excluding a specific category ID. Used
     * when editing to prevent selecting itself as a parent.
     *
     * @param excludeId ID to exclude
     * @return list of CategoryDTO excluding the specified ID
     */
    public List<CategoryDTO> getAllCategoriesExcluding(int excludeId) {
        List<CategoryDTO> categories = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                + "FROM Category c "
                + "LEFT JOIN Category p ON c.parentID = p.catID "
                + "WHERE c.catStatus = 1 AND c.catID <> ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, excludeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }

        } catch (Exception e) {
            System.err.println("Error in getAllCategoriesExcluding: " + e.getMessage());
        }

        return categories;
    }

    // --------------------------------------
    // Helper Methods
    // --------------------------------------
    /**
     * Maps a ResultSet row to a CategoryDTO.
     *
     * @param rs the ResultSet
     * @return a CategoryDTO object
     * @throws Exception if mapping fails
     */
    private CategoryDTO mapResultSetToCategory(ResultSet rs) throws Exception {
        return new CategoryDTO(
                rs.getInt("catID"),
                rs.getString("catName"),
                rs.getString("catDescription"),
                rs.getInt("parentID"),
                rs.getString("parentName")
        );
    }
}
