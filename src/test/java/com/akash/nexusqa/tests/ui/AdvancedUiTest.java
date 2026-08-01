package com.akash.nexusqa.tests.ui;

import com.akash.nexusqa.core.DriverFactory;
import com.akash.nexusqa.pages.AdvancedUiPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class AdvancedUiTest {

    private AdvancedUiPage getPage() {
        return new AdvancedUiPage(DriverFactory.getDriver());
    }

    @Test(groups = {"regression"})
    public void shouldReadTextInsideIframe() {
        DriverFactory.getDriver().get("https://practice.expandtesting.com/iframe");
        String frameText = getPage().getTextInsideFrame("mce_0_ifr", By.id("tinymce"));
        Assert.assertTrue(frameText.length() > 0, "Expected non-empty text inside the iframe");
    }

    @Test(groups = {"regression"})
    public void shouldHandleNewWindow() {
        DriverFactory.getDriver().get("https://practice.expandtesting.com/windows");
        String newWindowTitle = getPage().switchToNewWindowAndGetTitle(By.linkText("Click Here"));
        Assert.assertTrue(newWindowTitle.toLowerCase().contains("new window"),
                "Expected new window title to reference 'new window'");
    }

    @Test(groups = {"regression"})
    public void shouldAcceptJavaScriptAlert() {
        DriverFactory.getDriver().get("https://practice.expandtesting.com/js-dialogs");
        String alertText = getPage().acceptAlertAndGetText(By.id("js-alert"));
        Assert.assertNotNull(alertText);
    }

    @Test(groups = {"regression"})
    public void shouldHandleDragAndDrop() {
        DriverFactory.getDriver().get("https://practice.expandtesting.com/drag-and-drop");
        getPage().dragAndDrop(By.id("column-a"), By.id("column-b"));
        String columnBText = DriverFactory.getDriver().findElement(By.id("column-b")).getText();
        Assert.assertEquals(columnBText, "A", "Expected column A's content to now be in column B");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        DriverFactory.quitDriver();
        Thread.sleep(3000);
    }
}