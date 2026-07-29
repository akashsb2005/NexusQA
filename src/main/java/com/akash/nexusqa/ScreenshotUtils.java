package com.akash.nexusqa.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            if (driver == null) {
                System.err.println("Skipping screenshot for " + testName + ": driver is null");
                return null;
            }

            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Paths.get(filePath));

            return filePath;
        } catch (Exception e) {
            // Catches IOException AND Selenium's runtime exceptions (NoSuchSessionException,
            // NoSuchWindowException, etc.) — a screenshot failure must never crash the test run.
            System.err.println("Screenshot capture failed for " + testName + ": " + e.getMessage());
            return null;
        }
    }
}
