package com.selenium.pageObjects;

import com.selenium.pageObjects.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class FiltersPage extends BasePage {

    // Price slider (upper bound)
    @FindBy(id = "p_36/range-slider_slider-item_upper-bound-slider")
    private WebElement priceSlider;

    // Availability section
    @FindBy(id = "p_n_availability/1318483031")
    private WebElement availabilitySection;

    // Out of stock filter icon
    @FindBy(xpath = "//div[@id='p_n_availability/1318483031']//i")
    private WebElement outOfStockFilter;

    // All search result containers
    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    private List<WebElement> searchResults;

    // Product titles inside search results
    @FindBy(xpath = "//div[@data-component-type='s-search-result']//h2//span")
    private List<WebElement> productTitles;

    // Product prices inside search results
    @FindBy(css = ".a-price .a-price-whole")
    private List<WebElement> productPrices;

    public FiltersPage(WebDriver driver) {
        super(driver);
    }

    // Adjust price slider to target price
    public void setSliderToPrice(int targetPrice) throws InterruptedException {
        fluentWait.until(ExpectedConditions.visibilityOf(priceSlider));

        int minSteps = Integer.parseInt(priceSlider.getAttribute("min"));
        int maxSteps = Integer.parseInt(priceSlider.getAttribute("max"));
        System.out.println("Slider steps range: " + minSteps + " to " + maxSteps);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        int closestStep = -1;
        int closestDiff = Integer.MAX_VALUE;
        String closestText = "";

        for (int i = minSteps; i <= maxSteps; i++) {
            js.executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    priceSlider, i
            );

            Thread.sleep(30);
            String currentText = priceSlider.getAttribute("aria-valuetext");
            if (currentText != null && currentText.startsWith("₹")) {
                try {
                    int currentValue = Integer.parseInt(currentText.replaceAll("[^0-9]", ""));
                    int diff = Math.abs(currentValue - targetPrice);
                    if (diff < closestDiff) {
                        closestDiff = diff;
                        closestStep = i;
                        closestText = currentText;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        if (closestStep != -1) {
            js.executeScript(
                    "arguments[0].value = arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    priceSlider, closestStep
            );
            System.out.println("Slider set near ₹" + targetPrice + " (actual: " + closestText + ", step=" + closestStep + ")");
        } else {
            System.out.println("Could not find a step close to ₹" + targetPrice);
        }

        Thread.sleep(3000);
    }

    // Apply "Out of Stock" filter
    public void setOutOfStock() {
        fluentWait.until(ExpectedConditions.visibilityOf(availabilitySection));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", availabilitySection);
        fluentWait.until(ExpectedConditions.elementToBeClickable(outOfStockFilter)).click();
        System.out.println("Included out of stock");
    }

    // Display first three search results
    public void displayFirstThreeItems() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 200)");
        fluentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@data-component-type='s-search-result']")
        ));

        Thread.sleep(5000);

        int count = Math.min(3, searchResults.size());
        for (int i = 0; i < count; i++) {
            String title = productTitles.size() > i ? productTitles.get(i).getText() : "Title not available";
            String price = productPrices.size() > i ? productPrices.get(i).getText() : "Price not available";
            System.out.println((i + 1) + ". " + title + " - " + price);
        }
    }
}

