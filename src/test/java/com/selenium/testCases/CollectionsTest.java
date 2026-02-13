package com.selenium.testCases;

import com.selenium.pageObjects.CollectionsPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class CollectionsTest extends BaseTest {

    @Test
    public void testCollectionOptions() {
        try {
            CollectionsPage cp = new CollectionsPage(driver);

            cp.displayCollection();
            test.pass("Displayed collection options successfully");
        } catch (Exception e) {
            test.fail("Test failed: " + e.getMessage());
        }
    }
}
