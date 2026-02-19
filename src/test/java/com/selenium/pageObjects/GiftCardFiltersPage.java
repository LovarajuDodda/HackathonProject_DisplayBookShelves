package com.selenium.pageObjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class GiftCardFiltersPage extends BasePage {

    @FindBy(xpath = "//input[@id='twotabsearchtextbox']")
    WebElement searchBox;

    @FindBy(xpath = "//input[@id='nav-search-submit-button']")
    WebElement searchButton;


    @FindBy(xpath = "//span[text()='Wedding' or text()='Birthday']")
    WebElement weddingGiftCard;

    @FindBy(xpath = "//span[text()='eGift Cards']")
    WebElement eGiftCard;

    @FindBy(xpath = "(//div[@data-component-type='s-search-result' and not(contains(@class,'AdHolder'))])[1]")
    WebElement firstGiftCard;

    @FindBy(id = "gc-delivery-mechanism-button-Email-announce")
    WebElement emailButton;

    @FindBy(id = "gc-email-recipients")
    WebElement mailId;

    @FindBy(xpath = "//div[@id='recipients-error']/div/div")
    WebElement errorMsg;

    public GiftCardFiltersPage(WebDriver driver) {
        super(driver);
    }

    public void searchGiftCard(String query) throws InterruptedException {
        searchBox.sendKeys(query);
        searchButton.click();
        Thread.sleep(5000);
    }

    public void applyFiltersAndClickCard() throws InterruptedException {
        weddingGiftCard.click();
        eGiftCard.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(firstGiftCard));
        firstGiftCard.click();
        Thread.sleep(5000);
    }

    public void selectEmailDelivery(String mail) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(emailButton));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", emailButton);
        emailButton.click();
        mailId.sendKeys(mail+ Keys.TAB);
        System.out.println("Error Message :"+errorMsg.getText());


    }
}