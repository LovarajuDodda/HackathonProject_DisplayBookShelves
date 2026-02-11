package com.selenium.testCases;

import com.selenium.pageObjects.CollectionsPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class CollectionsTest extends BaseTest {

    @Test
    public void testCollectionOptions() {
        HomePage home = new HomePage(driver);

        home.openAmazon();
        CollectionsPage cp = new CollectionsPage(driver);
        cp.displayCollection();
    }

}
