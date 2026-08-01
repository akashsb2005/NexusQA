package com.akash.nexusqa.db;

import com.akash.nexusqa.config.ConfigManager;
import com.akash.nexusqa.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

    private static final Logger logger = LogManager.getLogger(DBUtils.class);

    private DBUtils() {
    }

    private static Connection getConnection() {
        Properties props = loadDbProperties();
        try {
            String url = props.getProperty("dbUrl");
            String username = props.getProperty("dbUsername");
            String password = props.getProperty("dbPassword");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("Failed to connect to database", e);
            throw new FrameworkException("Could not connect to database", e);
        }
    }

    private static Properties loadDbProperties() {
        Properties props = new Properties();
        try (var fis = new java.io.FileInputStream("src/test/resources/config/config.properties")) {
            props.load(fis);
        } catch (java.io.IOException e) {
            throw new FrameworkException("Could not load DB config properties", e);
        }
        return props;
    }

    public static boolean userExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    logger.info("Checked existence of user with email {}: {}", email, exists);
                    return exists;
                }
                return false;
            }
        } catch (SQLException e) {
            logger.error("Query failed for userExistsByEmail: {}", email, e);
            throw new FrameworkException("Database query failed", e);
        }
    }

    public static String getUserNameByEmail(String email) {
        String query = "SELECT name FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error("Query failed for getUserNameByEmail: {}", email, e);
            throw new FrameworkException("Database query failed", e);
        }
    }
}