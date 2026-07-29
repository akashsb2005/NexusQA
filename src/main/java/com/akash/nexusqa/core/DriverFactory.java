package com.akash.nexusqa.core;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.akash.nexusqa.config.ConfigManager;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();

        if (driver != null && !isSessionAlive(driver)) {
            System.err.println("Detected dead browser session - discarding and creating a fresh one.");
            quitDriver();
            driver = null;
        }

        if (driver == null) {
            initDriver();
        }

        return driverThreadLocal.get();
    }

    private static boolean isSessionAlive(WebDriver driver) {
        try {
            driver.getCurrentUrl();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void initDriver() {
        String browser = ConfigManager.getInstance().getBrowser().toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "firefox":
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (ConfigManager.getInstance().isHeadless()) {
                    ffOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(ffOptions);
                break;

            case "edge":
                driver = new EdgeDriver();
                break;

            case "chrome":
            default:
                System.setProperty("webdriver.chrome.driver", "C:\\ChromeForTesting\\chromedriver-win64\\chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setBinary("C:\\ChromeForTesting\\chrome-win64\\chrome.exe");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-background-networking");
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36");
                if (ConfigManager.getInstance().isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driverThreadLocal.set(driver);
    }

    public static void quitDriver() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.err.println("Error while quitting driver (session likely already dead): " + e.getMessage());
        } finally {
            driverThreadLocal.remove();
            killOrphanedProcesses();
        }
    }

    private static void killOrphanedProcesses() {
        try {
            Runtime.getRuntime().exec(new String[]{"taskkill", "/F", "/IM", "chromedriver.exe", "/T"});
        } catch (Exception ignored) {
        }
    }
}