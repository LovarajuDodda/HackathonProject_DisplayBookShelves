package com.selenium.pageObjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    WebElement searchBox;

    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    WebElement searchButton;

    public void openAmazon() {
        driver.get("https://www.amazon.in/");

    }

    public void searchAmazon(String query) {
        searchBox.sendKeys(query);
        searchButton.click();
    }
}
