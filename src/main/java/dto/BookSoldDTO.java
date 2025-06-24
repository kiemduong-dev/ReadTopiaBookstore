package dto;

/**
 * DTO lưu thông tin sách bán chạy
 */
public class BookSoldDTO {

    private String title;
    private String author;
    private String publisher;
    private int sold;
    private int price;

    // Getters và setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Phương thức toString để debug
    @Override
    public String toString() {
        return "BookSoldDTO{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", sold=" + sold +
                ", price=" + price +
                '}';
    }
}
