package com.akash.nexusqa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Set;

public class AdvancedUiPage extends BasePage {

    public AdvancedUiPage(WebDriver driver) {
        super(driver);
    }

    public String getTextInsideFrame(String frameId, By elementLocator) {
        driver.switchTo().frame(frameId);
        String text = driver.findElement(elementLocator).getText();
        driver.switchTo().defaultContent();
        return text;
    }

    public String switchToNewWindowAndGetTitle(By triggerLocator) {
        String originalWindow = driver.getWindowHandle();
        WebElement trigger = driver.findElement(triggerLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);

        wait.until(d -> d.getWindowHandles().size() > 1);

        Set<String> allWindows = driver.getWindowHandles();
        for (String handle : allWindows) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        String newWindowTitle = driver.getTitle();
        driver.close();
        driver.switchTo().window(originalWindow);
        return newWindowTitle;
    }

    public String acceptAlertAndGetText(By triggerLocator) {
        WebElement trigger = driver.findElement(triggerLocator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", trigger);
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }

    public void dragAndDrop(By sourceLocator, By targetLocator) {
        WebElement source = driver.findElement(sourceLocator);
        WebElement target = driver.findElement(targetLocator);
        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).build().perform();
    }
}