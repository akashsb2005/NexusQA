package com.akash.nexusqa.listeners;

import com.akash.nexusqa.core.DriverFactory;
import com.akash.nexusqa.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String screenshotPath = ScreenshotUtils.captureScreenshot(DriverFactory.getDriver(), testName);
        System.out.println("Test FAILED: " + testName +
                (screenshotPath != null ? " | Screenshot: " + screenshotPath : " | Screenshot capture failed"));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("===== Starting Suite: " + context.getName() + " =====");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("===== Finished Suite: " + context.getName() + " =====");
        System.out.println("Passed: " + context.getPassedTests().size() +
                " | Failed: " + context.getFailedTests().size() +
                " | Skipped: " + context.getSkippedTests().size());
    }
}