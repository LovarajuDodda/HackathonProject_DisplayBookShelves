package com.selenium.testCases;

import com.selenium.pageObjects.HomePage;
import com.selenium.pageObjects.FiltersPage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class FiltersTest extends BaseTest {

    @Test
    public void testSearchAndApplyFilters() throws InterruptedException {
        try {
            // Page objects use the driver from BaseTest
            HomePage homePage = new HomePage(driver);
            FiltersPage filtersPage = new FiltersPage(driver);

            // Perform search
            homePage.searchAmazon("open bookshelves");
            test.pass("Searched for 'open bookshelves'");

            // Apply filters
            filtersPage.setSliderToPrice(15000);
            test.pass("Adjusted price slider to ₹15,000");

            filtersPage.setOutOfStock();
            test.pass("Applied 'Out of Stock' filter");

            // Display results
            filtersPage.displayFirstThreeItems();
            test.pass("Displayed first three search results");
        } catch (Exception e) {
            test.fail("Test failed: " + e.getMessage());
        }
    }
}
