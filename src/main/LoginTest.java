$content = @"
package com.akash.nexusqa.tests.ui;

import com.akash.nexusqa.config.ConfigManager;
import com.akash.nexusqa.core.DriverFactory;
import com.akash.nexusqa.pages.LoginPage;
import com.akash.nexusqa.pages.ProductsPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest {

    @BeforeMethod
    public void setUp() {
        DriverFactory.getDriver().get(ConfigManager.getInstance().getBaseUrl());
    }

    private LoginPage getLoginPage() {
        WebDriver driver = DriverFactory.getDriver();
        return new LoginPage(driver);
    }

    @Test(groups = {"smoke", "regression"})
    public void validLoginShouldLandOnProductsPage() {
        ProductsPage productsPage = getLoginPage().loginAs("standard_user", "secret_sauce");
        Assert.assertEquals(productsPage.getPageTitle(), "Products");
    }

    @Test(groups = {"regression"})
    public void lockedOutUserShouldSeeErrorMessage() {
        getLoginPage().loginAs("locked_out_user", "secret_sauce");
        Assert.assertTrue(getLoginPage().getErrorMessage().contains("locked out"),
                "Expected locked-out error message to be shown");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            {"standard_user", "wrong_password", "Username and password do not match"},
            {"invalid_user", "secret_sauce", "Username and password do not match"},
            {"", "secret_sauce", "Username is required"},
            {"standard_user", "", "Password is required"}
        };
    }

    @Test(dataProvider = "invalidLoginData", groups = {"regression"})
    public void invalidLoginShouldShowCorrectError(String username, String password, String expectedErrorSnippet) {
        getLoginPage().loginAs(username, password);
        Assert.assertTrue(getLoginPage().getErrorMessage().contains(expectedErrorSnippet),
                "Expected error message to contain: " + expectedErrorSnippet);
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
"@

[System.IO.File]::WriteAllText("$PWD\src\test\java\com\akash\nexusqa\tests\ui\LoginTest.java", $content)    