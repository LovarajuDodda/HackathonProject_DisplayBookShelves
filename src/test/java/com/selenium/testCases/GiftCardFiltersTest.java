package com.selenium.testCases;

import com.selenium.pageObjects.GiftCardFiltersPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public class GiftCardFiltersTest extends BaseTest {
    public Properties p;

    @Test
    public void testGiftCardScenario() throws InterruptedException, IOException {

        FileReader file=new FileReader("./src//test//resources//config.properties");
        p=new Properties();
        p.load(file);

        GiftCardFiltersPage giftPage = new GiftCardFiltersPage(driver);
        giftPage.searchGiftCard(p.getProperty("query2"));

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

        filtersPage.selectEmailDelivery(p.getProperty("mail"));
        Thread.sleep(3000);
    }
}