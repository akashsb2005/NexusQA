package com.akash.nexusqa.tests.api;

import com.akash.nexusqa.db.DBUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserDbValidationTest {

    @Test(groups = {"db", "regression"})
    public void standardUserShouldExistInDatabase() {
        boolean exists = DBUtils.userExistsByEmail("standard_user@example.com");
        Assert.assertTrue(exists, "Expected standard_user to exist in the users table");
    }

    @Test(groups = {"db", "regression"})
    public void userNameShouldMatchExpectedValue() {
        String name = DBUtils.getUserNameByEmail("standard_user@example.com");
        Assert.assertEquals(name, "Standard User", "Expected DB name to match seeded value");
    }

    @Test(groups = {"db", "regression"})
    public void nonExistentUserShouldNotBeFound() {
        boolean exists = DBUtils.userExistsByEmail("nobody_real@example.com");
        Assert.assertFalse(exists, "Expected non-existent user to not be found in the users table");
    }
}