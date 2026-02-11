package com.selenium.pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CollectionsPage extends BasePage{

    @FindBy(xpath = "//select[@id='searchDropdownBox']")
    List<WebElement> collectionOptions;

    public CollectionsPage(WebDriver driver) {
        super(driver);
    }

    // Method to display collection options
    public void displayCollection() {
        for (WebElement option : collectionOptions) {
            System.out.println(option.getText());
        }
    }


}
