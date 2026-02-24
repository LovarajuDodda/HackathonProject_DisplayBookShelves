//package Utilities;
//
//import com.aventstack.extentreports.*;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.MediaEntityBuilder;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.nio.file.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class ExtentReportUtility {
//
//    private static ExtentReports extent;
//    private static final ThreadLocal<ExtentTest> testTL = new ThreadLocal<>();
//
//    // Holds the absolute path to the generated HTML report
//    private static Path reportFilePath;
//    // Holds the absolute path to the folder that contains the report
//    private static Path reportsDirAbs;           // e.g., <project>/reports
//    private static Path screenshotsDirAbs;       // e.g., <project>/reports/screenshots
//
//    // --- Initialize report (once) -------------------------------------------
//    public static synchronized ExtentReports initReport() {
//        if (extent == null) {
//            try {
//                // Base folder: <project>/reports
//                reportsDirAbs = Paths.get("reports").toAbsolutePath();
//                screenshotsDirAbs = reportsDirAbs.resolve("screenshots");
//
//                Files.createDirectories(screenshotsDirAbs);
//
//                // Timestamped report name within the same 'reports' folder
//                String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                reportFilePath = reportsDirAbs.resolve("ExtentReport_" + ts + ".html");
//
//                ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath.toString());
//                reporter.config().setReportName("Automation Execution Report");
//                reporter.config().setDocumentTitle("Test Results");
//                // reporter.config().setTimelineEnabled(true); // optional
//
//                extent = new ExtentReports();
//                extent.attachReporter(reporter);
//
//                // System info
//                extent.setSystemInfo("Tester", "Manasa");
//                extent.setSystemInfo("OS", System.getProperty("os.name"));
//                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
//                extent.setSystemInfo("Environment", "QA");
//
//            } catch (Exception e) {
//                System.err.println("[ExtentReportUtility] Failed to initialize report: " + e.getMessage());
//            }
//        }
//        return extent;
//    }
//
//    // --- Start & get test node ----------------------------------------------
//    public static void startTest(String testName) {
//        ExtentTest t = initReport().createTest(testName);
//        testTL.set(t);
//    }
//
//    public static void startTest(String testName, String description) {
//        ExtentTest t = initReport().createTest(testName, description);
//        testTL.set(t);
//    }
//
//    public static ExtentTest getTest() {
//        return testTL.get();
//    }
//
//    private static ExtentTest safeGet() {
//        ExtentTest t = getTest();
//        if (t == null) {
//            t = initReport().createTest("Untracked Test");
//            testTL.set(t);
//        }
//        return t;
//    }
//
//    // --- Logging helpers -----------------------------------------------------
//    public static void logInfo(String message)  { safeGet().log(Status.INFO, message); }
//    public static void logPass(String message)  { safeGet().log(Status.PASS, message); }
//    public static void logFail(String message)  { safeGet().log(Status.FAIL, message); }
//    public static void logSkip(String message)  { safeGet().log(Status.SKIP, message); }
//    public static void logWarn(String message)  { safeGet().log(Status.WARNING, message); }
//
//    // --- Screenshot: Save to file under <reports>/screenshots ---------------
//    /**
//     * Captures a screenshot and saves it under:
//     *   <project>/reports/screenshots/<testName>_yyyyMMdd_HHmmss_SSS.png
//     *
//     * Returns the ABSOLUTE filesystem path string (for logging/verification).
//     * The RELATIVE path (used for report linking) is always: "screenshots/<fileName>"
//     */
//    public static String captureScreenshot(WebDriver driver, String testName) {
//        try {
//            if (driver == null) {
//                logFail("Driver is null; cannot capture screenshot");
//                return null;
//            }
//
//            // Take the screenshot to a temp file
//            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//
//            // Ensure screenshots directory exists
//            Files.createDirectories(screenshotsDirAbs);
//
//            // Build timestamped, sanitized filename
//            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
//            String safeName  = sanitize(testName);
//            String fileName  = safeName + "_" + timestamp + ".png";
//
//            // Absolute destination: <reports>/screenshots/<fileName>
//            Path destAbs = screenshotsDirAbs.resolve(fileName);
//            FileUtils.copyFile(src, destAbs.toFile());
//
//            // Log absolute path for quick verification
//            logInfo("Screenshot saved at: " + destAbs.toAbsolutePath());
//
//            return destAbs.toAbsolutePath().toString();
//        } catch (Exception e) {
//            System.err.println("[ExtentReportUtility] captureScreenshot failed: " + e.getMessage());
//            logFail("captureScreenshot failed: " + e.getMessage());
//            return null;
//        }
//    }
//
//    // --- Screenshot: Attach (Relative → Absolute → Base64 fallback) ----------
//    /**
//     * Attaches a screenshot to the report.
//     * Primary: relative path "screenshots/<fileName>" (portable with the report)
//     * Secondary: absolute path (in case relative linking fails in some viewers)
//     * Fallback: Base64 (always renders inline)
//     */
//    public static void attachScreenshot(WebDriver driver, String testName) {
//        try {
//            String absolutePath = captureScreenshot(driver, testName);
//            if (absolutePath == null) {
//                logFail("Screenshot path is null; attachment skipped");
//                return;
//            }
//
//            Path shotAbs = Paths.get(absolutePath);
//
//            // 1) Relative path from the report folder (portable when 'reports' is zipped or moved)
//            String relativeStr = "screenshots/" + shotAbs.getFileName().toString();
//            relativeStr = relativeStr.replace("\\", "/"); // normalize for HTML on Windows
//
//            try {
//                safeGet().fail("Screenshot:",
//                        MediaEntityBuilder.createScreenCaptureFromPath(relativeStr).build());
//                // Optional thumbnail
//                safeGet().addScreenCaptureFromPath(relativeStr);
//                return;
//            } catch (Exception eRel) {
//                System.err.println("[ExtentReportUtility] Relative attach failed → trying absolute: " + eRel.getMessage());
//            }
//
//            // 2) Absolute path (works locally if filesystem accessible)
//            String absoluteStr = shotAbs.toAbsolutePath().toString();
//            try {
//                safeGet().fail("Screenshot (absolute):",
//                        MediaEntityBuilder.createScreenCaptureFromPath(absoluteStr).build());
//                safeGet().addScreenCaptureFromPath(absoluteStr);
//                return;
//            } catch (Exception eAbs) {
//                System.err.println("[ExtentReportUtility] Absolute attach failed → using Base64: " + eAbs.getMessage());
//            }
//
//            // 3) Base64 fallback (always renders inline)
//            attachScreenshotBase64(driver, "Screenshot (Base64)");
//
//        } catch (Exception e) {
//            logFail("Screenshot could not be attached: " + e.getMessage());
//        }
//    }
//
//    // --- Screenshot: Base64 (always renders inline) --------------------------
//    public static void attachScreenshotBase64(WebDriver driver, String label) {
//        try {
//            if (driver == null) {
//                logFail("Driver is null; cannot capture Base64 screenshot");
//                return;
//            }
//            String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
//            safeGet().fail(label, MediaEntityBuilder
//                    .createScreenCaptureFromBase64String(base64, label)
//                    .build());
//        } catch (Exception e) {
//            logFail("Base64 screenshot failed: " + e.getMessage());
//        }
//    }
//
//    // --- Flush ---------------------------------------------------------------
//    public static void flushReport() {
//        if (extent != null) {
//            extent.flush();
//        }
//    }
//
//    // --- Helpers -------------------------------------------------------------
//    private static String sanitize(String name) {
//        if (name == null || name.isBlank()) return "screenshot";
//        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
//    }
//}
package Utilities;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportUtility {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testTL = new ThreadLocal<>();

    // Holds the absolute path to the generated HTML report
    private static Path reportFilePath;
    // Holds the absolute path to the folder that contains the report
    private static Path reportsDirAbs;           // e.g., <project>/reports
    private static Path screenshotsDirAbs;       // e.g., <project>/reports/screenshots

    // --- Initialize report (once) -------------------------------------------
    public static synchronized ExtentReports initReport() {
        if (extent == null) {
            try {
                // Base folder: <project>/reports
                reportsDirAbs = Paths.get("reports").toAbsolutePath();
                screenshotsDirAbs = reportsDirAbs.resolve("screenshots");

                // Create base screenshot folder and subfolders
                Files.createDirectories(screenshotsDirAbs);
                Files.createDirectories(screenshotsDirAbs.resolve("passed")); // reports/screenshots/passed
                Files.createDirectories(screenshotsDirAbs.resolve("failed")); // reports/screenshots/failed

                // Timestamped report name
                String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                reportFilePath = reportsDirAbs.resolve("ExtentReport_" + ts + ".html");

                ExtentSparkReporter reporter = new ExtentSparkReporter(reportFilePath.toString());
                reporter.config().setReportName("Automation Execution Report");
                reporter.config().setDocumentTitle("Test Results");

                extent = new ExtentReports();
                extent.attachReporter(reporter);

                // System info
                extent.setSystemInfo("Tester", "Lovaraju");
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("Environment", "QA");

            } catch (Exception e) {
                System.err.println("[ExtentReportUtility] Failed to initialize report: " + e.getMessage());
            }
        }
        return extent;
    }

    // --- Start & get test node ----------------------------------------------
    public static void startTest(String testName) {
        ExtentTest t = initReport().createTest(testName);
        testTL.set(t);
    }

    public static void startTest(String testName, String description) {
        ExtentTest t = initReport().createTest(testName, description);
        testTL.set(t);
    }

    public static ExtentTest getTest() {
        return testTL.get();
    }

    private static ExtentTest safeGet() {
        ExtentTest t = getTest();
        if (t == null) {
            t = initReport().createTest("Untracked Test");
            testTL.set(t);
        }
        return t;
    }

    // --- Logging helpers -----------------------------------------------------
    public static void logInfo(String message)  { safeGet().log(Status.INFO, message); }
    public static void logPass(String message)  { safeGet().log(Status.PASS, message); }
    public static void logFail(String message)  { safeGet().log(Status.FAIL, message); }
    public static void logSkip(String message)  { safeGet().log(Status.SKIP, message); }
    public static void logWarn(String message)  { safeGet().log(Status.WARNING, message); }

    // --- Screenshot: Save to file (Pass/Fail subfolders) --------------------
    /**
     * Captures a screenshot and saves it under:
     * Success: <project>/reports/screenshots/passed/<fileName>
     * Failure: <project>/reports/screenshots/failed/<fileName>
     *
     * @param driver    WebDriver instance
     * @param testName  Name of the test (used for filename)
     * @param isSuccess True to store in 'passed', False to store in 'failed'
     * @return ABSOLUTE filesystem path string
     */
    public static String captureScreenshot(WebDriver driver, String testName, boolean isSuccess) {
        try {
            if (driver == null) {
                logFail("Driver is null; cannot capture screenshot");
                return null;
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Determine subfolder based on status
            String subFolderName = isSuccess ? "passed" : "failed";
            Path targetDir = screenshotsDirAbs.resolve(subFolderName);

            // Ensure directory exists (redundant safety check)
            Files.createDirectories(targetDir);

            // Build filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String safeName  = sanitize(testName);
            String fileName  = safeName + "_" + timestamp + ".png";

            // Absolute destination
            Path destAbs = targetDir.resolve(fileName);
            FileUtils.copyFile(src, destAbs.toFile());

            // Log absolute path (optional debugging)
            // System.out.println("Screenshot saved: " + destAbs.toAbsolutePath());

            return destAbs.toAbsolutePath().toString();
        } catch (Exception e) {
            System.err.println("[ExtentReportUtility] captureScreenshot failed: " + e.getMessage());
            logFail("captureScreenshot failed: " + e.getMessage());
            return null;
        }
    }

    // --- Screenshot: Attach (Relative → Absolute → Base64 fallback) ----------
    /**
     * Attaches a screenshot to the report.
     * * @param driver    WebDriver instance
     * @param testName  Name of the test
     * @param isSuccess True = Log as PASS (green), False = Log as FAIL (red)
     */
    public static void attachScreenshot(WebDriver driver, String testName, boolean isSuccess) {
        try {
            String absolutePath = captureScreenshot(driver, testName, isSuccess);
            if (absolutePath == null) {
                logFail("Screenshot path is null; attachment skipped");
                return;
            }

            Path shotAbs = Paths.get(absolutePath);
            String subFolderName = isSuccess ? "passed" : "failed";

            // 1) Relative path: "screenshots/passed/file.png" or "screenshots/failed/file.png"
            // Note: We use shotAbs.getFileName() to get just the name.
            String relativeStr = "screenshots/" + subFolderName + "/" + shotAbs.getFileName().toString();
            relativeStr = relativeStr.replace("\\", "/");

            // Create the Media object
            var media = MediaEntityBuilder.createScreenCaptureFromPath(relativeStr).build();

            // 2) Attach to report based on Status
            if (isSuccess) {
                safeGet().pass("Test Passed (Screenshot captured)", media);
            } else {
                safeGet().fail("Test Failed (Screenshot captured)", media);
            }

        } catch (Exception e) {
            logFail("Screenshot could not be attached: " + e.getMessage());
        }
    }

    // --- Flush ---------------------------------------------------------------
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    // --- Helpers -------------------------------------------------------------
    private static String sanitize(String name) {
        if (name == null || name.isBlank()) return "screenshot";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}