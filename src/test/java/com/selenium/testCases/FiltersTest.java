package com.selenium.testCases;

import Utilities.ExtentReportUtility;
import com.selenium.pageObjects.HomePage;
import com.selenium.pageObjects.FiltersPage;
import com.selenium.testBase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FiltersTest extends BaseTest {

    @Test(description = "Search 'open bookshelves', apply price + OOS filters, and list first three results")
    public void testSearchAndApplyFilters() throws InterruptedException {
        final String query = "open bookshelves";

        try {
            logger.info("====== Starting FiltersTest.testSearchAndApplyFilters ======");
            ExtentReportUtility.logInfo("Starting Filters Test: search and apply filters on '" + query + "'");

            HomePage homePage = new HomePage(driver);
            FiltersPage filtersPage = new FiltersPage(driver);

            // Perform search
            homePage.searchAmazon(query);
            logger.info("Searched for '{}'", query);
            ExtentReportUtility.logPass("Searched for '" + query + "'");

            // Apply filters
            filtersPage.setSliderToPrice(15000);
            logger.info("Adjusted price slider to ₹15,000 (approx)");
            ExtentReportUtility.logPass("Adjusted price slider to ₹15,000");

            filtersPage.setOutOfStock();
            logger.info("Applied 'Out of Stock' filter");
            ExtentReportUtility.logPass("Applied 'Out of Stock' filter");

            boolean hasResults = homePage.isShowingFilteredContent();
            logger.info("Results found after applying filters: {}", hasResults);
            Assert.assertTrue(hasResults, "No results found after applying filters.");

            // Display results
            filtersPage.displayFirstThreeItems();
            logger.info("Displayed first three search results in logs");
            ExtentReportUtility.logPass("Displayed first three search results");

            ExtentReportUtility.attachScreenshot(driver, "testSearchAndApplyFilters_Success", true);

            ExtentReportUtility.logInfo("Completed Filters Test successfully.");
            logger.info("====== Finished FiltersTest.testSearchAndApplyFilters ======");

        } catch (AssertionError ae) {
            logger.error("Assertion failed in FiltersTest: {}", ae.getMessage(), ae);
            ExtentReportUtility.logFail("Assertion failed: " + ae.getMessage());

            ExtentReportUtility.attachScreenshot(driver, "testSearchAndApplyFilters_Fail", false);

            throw ae;

        } catch (Exception e) {
            logger.error("Exception in FiltersTest: ", e);
            ExtentReportUtility.logFail("Test failed: " + e.getMessage());

            ExtentReportUtility.attachScreenshot(driver, "testSearchAndApplyFilters_Fail", false);

            throw e;
        }
    }
}