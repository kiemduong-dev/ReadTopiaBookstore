package dto;

/**
 * CategoryDTO - Data Transfer Object representing a book category. Used for
 * transferring category data between DAO, Controller, and View layers. Includes
 * parent category information for hierarchical structures.
 *
 * @author CE182018 Vuong Chi Bao
 */
public class CategoryDTO {

    private int categoryID;              // Unique ID of the category
    private String categoryName;         // Name of the category
    private String categoryDescription;  // Description of the category

    private Integer parentID;            // Parent category ID (nullable)
    private String parentName;           // Parent category name (for display)

    // ======================
    // Constructors
    // ======================
    /**
     * Default constructor.
     */
    public CategoryDTO() {
    }

    /**
     * Constructor without parent information.
     *
     * @param categoryID the category ID
     * @param categoryName the category name
     * @param categoryDescription the category description
     */
    public CategoryDTO(int categoryID, String categoryName, String categoryDescription) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    /**
     * Constructor with parent category ID.
     *
     * @param categoryID the category ID
     * @param categoryName the category name
     * @param categoryDescription the category description
     * @param parentID the parent category ID
     */
    public CategoryDTO(int categoryID, String categoryName, String categoryDescription, Integer parentID) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentID = parentID;
    }

    /**
     * Full constructor with parent category details.
     *
     * @param categoryID the category ID
     * @param categoryName the category name
     * @param categoryDescription the category description
     * @param parentID the parent category ID
     * @param parentName the parent category name
     */
    public CategoryDTO(int categoryID, String categoryName, String categoryDescription, Integer parentID, String parentName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.parentID = parentID;
        this.parentName = parentName;
    }

    // ======================
    // Getters and Setters
    // ======================
    /**
     * Gets the category ID.
     *
     * @return the category ID
     */
    public int getCategoryID() {
        return categoryID;
    }

    /**
     * Sets the category ID.
     *
     * @param categoryID the category ID to set
     */
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * Gets the category name.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the category name.
     *
     * @param categoryName the category name to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Gets the category description.
     *
     * @return the category description
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Sets the category description.
     *
     * @param categoryDescription the category description to set
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * Gets the parent category ID.
     *
     * @return the parent category ID (nullable)
     */
    public Integer getParentID() {
        return parentID;
    }

    /**
     * Sets the parent category ID.
     *
     * @param parentID the parent category ID to set
     */
    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    /**
     * Gets the parent category name.
     *
     * @return the parent category name
     */
    public String getParentName() {
        return parentName;
    }

    /**
     * Sets the parent category name.
     *
     * @param parentName the parent category name to set
     */
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * Returns a string representation of the CategoryDTO.
     *
     * @return string representation of the category
     */
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
