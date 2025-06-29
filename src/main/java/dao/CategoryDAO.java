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
 * operations and database interaction related to categories.
 *
 * Author: Vuong Chi Bao_CE182018
 */
public class CategoryDAO {

    /**
     * Retrieves all active categories.
     *
     * @return List of active CategoryDTO objects
     */
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE catStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractCategory(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id category ID
     * @return CategoryDTO object or null if not found
     */
    public CategoryDTO getCategoryById(int id) {
        String sql = "SELECT * FROM Category WHERE catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractCategory(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Adds a new category with active status.
     *
     * @param category the category to add
     * @return true if successful, false otherwise
     */
    public boolean addCategory(CategoryDTO category) {
        String sql = "INSERT INTO Category (catName, catDescription, catStatus) VALUES (?, ?, 1)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryDescription());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates an existing category.
     *
     * @param category category with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(CategoryDTO category) {
        String sql = "UPDATE Category SET catName = ?, catDescription = ? WHERE catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getCategoryDescription());
            ps.setInt(3, category.getCategoryID());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Soft deletes a category by setting catStatus = 0.
     *
     * @param id category ID
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(int id) {
        String sql = "UPDATE Category SET catStatus = 0 WHERE catID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Searches categories by name or description. Only returns active
     * categories.
     *
     * @param keyword search keyword
     * @return list of matched CategoryDTOs
     */
    public List<CategoryDTO> searchCategories(String keyword) {
        List<CategoryDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Category WHERE catStatus = 1 AND (catName LIKE ? OR catDescription LIKE ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractCategory(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Checks if a category with the given name already exists
     * (case-insensitive).
     *
     * @param name the category name to check
     * @return true if name exists, false otherwise
     */
    public boolean isCategoryNameExists(String name) {
        String sql = "SELECT 1 FROM Category WHERE LOWER(catName) = LOWER(?) AND catStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // Nếu có dòng nào trả về thì đã tồn tại

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * (Optional) Retrieves categories that are linked with books. To be
     * implemented if needed.
     *
     * @return list of categories used by books
     */
    public List<CategoryDTO> getAllCategoriesWithBooks() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Utility method to extract a CategoryDTO from a ResultSet.
     *
     * @param rs the result set
     * @return CategoryDTO
     * @throws Exception
     */
    private CategoryDTO extractCategory(ResultSet rs) throws Exception {
        return new CategoryDTO(
                rs.getInt("catID"),
                rs.getString("catName"),
                rs.getString("catDescription")
        );
    }
}
