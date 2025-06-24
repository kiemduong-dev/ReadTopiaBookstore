package dto;

/**
 * BookDTO - Data Transfer Object representing a book. This class encapsulates
 * all data related to a book record.
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

    /**
     * Default constructor
     */
    public BookDTO() {
    }

    /**
     * Full constructor
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

    // --- Getters and Setters ---
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public int getHardcover() {
        return hardcover;
    }

    public void setHardcover(int hardcover) {
        this.hardcover = hardcover;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public int getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(int bookStatus) {
        this.bookStatus = bookStatus;
    }
}
