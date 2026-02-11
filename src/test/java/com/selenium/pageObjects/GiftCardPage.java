package com.selenium.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GiftCardPage extends BasePage{
    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    WebElement searchBox;

    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    WebElement searchButton;

    public GiftCardPage(WebDriver driver) {
        super(driver);
    }

    public void searchGift(String query) {
        searchBox.sendKeys(query);
        searchButton.click();
    }
}
