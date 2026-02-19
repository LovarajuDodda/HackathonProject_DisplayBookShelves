package com.selenium.testBase;

import Utilities.ConfigReader;
import Utilities.ExtentReportUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class BaseTest {
    protected static WebDriver driver;
    protected static Logger logger;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        try { Files.createDirectories(Path.of("reports/screenshots")); } catch (Exception ignored) {}
        try { Files.createDirectories(Path.of("logs/tests")); } catch (Exception ignored) {}

        // Initialize report once
        ExtentReportUtility.initReport();

        // Create logger for this class
        logger = LogManager.getLogger(getClass());
        String browser=ConfigReader.get("browser");
        // Browser
        switch (browser.toLowerCase()) {
            case "chrome":  driver = new ChromeDriver();  break;
            case "firefox": driver = new FirefoxDriver(); break;
            case "edge":    driver = new EdgeDriver();    break;
            default: throw new RuntimeException("Unsupported browser: " + browser);
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(Integer.parseInt(ConfigReader.get("implicitWait"))));
        driver.manage().window().maximize();

        logger.info("==== Suite started. Browser: {} ====", browser);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        String testName = method.getName();

        // Set per-test context (used by RoutingAppender)
        ThreadContext.put("testName", testName);

        // Start Extent test
        ExtentReportUtility.startTest(testName, "Executing test: " + testName);
        ExtentReportUtility.logInfo("Starting test: " + testName);

        // Navigate to base URL
        String baseUrl = ConfigReader.get("baseUrl");
        if (driver != null && baseUrl != null && !baseUrl.isBlank()) {
            driver.get(baseUrl);
            logger.info("Navigated to base URL: {}", baseUrl);
        }

        logger.info("==== Test START: {} ====", testName);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.info("Test PASSED: {}", testName);
                ExtentReportUtility.logPass("Test passed: " + testName);
                break;

            case ITestResult.FAILURE:
                logger.error("Test FAILED: {} | Error: ", testName, result.getThrowable());
                ExtentReportUtility.logFail("Test failed: " + testName + " | Error: " +
                        (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error"));
                break;

            case ITestResult.SKIP:
                logger.warn("Test SKIPPED: {}", testName);
                ExtentReportUtility.logSkip("Test skipped: " + testName);
                break;

            default:
                logger.info("Test finished with status: {} ({})", testName, result.getStatus());
        }

        // Clear per-test MDC to avoid leaking to next test
        ThreadContext.clearMap();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        logger.info("==== Suite finished ====");
        if (driver != null) {
            driver.quit();
            logger.info("Driver quit successfully");
        }
        ExtentReportUtility.flushReport();
    }
}