package com.selenium.pageObjects;

import Utilities.ConfigReader;
import com.selenium.testBase.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    // Constructor passes WebDriver to BasePage
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Locate the search box
    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    private WebElement searchBox;

    // Locate the search button
    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    private WebElement searchButton;

    // Navigate to Amazon homepage
    public void openAmazon() {
        driver.get((ConfigReader.get("baseUrl")));
    }

    // Perform a search on Amazon
    public void searchAmazon(String query) {
        searchBox.sendKeys(query);
        searchButton.click();
    }
}
