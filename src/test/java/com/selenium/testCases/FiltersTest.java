package com.selenium.testCases;

import com.selenium.pageObjects.HomePage;
import com.selenium.pageObjects.FiltersPage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FiltersTest extends BaseTest {
    public Properties p;

    @Test
    public void testSearchAndApplyFilters() throws InterruptedException, IOException {
        // Page objects use the driver from BaseTest
        HomePage homePage = new HomePage(driver);
        FiltersPage filtersPage = new FiltersPage(driver);
        FileReader file=new FileReader("./src//test//resources//config.properties");
        p=new Properties();
        p.load(file);

        homePage.searchAmazon(p.getProperty("query1"));
        filtersPage.setSliderToPrice(15000);   // simplified slider method
        filtersPage.setOutOfStock();
        filtersPage.displayFirstThreeItems();
    }
}
