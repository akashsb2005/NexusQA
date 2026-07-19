package com.akash.nexusqa.tests.ui;

import com.akash.nexusqa.config.ConfigManager;
import com.akash.nexusqa.core.DriverFactory;
import com.akash.nexusqa.pages.LoginPage;
import com.akash.nexusqa.pages.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        DriverFactory.getDriver().get(ConfigManager.getInstance().getBaseUrl());
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @Test
    public void validLoginShouldLandOnProductsPage() {
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertEquals(productsPage.getPageTitle(), "Products");
    }

    @Test
    public void lockedOutUserShouldSeeErrorMessage() {
        loginPage.loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Expected locked-out error message to be shown");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}