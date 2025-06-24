/**
 * BookDAO - Data Access Object for managing Book records in the database.
 * Provides CRUD operations and search/filter capabilities.
 *
 * @author Vuong Chi Bao
 */
package dao;

import dto.BookDTO;
import util.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    /**
     * Retrieves all active books (bookStatus = 1).
     *
     * @return list of active books
     */
    public List<BookDTO> getAllBooks() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves a single book by ID.
     *
     * @param id the book ID
     * @return BookDTO if found, otherwise null
     */
    public BookDTO getBookByID(int id) {
        String sql = "SELECT * FROM Book WHERE bookID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Searches books by title or author using keyword.
     *
     * @param keyword search term
     * @return list of matching books
     */
    public List<BookDTO> searchBooksByTitleOrAuthor(String keyword) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1 AND (bookTitle LIKE ? OR author LIKE ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves sorted books based on specified sorting.
     *
     * @param sortBy sort criteria (price_asc, price_desc, title_asc,
     * title_desc)
     * @return sorted list of books
     */
    public List<BookDTO> getBooksSortedBy(String sortBy) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1 ";

        switch (sortBy) {
            case "price_asc":
                sql += "ORDER BY bookPrice ASC";
                break;
            case "price_desc":
                sql += "ORDER BY bookPrice DESC";
                break;
            case "title_asc":
                sql += "ORDER BY bookTitle ASC";
                break;
            case "title_desc":
                sql += "ORDER BY bookTitle DESC";
                break;
            default:
                sql += "ORDER BY bookID DESC";
        }

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves books belonging to a specific category.
     *
     * @param catID category ID
     * @return list of books in the category
     */
    public List<BookDTO> getBooksByCategory(int catID) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT b.* FROM Book b JOIN Book_Category bc ON b.bookID = bc.book_id "
                + "WHERE bc.cat_id = ? AND b.bookStatus = 1";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, catID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Inserts a new book record into the database.
     *
     * @param book the BookDTO object to insert
     */
    public void insertBook(BookDTO book) {
        String sql = "INSERT INTO Book (bookTitle, author, translator, publisher, publicationYear, isbn, image, "
                + "bookDescription, hardcover, dimension, weight, bookPrice, bookQuantity, bookStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            setBookParams(ps, book);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing book record.
     *
     * @param book the updated BookDTO object
     */
    public void updateBook(BookDTO book) {
        String sql = "UPDATE Book SET bookTitle = ?, author = ?, translator = ?, publisher = ?, publicationYear = ?, "
                + "isbn = ?, image = ?, bookDescription = ?, hardcover = ?, dimension = ?, weight = ?, "
                + "bookPrice = ?, bookQuantity = ?, bookStatus = ? WHERE bookID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            setBookParams(ps, book);
            ps.setInt(15, book.getBookID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a soft delete on a book (sets bookStatus = 0).
     *
     * @param bookID ID of the book to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteBookByID(int bookID) {
        String sql = "UPDATE Book SET bookStatus = 0 WHERE bookID = ?";

        try ( Connection conn = new DBContext().getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --------------------------------------
    // Helper Methods
    // --------------------------------------
    /**
     * Extracts a BookDTO from a ResultSet.
     *
     * @param rs the ResultSet
     * @return a BookDTO object
     * @throws SQLException if SQL error occurs
     */
    private BookDTO extractBookFromResultSet(ResultSet rs) throws SQLException {
        return new BookDTO(
                rs.getInt("bookID"),
                rs.getString("bookTitle"),
                rs.getString("author"),
                rs.getString("translator"),
                rs.getString("publisher"),
                rs.getInt("publicationYear"),
                rs.getString("isbn"),
                rs.getString("image"),
                rs.getString("bookDescription"),
                rs.getInt("hardcover"),
                rs.getString("dimension"),
                rs.getFloat("weight"),
                rs.getDouble("bookPrice"),
                rs.getInt("bookQuantity"),
                rs.getInt("bookStatus")
        );
    }

    /**
     * Sets parameters for PreparedStatement from BookDTO.
     *
     * @param ps the PreparedStatement
     * @param book the BookDTO object
     * @throws SQLException if SQL error occurs
     */
    private void setBookParams(PreparedStatement ps, BookDTO book) throws SQLException {
        ps.setString(1, book.getBookTitle());
        ps.setString(2, book.getAuthor());
        ps.setString(3, book.getTranslator());
        ps.setString(4, book.getPublisher());
        ps.setInt(5, book.getPublicationYear());
        ps.setString(6, book.getIsbn());
        ps.setString(7, book.getImage());
        ps.setString(8, book.getBookDescription());
        ps.setInt(9, book.getHardcover());
        ps.setString(10, book.getDimension());
        ps.setFloat(11, book.getWeight());
        ps.setDouble(12, book.getBookPrice());
        ps.setInt(13, book.getBookQuantity());
        ps.setInt(14, book.getBookStatus());
    }
}
