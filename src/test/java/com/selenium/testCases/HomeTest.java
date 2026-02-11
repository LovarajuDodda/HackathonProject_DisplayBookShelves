package com.selenium.testCases;

import com.selenium.testBase.BaseTest;
import com.selenium.pageObjects.HomePage;
import org.testng.annotations.Test;

public class HomeTest extends BaseTest {

    @Test
    public void testSearchAmazon() {
        HomePage home = new HomePage(driver);

        home.openAmazon();
        home.searchAmazon("open bookshelves");
    }
}
