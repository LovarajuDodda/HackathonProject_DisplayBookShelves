package com.selenium.testBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import Utilities.ConfigReader;   // your utility package
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.time.Duration;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class BaseTest {
    protected static WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeSuite
    @Parameters({"browser"})
    public void setUpSuite(@Optional("chrome") String browser) {
        // Browser from XML
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new RuntimeException("Unsupported browser: " + browser);
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Integer.parseInt(ConfigReader.get("implicitWait")))
        );
        driver.manage().window().maximize();

        // ExtentReports setup
        ExtentSparkReporter spark = new ExtentSparkReporter(ConfigReader.get("reportPath"));
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setReportName("Selenium Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeMethod
    public void startTest(Method method) {
        test = extent.createTest(method.getName(), "Executing test: " + method.getName());

        if (driver != null) {
            driver.get(ConfigReader.get("baseUrl"));
        }
    }

    @AfterMethod
    public void captureFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String path = ConfigReader.get("screenshotPath") + result.getName() + ".png";
                Files.copy(screenshot.toPath(), new File(path).toPath());
                test.fail("Test failed: " + result.getThrowable().getMessage());
                test.addScreenCaptureFromPath(path);
            } catch (IOException e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
        if (extent != null) {
            extent.flush();
        }
    }
}
