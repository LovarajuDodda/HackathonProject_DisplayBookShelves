package com.selenium.testCases;

import Utilities.ConfigReader;
import Utilities.ExtentReportUtility;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeTest extends BaseTest {

    @Test(description = "Searches Amazon for the configured query and verifies results page loaded")
    public void testSearchAmazon() {
        final String query = ConfigReader.get("query1");

        try {
            logger.info("====== Starting HomeTest.testSearchAmazon ======");
            ExtentReportUtility.logInfo("****** Starting the testCase *******");

            HomePage homePage = new HomePage(driver);

            // Open Amazon
            homePage.openAmazon();
            logger.info("Opened Amazon homepage");
            ExtentReportUtility.logPass("Opened Amazon homepage");

            // Search
            homePage.searchAmazon(query);
            logger.info("Searched for: " + query);
            ExtentReportUtility.logPass("Searched for '" + query + "' successfully");

            // Validation: Delegated to Page Object
            boolean resultsVisible = homePage.isResultsHeaderDisplayed();

            logger.info("Results header present: " + resultsVisible);

            // Assertion
            Assert.assertTrue(resultsVisible, "Results header not visible after searching for: " + query);

            // SUCCESS SCREENSHOT
            ExtentReportUtility.attachScreenshot(driver, "testSearchAmazon_Pass", true);

            ExtentReportUtility.logInfo("***** Ending the testCase *********");
            logger.info("====== Finished HomeTest.testSearchAmazon ======");

        } catch (AssertionError ae) {
            logger.error("Assertion failed in HomeTest.testSearchAmazon: " + ae.getMessage());
            ExtentReportUtility.logFail("Assertion failed: " + ae.getMessage());

            // FAILURE SCREENSHOT
            ExtentReportUtility.attachScreenshot(driver, "testSearchAmazon_Fail", false);
            throw ae;

        } catch (Exception e) {
            logger.error("Exception in HomeTest.testSearchAmazon: " + e.getMessage());
            ExtentReportUtility.logFail("Test failed: " + e.getMessage());

            // ERROR SCREENSHOT
            ExtentReportUtility.attachScreenshot(driver, "testSearchAmazon_Error", false);
            throw e;
        }
    }
}