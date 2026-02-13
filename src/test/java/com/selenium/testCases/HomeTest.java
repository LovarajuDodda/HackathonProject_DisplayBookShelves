package com.selenium.testCases;

import Utilities.ConfigReader;
import com.selenium.testBase.BaseTest;
import com.selenium.pageObjects.HomePage;
import org.testng.annotations.Test;

public class HomeTest extends BaseTest {

    @Test
    public void testSearchAmazon() {
        try {
            HomePage homePage = new HomePage(driver);
            homePage.openAmazon();
            test.pass("Opened Amazon homepage");
            homePage.searchAmazon(ConfigReader.get("query1"));
            test.pass("Searched for Laptop successfully");
        } catch (Exception e)
        {
            test.fail("Test failed: " + e.getMessage());
        }
    }
}
