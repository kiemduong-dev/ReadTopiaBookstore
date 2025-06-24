package dto;

/**
 * BookSoldDTO - Represents a data transfer object for the best-selling books.
 * This class stores the title, author, publisher, sold quantity, and price.
 *
 * @author CE181518 Dương An Kiếm
 */
public class BookSoldDTO {

    // === Fields ===
    /**
     * The title of the book
     */
    private String title;

    /**
     * The author of the book
     */
    private String author;

    /**
     * The publisher of the book
     */
    private String publisher;

    /**
     * The number of copies sold
     */
    private int sold;

    /**
     * The price of the book
     */
    private int price;

    // === Getters and Setters ===
    /**
     * Gets the title of the book.
     *
     * @return The title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param author The author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the publisher of the book.
     *
     * @return The publisher of the book
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of the book.
     *
     * @param publisher The publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the number of copies sold.
     *
     * @return The number of books sold
     */
    public int getSold() {
        return sold;
    }

    /**
     * Sets the number of copies sold.
     *
     * @param sold The number of books sold to set
     */
    public void setSold(int sold) {
        this.sold = sold;
    }

    /**
     * Gets the price of the book.
     *
     * @return The book price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Sets the price of the book.
     *
     * @param price The book price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    // === toString method for debugging ===
    /**
     * Returns a string representation of the BookSoldDTO object.
     *
     * @return A string representation of the object
     */
    @Override
    public String toString() {
        return "BookSoldDTO{"
                + "title='" + title + '\''
                + ", author='" + author + '\''
                + ", publisher='" + publisher + '\''
                + ", sold=" + sold
                + ", price=" + price
                + '}';
    }
}
