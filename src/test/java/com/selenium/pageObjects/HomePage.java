package com.selenium.pageObjects;

import com.selenium.pageObjects.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void openAmazon() {
        driver.get("https://www.amazon.in/");
        // Ensure search box is present before continuing
        fluentWait.until(ExpectedConditions.presenceOfElementLocated(By.id("twotabsearchtextbox")));
    }

    public void searchAmazon(String query) {
        fluentWait.until(ExpectedConditions.visibilityOf(searchBox)).sendKeys(query);
        fluentWait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}
