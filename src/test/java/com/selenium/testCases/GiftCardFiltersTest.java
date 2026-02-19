package com.selenium.testCases;

import Utilities.ConfigReader;
import Utilities.ExtentReportUtility;
import com.selenium.pageObjects.GiftCardFiltersPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Set;

public class GiftCardFiltersTest extends BaseTest {

    @Test(description = "Search gift cards, apply filters, open details in new window, and select Email delivery")
    public void testGiftCardScenario() throws InterruptedException {
        try {
            logger.info("====== Starting GiftCardFiltersTest.testGiftCardScenario ======");
            ExtentReportUtility.logInfo("Starting Gift Card Scenario test");

            // 1. Search for gift cards
            HomePage homePage=new HomePage(driver);
            GiftCardFiltersPage giftPage = new GiftCardFiltersPage(driver);
            giftPage.searchGiftCard(ConfigReader.get("query2"));
            logger.info("Searched for Gift Cards with query: " + ConfigReader.get("query2"));
            ExtentReportUtility.logPass("Searched for Gift Cards");

            // 2. Apply filters & click a gift card
            GiftCardFiltersPage filtersPage = new GiftCardFiltersPage(driver);
            filtersPage.applyFiltersAndClickCard();
            logger.info("Applied filters and clicked on a gift card");
            ExtentReportUtility.logPass("Applied filters and clicked on a gift card");

            // 3. Switch to the newly opened window/tab
            String originalWindow = driver.getWindowHandle();
            boolean switched = switchToNewWindow(originalWindow, Duration.ofSeconds(5));
            if (!switched) {
                throw new RuntimeException("No new window found after clicking the gift card");
            }
            logger.info("Switched to gift card details window");
            ExtentReportUtility.logInfo("Switched to gift card details window");

            // 4. Verification: Check a typical element on details page

            boolean detailsHeaderVisible = homePage.isHeaderVisible();
            logger.info("Details header visible: " + detailsHeaderVisible);

            // Assertion
            Assert.assertTrue(detailsHeaderVisible, "Gift card details header not visible after switching window");

            // 5. Email delivery
            String toEmail = "12345@ab.cde";
            filtersPage.selectEmailDelivery(toEmail);
            logger.info("Selected Email delivery and entered recipient email: " + toEmail);
            ExtentReportUtility.logPass("Selected email delivery and entered recipient email");

            // 6. SUCCESS SCREENSHOT: Pass 'true' to save in 'passed' folder
            ExtentReportUtility.attachScreenshot(driver, "testGiftCardScenario_Pass", true);

            ExtentReportUtility.logInfo("Ending Gift Card Scenario test");
            logger.info("====== Finished GiftCardFiltersTest.testGiftCardScenario ======");

        } catch (AssertionError ae) {
            logger.error("Assertion failed in GiftCardFiltersTest: " + ae.getMessage());
            ExtentReportUtility.logFail("Assertion failed: " + ae.getMessage());

            // 7. FAILURE SCREENSHOT: Pass 'false' to save in 'failed' folder
            ExtentReportUtility.attachScreenshot(driver, "testGiftCardScenario_Fail", false);

            throw ae;

        } catch (Exception e) {
            logger.error("Exception in GiftCardFiltersTest: " + e.getMessage());
            ExtentReportUtility.logFail("Test failed: " + e.getMessage());

            // 8. EXCEPTION SCREENSHOT: Pass 'false' to save in 'failed' folder
            ExtentReportUtility.attachScreenshot(driver, "testGiftCardScenario_Error", false);

            throw e;
        }
    }

    /**
     * Helper: tries to switch to a window that's not the original, within a timeout.
     */
    private boolean switchToNewWindow(String originalHandle, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            Set<String> handles = driver.getWindowHandles();
            for (String h : handles) {
                if (!h.equals(originalHandle)) {
                    driver.switchTo().window(h);
                    return true;
                }
            }
            try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        }
        return false;
    }
}