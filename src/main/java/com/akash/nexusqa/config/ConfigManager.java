package com.akash.nexusqa.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static ConfigManager instance;
    private final Properties properties;

    // private constructor -> nobody outside this class can "new" it
    private ConfigManager() {
        properties = new Properties();
        String path = "src/test/resources/config/config.properties";
        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config file at: " + path, e);
        }
    }

    // single global access point (classic Singleton)
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }

    public String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public int getImplicitWaitSeconds() {
        return Integer.parseInt(properties.getProperty("implicitWaitSeconds", "5"));
    }

    public int getExplicitWaitSeconds() {
        return Integer.parseInt(properties.getProperty("explicitWaitSeconds", "10"));
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
}