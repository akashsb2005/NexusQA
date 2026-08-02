package com.akash.nexusqa.core;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.akash.nexusqa.config.ConfigManager;
import com.akash.nexusqa.exceptions.FrameworkException;

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();

        if (driver != null && !isSessionAlive(driver)) {
            logger.warn("Detected dead browser session - discarding and creating a fresh one.");
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
        logger.info("Initializing WebDriver for browser: {}", browser);
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
                ChromeOptions chromeOptions = new ChromeOptions();
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

                if (ConfigManager.getInstance().isUseGrid()) {
                    try {
                        driver = new org.openqa.selenium.remote.RemoteWebDriver(
                                new java.net.URL("http://localhost:4444/wd/hub"), chromeOptions);
                    } catch (java.net.MalformedURLException e) {
                        throw new FrameworkException("Invalid Grid URL", e);
                    }
                } else {
                    System.setProperty("webdriver.chrome.driver", "C:\\ChromeForTesting\\chromedriver-win64\\chromedriver.exe");
                    chromeOptions.setBinary("C:\\ChromeForTesting\\chrome-win64\\chrome.exe");
                    driver = new ChromeDriver(chromeOptions);
                }
                break;
        }

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driverThreadLocal.set(driver);
        logger.info("WebDriver session started successfully.");
    }

    public static void quitDriver() {
        try {
            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                driver.quit();
                logger.info("WebDriver session closed.");
            }
        } catch (Exception e) {
            logger.warn("Error while quitting driver (session likely already dead): {}", e.getMessage());
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