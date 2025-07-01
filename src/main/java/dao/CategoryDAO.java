package dao;

import dto.CategoryDTO;
import util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO - Data Access Object for Category entities. Handles CRUD
 * operations and parent-child relationship.
 *
 * Author: Vuong Chi Bao_CE182018
 */
public class CategoryDAO {

    /**
     * Retrieves all active categories including parent name.
     */
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                   + "FROM Category c "
                   + "LEFT JOIN Category p ON c.parentID = p.catID "
                   + "WHERE c.catStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoryDTO category = new CategoryDTO(
                        rs.getInt("catID"),
                        rs.getString("catName"),
                        rs.getString("catDescription"),
                        rs.getInt("parentID"),
                        rs.getString("parentName")
                );
                list.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves a category by ID.
     */
    public CategoryDTO getCategoryById(int id) {
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                   + "FROM Category c "
                   + "LEFT JOIN Category p ON c.parentID = p.catID "
                   + "WHERE c.catID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CategoryDTO(
                        rs.getInt("catID"),
                        rs.getString("catName"),
                        rs.getString("catDescription"),
                        rs.getInt("parentID"),
                        rs.getString("parentName")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a new category.
     */
    public boolean addCategory(CategoryDTO category) {
        String sql = "INSERT INTO Category (catName, catDescription, catStatus, parentID) VALUES (?, ?, 1, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryDescription());

            // If no parent, set NULL
            if (category.getParentID() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, category.getParentID());
            }

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates an existing category.
     */
    public boolean updateCategory(CategoryDTO category) {
        String sql = "UPDATE Category SET catName = ?, catDescription = ?, parentID = ? WHERE catID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Soft deletes a category.
     */
    public boolean deleteCategory(int id) {
        String sql = "UPDATE Category SET catStatus = 0 WHERE catID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Searches categories by keyword (name or description).
     */
    public List<CategoryDTO> searchCategories(String keyword) {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                   + "FROM Category c "
                   + "LEFT JOIN Category p ON c.parentID = p.catID "
                   + "WHERE c.catStatus = 1 AND (c.catName LIKE ? OR c.catDescription LIKE ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CategoryDTO(
                        rs.getInt("catID"),
                        rs.getString("catName"),
                        rs.getString("catDescription"),
                        rs.getInt("parentID"),
                        rs.getString("parentName")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Checks if category name exists.
     */
    public boolean isCategoryNameExists(String name) {
        String sql = "SELECT 1 FROM Category WHERE LOWER(catName) = LOWER(?) AND catStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves all active categories excluding the one with given ID. Used for
     * editing (to prevent a category being its own parent).
     *
     * @param excludeID category ID to exclude
     * @return list of CategoryDTO excluding the specified ID
     */
    public List<CategoryDTO> getAllCategoriesExcluding(int excludeID) {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT c.catID, c.catName, c.catDescription, c.parentID, p.catName AS parentName "
                   + "FROM Category c "
                   + "LEFT JOIN Category p ON c.parentID = p.catID "
                   + "WHERE c.catStatus = 1 AND c.catID <> ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, excludeID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new CategoryDTO(
                        rs.getInt("catID"),
                        rs.getString("catName"),
                        rs.getString("catDescription"),
                        rs.getInt("parentID"),
                        rs.getString("parentName")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

 

}
