package com.akash.nexusqa.config;

import com.akash.nexusqa.exceptions.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties;

    private ConfigManager() {
        properties = new Properties();
        String path = "src/test/resources/config/config.properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
            logger.info("Configuration loaded successfully from {}", path);
        } catch (IOException e) {
            logger.error("Failed to load configuration file at {}", path, e);
            throw new ConfigurationException("Could not load config file at: " + path, e);
        }
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getBrowser() {
        String override = System.getProperty("browser");
        return override != null ? override : properties.getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        String override = System.getProperty("headless");
        if (override != null) {
            return Boolean.parseBoolean(override);
        }
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }

    public String getBaseUrl() {
        String baseUrl = properties.getProperty("baseUrl");
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new ConfigurationException("baseUrl is missing or empty in config.properties");
        }
        return baseUrl;
    }

    public int getImplicitWaitSeconds() {
        return Integer.parseInt(properties.getProperty("implicitWaitSeconds", "5"));
    }

    public int getExplicitWaitSeconds() {
        return Integer.parseInt(properties.getProperty("explicitWaitSeconds", "10"));
    }

    public boolean isUseGrid() {
        return Boolean.parseBoolean(properties.getProperty("useGrid", "false"));
    }

    public String getDbUrl() {
        String override = System.getProperty("db.url");
        return override != null ? override : properties.getProperty("dbUrl");
    }

    public String getDbUsername() {
        String override = System.getProperty("db.user");
        return override != null ? override : properties.getProperty("dbUsername");
    }

    public String getDbPassword() {
        String override = System.getProperty("db.password");
        return override != null ? override : properties.getProperty("dbPassword");
    }
}