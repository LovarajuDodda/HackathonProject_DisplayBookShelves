package com.selenium.testBase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseTest {
    // 1. Make driver static so it is shared by all test classes
    protected static WebDriver driver;

    // 2. Run this once before the entire Suite starts
    @BeforeSuite
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    // 3. Run this before EVERY @Test method to ensure we are on the homepage
    @BeforeMethod
    public void goToHome() {
        if (driver != null) {
            driver.get("https://www.amazon.in/");
        }
    }

    // 4. Run this once after all tests in the suite are finished
    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}