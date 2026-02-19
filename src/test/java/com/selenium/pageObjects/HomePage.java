package com.selenium.pageObjects;

import Utilities.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    private WebElement searchBox;

    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    private WebElement searchButton;

    @FindBy(css = "span.a-color-state, span.a-color-base.a-text-bold")
    private List<WebElement> resultsHeaders;

    @FindBy(css = ".s-main-slot .s-result-item")
    private List<WebElement> filteredContent;

    @FindBy(css = "h1, h2, [data-testid='gc-title']")
    private List<WebElement> detailsHeader;

    public void openAmazon() {
        driver.get(ConfigReader.get("baseUrl"));
    }

    public void searchAmazon(String query) {
        searchBox.sendKeys(query);
        searchButton.click();
    }

    public boolean isResultsHeaderDisplayed() {
        return !resultsHeaders.isEmpty();
    }

    public boolean isShowingFilteredContent(){
        return !filteredContent.isEmpty();
    }

    public boolean isHeaderVisible(){
        return !detailsHeader.isEmpty();
    }
}