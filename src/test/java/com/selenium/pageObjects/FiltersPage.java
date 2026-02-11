package com.selenium.pageObjects;

import com.selenium.pageObjects.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class FiltersPage extends BasePage {

    // Price slider (upper bound)
    @FindBy(id = "p_36/range-slider_slider-item_upper-bound-slider")
    WebElement priceSlider;

    // Availability section
    @FindBy(id = "p_n_availability/1318483031")
    WebElement availabilitySection;

    // Out of stock filter icon
    @FindBy(xpath = "//div[@id='p_n_availability/1318483031']//i")
    WebElement outOfStockFilter;

    // All search result containers
    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    List<WebElement> searchResults;

    // Product titles inside search results
    @FindBy(xpath = "//div[@data-component-type='s-search-result']//h2//span")
    List<WebElement> productTitles;

    // Product prices inside search results
    @FindBy(css = ".a-price .a-price-whole")
    List<WebElement> productPrices;

    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public FiltersPage(WebDriver driver) {
        super(driver);
    }

    // Adjust price slider to target price
    public void setSliderToPrice(int targetPrice) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int min = Integer.parseInt(priceSlider.getAttribute("min"));
        int max = Integer.parseInt(priceSlider.getAttribute("max"));

        for (int i = min; i <= max; i++) {
            js.executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    priceSlider, i
            );

            String currentText = priceSlider.getAttribute("aria-valuetext");
            if (currentText != null && currentText.startsWith("₹")) {
                int currentValue = Integer.parseInt(currentText.replaceAll("[^0-9]", ""));

                // Stop once we reach or exceed the target
                if (currentValue >= targetPrice) {
                    System.out.println("Slider set to: " + currentText);
                    break;
                }
            }
        }
    }

    // Apply "Out of Stock" filter
    public void setOutOfStock() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", availabilitySection);

        // Click the "Out of Stock" filter
        outOfStockFilter.click();

        System.out.println("Included out of stock");
    }

    // Display first three search results
    public void displayFirstThreeItems() {
        // Scroll down a bit to ensure results are visible
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200)");

        // Explicit wait for search results to be present
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@data-component-type='s-search-result']")
        ));

        // Display first three items
        int count = Math.min(3, searchResults.size());
        for (int i = 0; i < count; i++) {
            String title = productTitles.size() > i ? productTitles.get(i).getText() : "Title not available";
            String price = productPrices.size() > i ? productPrices.get(i).getText() : "Price not available";
            System.out.println((i + 1) + ". " + title + " - " + price);
        }
    }


}
