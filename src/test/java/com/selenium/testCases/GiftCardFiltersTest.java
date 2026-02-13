package com.selenium.testCases;

import Utilities.ConfigReader;
import com.selenium.pageObjects.GiftCardFiltersPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

import java.util.Set;

public class GiftCardFiltersTest extends BaseTest {

    @Test
    public void testGiftCardScenario() throws InterruptedException {
        try {
            GiftCardFiltersPage giftPage = new GiftCardFiltersPage(driver);
            giftPage.searchGiftCard(ConfigReader.get("query2"));
            test.pass("Searched for Gift Cards");

            GiftCardFiltersPage filtersPage = new GiftCardFiltersPage(driver);
            filtersPage.applyFiltersAndClickCard();
            test.pass("Applied filters and clicked on a gift card");

            // Switch to new window
            String originalWindow = driver.getWindowHandle();
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    test.pass("Switched to gift card details window");
                    break;
                }
            }

            filtersPage.selectEmailDelivery("12345@ab.cde");
            test.pass("Selected email delivery and entered recipient email");

            Thread.sleep(3000);
        } catch (Exception e) {
            test.fail("Test failed: " + e.getMessage());
        }
    }
}
