package com.selenium.testCases;

import com.selenium.pageObjects.GiftCardPage;
import com.selenium.pageObjects.HomePage;
import com.selenium.testBase.BaseTest;
import org.testng.annotations.Test;

public class GiftCardTest extends BaseTest {
    @Test
    public void searchGift(){
        GiftCardPage giftPage = new GiftCardPage(driver);
        HomePage home = new HomePage(driver);

        home.openAmazon();
        giftPage.searchGift("Gift Cards");
    }
}
