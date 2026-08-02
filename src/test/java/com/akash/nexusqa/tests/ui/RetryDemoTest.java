package com.akash.nexusqa.tests.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RetryDemoTest {

    private static int attemptCount = 0;

    @Test(groups = {"regression"})
    public void demonstratesRetryOnFlakyFailure() {
        attemptCount++;
        System.out.println("Attempt number: " + attemptCount);
        Assert.assertTrue(attemptCount >= 2, "Intentionally fails on first attempt to prove retry works");
    }
}