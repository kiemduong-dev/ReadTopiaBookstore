package dao;

import dto.BookDTO;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookDAO - Data Access Object responsible for database operations
 * related to the Book and Book_Category tables.
 *
 * This class provides methods to:
 * - Retrieve books (all, by ID, by category, by keyword).
 * - Insert, update, soft-delete book records.
 * - Manage book-category associations.
 * - Get metadata such as all authors, publishers, translators.
 *
 * Commonly used in servlet controllers like BookListDashboardServlet.
 *
 * Author: CE182018 Vuong Chi Bao
 */
public class BookDAO {

    /**
     * Retrieves all books with active status (bookStatus = 1).
     *
     * @return List of active BookDTO objects.
     */
    public List<BookDTO> getAllBooks() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The book's ID.
     * @return BookDTO object if found, otherwise null.
     */
    public BookDTO getBookByID(int id) {
        String sql = "SELECT * FROM Book WHERE bookID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
     * Retrieves the category ID of a given book.
     *
     * @param bookID The book's ID.
     * @return Category ID if found, otherwise -1.
     */
    public int getCategoryIDByBookID(int bookID) {
        String sql = "SELECT cat_id FROM Book_Category WHERE book_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("cat_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Searches books by title or author using a keyword.
     *
     * @param keyword The search keyword.
     * @return List of matching BookDTO objects.
     */
    public List<BookDTO> searchBooksByTitleOrAuthor(String keyword) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1 AND (bookTitle LIKE ? OR author LIKE ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
     * Retrieves books sorted by a given field.
     *
     * @param sortBy Sorting option: "price_asc", "price_desc", "title_asc", "title_desc", or default.
     * @return Sorted list of books.
     */
    public List<BookDTO> getBooksSortedBy(String sortBy) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Book WHERE bookStatus = 1 ";

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "title_asc";
        }

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

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(extractBookFromResultSet(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Retrieves books by a specific category ID.
     *
     * @param catID Category ID.
     * @return List of books in the given category.
     */
    public List<BookDTO> getBooksByCategory(int catID) {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT b.* FROM Book b JOIN Book_Category bc ON b.bookID = bc.book_id "
                   + "WHERE bc.cat_id = ? AND b.bookStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

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
     * Inserts a new book into the database.
     *
     * @param book BookDTO object containing the book data.
     * @return The generated book ID if successful, otherwise -1.
     */
    public int insertBook(BookDTO book) {
        String sql = "INSERT INTO Book (bookTitle, author, translator, publisher, publicationYear, isbn, image, "
                   + "bookDescription, hardcover, dimension, weight, bookPrice, bookQuantity, bookStatus) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setBookParams(ps, book);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Inserts a record into the Book_Category table linking book to category.
     *
     * @param bookID Book ID.
     * @param categoryID Category ID.
     */
    public void insertBookCategory(int bookID, int categoryID) {
        String sql = "INSERT INTO Book_Category (book_id, cat_id) VALUES (?, ?)";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);
            ps.setInt(2, categoryID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the book's category (delete old and insert new).
     *
     * @param bookID Book ID.
     * @param categoryID New category ID.
     */
    public void updateBookCategory(int bookID, int categoryID) {
        String deleteSql = "DELETE FROM Book_Category WHERE book_id = ?";
        String insertSql = "INSERT INTO Book_Category (book_id, cat_id) VALUES (?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, bookID);
                deleteStmt.executeUpdate();
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, bookID);
                insertStmt.setInt(2, categoryID);
                insertStmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing book in the database.
     *
     * @param book BookDTO containing updated data.
     */
    public void updateBook(BookDTO book) {
        String sql = "UPDATE Book SET bookTitle = ?, author = ?, translator = ?, publisher = ?, publicationYear = ?, "
                   + "isbn = ?, image = ?, bookDescription = ?, hardcover = ?, dimension = ?, weight = ?, "
                   + "bookPrice = ?, bookQuantity = ?, bookStatus = ? WHERE bookID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            setBookParams(ps, book);
            ps.setInt(15, book.getBookID());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Soft deletes a book by setting bookStatus to 0.
     *
     * @param bookID ID of the book to delete.
     * @return True if successful, otherwise false.
     */
    public boolean deleteBookByID(int bookID) {
        String sql = "UPDATE Book SET bookStatus = 0 WHERE bookID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the available quantity of a book.
     *
     * @param bookID Book ID.
     * @param quantity New quantity value.
     * @return True if update was successful, otherwise false.
     */
    public boolean updateBookQuantity(int bookID, int quantity) {
        String sql = "UPDATE Book SET bookQuantity = ? WHERE bookID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, bookID);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Retrieves all unique authors from active books.
     *
     * @return List of authors.
     */
    public List<String> getAllAuthors() {
        List<String> authors = new ArrayList<>();
        String sql = "SELECT DISTINCT author FROM Book WHERE bookStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                authors.add(rs.getString("author"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authors;
    }

    /**
     * Retrieves all unique translators from active books.
     *
     * @return List of translators.
     */
    public List<String> getAllTranslators() {
        List<String> translators = new ArrayList<>();
        String sql = "SELECT DISTINCT translator FROM Book WHERE bookStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                translators.add(rs.getString("translator"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return translators;
    }

    /**
     * Retrieves all unique publishers from active books.
     *
     * @return List of publishers.
     */
    public List<String> getAllPublishers() {
        List<String> publishers = new ArrayList<>();
        String sql = "SELECT DISTINCT publisher FROM Book WHERE bookStatus = 1";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                publishers.add(rs.getString("publisher"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return publishers;
    }

    // ========== Utility Methods ==========

    /**
     * Extracts a BookDTO from the current row of a ResultSet.
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
     * Sets parameters for a PreparedStatement based on a BookDTO object.
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
