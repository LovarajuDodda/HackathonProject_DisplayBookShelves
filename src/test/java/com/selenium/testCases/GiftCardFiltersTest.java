package com.selenium.testCases;

import com.selenium.pageObjects.GiftCardFiltersPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

import java.util.Set;

public class GiftCardFiltersTest extends BaseTest {

    @Test
    public void testGiftCardScenario() throws InterruptedException {

        GiftCardFiltersPage giftPage = new GiftCardFiltersPage(driver);
        giftPage.searchGiftCard("Gift Cards");

        GiftCardFiltersPage filtersPage = new GiftCardFiltersPage(driver);
        filtersPage.applyFiltersAndClickCard();

        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();
        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(originalWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        filtersPage.selectEmailDelivery("12345@ab.cde");
        Thread.sleep(3000);
    }
}