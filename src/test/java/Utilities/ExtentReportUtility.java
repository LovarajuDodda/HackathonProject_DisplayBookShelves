package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportUtility {

    private static ExtentReports extent;
    private static ExtentTest test;

    // Initialize ExtentReports only once
    public static void initReport(String reportPath) {
        if (extent == null) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Report");
            sparkReporter.config().setReportName("Selenium Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
        }
    }

    // Create a new test entry
    public static ExtentTest createTest(String testName, String description) {
        test = extent.createTest(testName, description);
        return test;
    }

    // Get the current test object
    public static ExtentTest getTest() {
        return test;
    }

    // Flush the report (write to file)
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
