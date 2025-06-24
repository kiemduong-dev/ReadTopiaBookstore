package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * DBContext is a utility class that provides a method for connecting to the SQL
 * Server database. This class uses hard-coded credentials and configuration
 * values, which should be replaced or externalized in a production environment
 * for security and flexibility.
 */
public class DBContext {
 
    // Database connection URL, including host, port, and database name
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ReadTopia;encrypt=false";

    // Database username used for authentication
    private static final String DB_USER = "sa"; // Replace with your actual database username

    // Database password used for authentication
    private static final String DB_PASSWORD = "123456"; // Replace with your actual database password

    /**
     * Establishes and returns a new connection to the configured SQL Server
     * database. This method must be called each time a connection is needed.
     *
     * @return a Connection object to the database
     * @throws Exception if the JDBC driver class is not found or the connection
     * fails
     *
     * <p>
     * Algorithm steps:</p>
     * <ol>
     * <li>Load the Microsoft SQL Server JDBC driver using Class.forName</li>
     * <li>Use DriverManager.getConnection with the configured URL, username,
     * and password</li>
     * <li>Return the Connection object</li>
     * </ol>
     */
    public static Connection getConnection() throws Exception {
        try {
            // Step 1: Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Step 2: Establish a connection to the database using JDBC URL, username, and password
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Optional: Log successful connection
            System.out.println("✅ Database connection established successfully");
            
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found: " + e.getMessage());
            throw new Exception("SQL Server JDBC Driver not found", e);
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            throw new Exception("Failed to connect to database", e);
        }
    }

    /**
     * Test the database connection.
     * This method can be used to verify if the database connection is working properly.
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }
}
