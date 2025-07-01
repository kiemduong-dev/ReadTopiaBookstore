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

    private Integer parentID;      // Cho phép null
    private String parentName;     // Tên của danh mục cha (chỉ để hiển thị)

    // --- Constructors ---
    public CategoryDTO() {
    }

    public CategoryDTO(int categoryID, String categoryName, String categoryDescription) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    public CategoryDTO(int categoryID, String categoryName, String categoryDescription, Integer parentID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentID = parentID;
    }

    public CategoryDTO(int categoryID, String categoryName, String categoryDescription, Integer parentID, String parentName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentID = parentID;
        this.parentName = parentName;
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

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return "CategoryDTO{"
                + "categoryID=" + categoryID
                + ", categoryName='" + categoryName + '\''
                + ", categoryDescription='" + categoryDescription + '\''
                + ", parentID=" + parentID
                + ", parentName='" + parentName + '\''
                + '}';
    }
}
