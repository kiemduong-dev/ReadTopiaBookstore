package dto;

/**
 * BookDTO - Data Transfer Object representing a book. Encapsulates all fields
 * of a book record for data transfer between layers.
 *
 * Author: Vuong Chi Bao_CE182018
 */
public class BookDTO {

    private int bookID;
    private String bookTitle;
    private String author;
    private String translator;
    private String publisher;
    private int publicationYear;
    private String isbn;
    private String image;
    private String bookDescription;
    private int hardcover;
    private String dimension;
    private float weight;
    private double bookPrice;
    private int bookQuantity;
    private int bookStatus;
    private int categoryID;

    /**
     * Default constructor. Creates an empty BookDTO object.
     */
    public BookDTO() {
    }

    /**
     * Full constructor.
     *
     * @param bookID the unique ID of the book
     * @param bookTitle the title of the book
     * @param author the author of the book
     * @param translator the translator of the book
     * @param publisher the publisher of the book
     * @param publicationYear the year the book was published
     * @param isbn the ISBN number
     * @param image the image URL or path
     * @param bookDescription a description of the book
     * @param hardcover hardcover status (1 for true, 0 for false)
     * @param dimension physical dimensions of the book
     * @param weight weight of the book
     * @param bookPrice price of the book
     * @param bookQuantity number of copies available
     * @param bookStatus status of the book (1 for active, 0 for inactive)
     */
    public BookDTO(int bookID, String bookTitle, String author, String translator, String publisher,
            int publicationYear, String isbn, String image, String bookDescription, int hardcover,
            String dimension, float weight, double bookPrice, int bookQuantity, int bookStatus) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.author = author;
        this.translator = translator;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.image = image;
        this.bookDescription = bookDescription;
        this.hardcover = hardcover;
        this.dimension = dimension;
        this.weight = weight;
        this.bookPrice = bookPrice;
        this.bookQuantity = bookQuantity;
        this.bookStatus = bookStatus;
    }

    // ----------------------------
    // Getters and Setters
    // ----------------------------
    /**
     * @return the book ID
     */
    public int getBookID() {
        return bookID;
    }

    // Getter và Setter cho categoryID
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    /**
     * @param bookID the book ID to set
     */
    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    /**
     * @return the book title
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * @param bookTitle the book title to set
     */
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the translator
     */
    public String getTranslator() {
        return translator;
    }

    /**
     * @param translator the translator to set
     */
    public void setTranslator(String translator) {
        this.translator = translator;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the publication year
     */
    public int getPublicationYear() {
        return publicationYear;
    }

    /**
     * @param publicationYear the publication year to set
     */
    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * @return the ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the ISBN to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the image URL or path
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image the image URL or path to set
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return the book description
     */
    public String getBookDescription() {
        return bookDescription;
    }

    /**
     * @param bookDescription the book description to set
     */
    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    /**
     * @return the hardcover status (1 or 0)
     */
    public int getHardcover() {
        return hardcover;
    }

    /**
     * @param hardcover the hardcover status to set
     */
    public void setHardcover(int hardcover) {
        this.hardcover = hardcover;
    }

    /**
     * @return the dimensions
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimensions to set
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * @return the weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * @return the book price
     */
    public double getBookPrice() {
        return bookPrice;
    }

    /**
     * @param bookPrice the price to set
     */
    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    /**
     * @return the quantity available
     */
    public int getBookQuantity() {
        return bookQuantity;
    }

    /**
     * @param bookQuantity the quantity to set
     */
    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    /**
     * @return the book status (1 = active, 0 = inactive)
     */
    public int getBookStatus() {
        return bookStatus;
    }

    /**
     * @param bookStatus the status to set
     */
    public void setBookStatus(int bookStatus) {
        this.bookStatus = bookStatus;
    }
}
