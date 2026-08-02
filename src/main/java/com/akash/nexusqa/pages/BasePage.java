package com.akash.nexusqa.pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.akash.nexusqa.config.ConfigManager;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigManager.getInstance().getExplicitWaitSeconds()));
        PageFactory.initElements(driver, this);
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    protected void type(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
/**
     * Attempts to click using the primary locator; if not found within a short
     * timeout, falls back to an alternate locator before giving up.
     */
    protected void clickWithFallback(org.openqa.selenium.By primaryLocator, org.openqa.selenium.By fallbackLocator) {
        try {
            org.openqa.selenium.WebElement element = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(3))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(primaryLocator));
            element.click();
        } catch (org.openqa.selenium.TimeoutException e) {
            org.apache.logging.log4j.LogManager.getLogger(BasePage.class)
                    .warn("Primary locator {} not found, falling back to {}", primaryLocator, fallbackLocator);
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(fallbackLocator)).click();
        }
    }
}