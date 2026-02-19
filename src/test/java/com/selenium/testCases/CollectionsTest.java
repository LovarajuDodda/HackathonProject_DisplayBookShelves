package com.selenium.testCases;

import Utilities.ExtentReportUtility;
import com.selenium.pageObjects.CollectionsPage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class CollectionsTest extends BaseTest {

    @Test
    public void testCollectionOptions() throws InterruptedException {
        try {
            ExtentReportUtility.logInfo("Starting Collections test: Displaying collection options");

            CollectionsPage cp = new CollectionsPage(driver);

            cp.displayCollection();

            ExtentReportUtility.logPass("Displayed collection options successfully");

            ExtentReportUtility.attachScreenshot(driver, "testCollectionOptions", true);

            ExtentReportUtility.logInfo("Completed Collections test successfully");

        } catch (Exception e) {
            ExtentReportUtility.logFail("Test failed: " + e.getMessage());

            ExtentReportUtility.attachScreenshot(driver, "testCollectionOptions", false);

            throw e;
        }
    }
}