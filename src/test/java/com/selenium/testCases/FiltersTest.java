package com.selenium.testCases;

import com.selenium.pageObjects.HomePage;
import com.selenium.pageObjects.FiltersPage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class FiltersTest extends BaseTest {

    @Test
    public void testSearchAndApplyFilters() throws InterruptedException {
        // Page objects use the driver from BaseTest
        HomePage homePage = new HomePage(driver);
        FiltersPage filtersPage = new FiltersPage(driver);

        homePage.searchAmazon("open bookshelves");
        filtersPage.setSliderToPrice(15000);   // simplified slider method
        filtersPage.setOutOfStock();
        filtersPage.displayFirstThreeItems();
    }
}
