package com.selenium.testCases;

import com.selenium.pageObjects.HomePage;
import com.selenium.pageObjects.FiltersPage;
import com.selenium.testBase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class FiltersTest extends BaseTest {

    @Test
    public void testSearchAndApplyFilters() throws InterruptedException {

        // Initialize page objects
        HomePage homePage = new HomePage(driver);
        FiltersPage filtersPage = new FiltersPage(driver);

        // Test flow
        homePage.openAmazon();
        homePage.searchAmazon("open bookshelves");
        filtersPage.setSliderToPrice(15000);
        filtersPage.setOutOfStock();
        filtersPage.displayFirstThreeItems();

        // Quit driver at the end
        driver.quit();
    }
}

