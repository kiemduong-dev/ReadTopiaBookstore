package dto;

/**
 * CategoryDTO - Data Transfer Object representing a book category. Used to
 * transfer category data between DAO, Controller, and View layers.
 *
 * Author: Vuong Chi Bao_CE182018
 */
public class CategoryDTO {

    private int categoryID;
    private String categoryName;
    private String categoryDescription;

    /**
     * Default constructor
     */
    public CategoryDTO() {
    }

    /**
     * Full constructor
     *
     * @param categoryID the category ID
     * @param categoryName the name of the category
     * @param categoryDescription the description of the category
     */
    public CategoryDTO(int categoryID, String categoryName, String categoryDescription) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    // --- Getters and Setters ---
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    @Override
    public String toString() {
        return "CategoryDTO{"
                + "categoryID=" + categoryID
                + ", categoryName='" + categoryName + '\''
                + ", categoryDescription='" + categoryDescription + '\''
                + '}';
    }
}
