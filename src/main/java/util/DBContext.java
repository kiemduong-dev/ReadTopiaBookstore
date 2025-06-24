package util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database Context - Provides SQL Server connection functionality.
 *
 * This utility class offers a method to establish a connection to the
 * configured SQL Server database using hardcoded credentials.
 *
 * <p>
 * <b>Note:</b> In production environments, credentials and configurations
 * should be stored securely using configuration files or environment
 * variables.</p>
 *
 * @author CE181518 Dương An Kiếm
 */
public class DBContext {

    /**
     * JDBC connection string for SQL Server.
     */
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=ReadTopia;encrypt=false";

    /**
     * SQL Server username for authentication.
     */
    private static final String DB_USER = "sa"; // TODO: Replace with secure credentials

    /**
     * SQL Server password for authentication.
     */
    private static final String DB_PASSWORD = "123456"; // TODO: Replace with secure credentials

    /**
     * Establishes a connection to the SQL Server database.
     *
     * @return A Connection object to the configured database
     * @throws Exception If the JDBC driver is not found or connection fails
     *
     * <p>
     * Algorithm:</p>
     * <ol>
     * <li>Load the Microsoft SQL Server JDBC driver</li>
     * <li>Use DriverManager to connect with configured URL, username, and
     * password</li>
     * <li>Return the established Connection object</li>
     * </ol>
     */
    public Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
