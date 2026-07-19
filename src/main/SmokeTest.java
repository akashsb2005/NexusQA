package com.akash.nexusqa.tests.ui;

import com.akash.nexusqa.config.ConfigManager;
import com.akash.nexusqa.core.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

public class SmokeTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.getDriver().get(ConfigManager.getInstance().getBaseUrl());
    }

    @Test
    public void titleShouldLoadOnSauceDemo() {
        WebDriver driver = DriverFactory.getDriver();
        Assert.assertTrue(driver.getTitle().contains("Swag Labs"),
                "Expected SauceDemo title to load correctly");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}