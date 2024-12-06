package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener {
	
	
	 public ExtentSparkReporter sparkReporter; // UI of the report
	    public ExtentReports extent;              // Populate common info on the report
	    public ExtentTest logger;   // Creating test case entries in the reports and update
	    

	    public void onStart(ITestContext context) {
	    	
	    	String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	        String reportName = "Test-Report-" + timeStamp + ".html";
	        
	        // Set the path for the report
	        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/" +reportName);
	        sparkReporter.config().setDocumentTitle("Automation Report");
	        sparkReporter.config().setReportName("Functional Testing");
	        sparkReporter.config().setTheme(Theme.DARK);

	        extent = new ExtentReports();
	        extent.attachReporter(sparkReporter);

	        extent.setSystemInfo("Computer Name", "LocalHost");
	        extent.setSystemInfo("Environment", "QA");
	        extent.setSystemInfo("Tester Name", "Harish Patel");
	        extent.setSystemInfo("OS", "Windows 11");
	        extent.setSystemInfo("Browser Name", "Edge");
	    }

	    @Override
	    public void onTestSuccess(ITestResult result) {
	    	logger = extent.createTest(result.getName());  // Create a new test entry
	    	logger.log(Status.PASS, "Test case PASSED: " + result.getName());
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {
	    	logger = extent.createTest(result.getName());  // Create a new test entry
	    	logger.log(Status.FAIL, "Test case FAILED: " + result.getName());
	    	logger.log(Status.FAIL, "Test case FAILED due to: " + result.getThrowable());
	        
	    	 // Get the WebDriver from the ITestContext
	        WebDriver driver = (WebDriver) result.getTestContext().getAttribute("driver");

	        if (driver != null) {
	            // Capture screenshot and attach to the report
	            String screenshotPath = ScreenshotUtility.captureScreenshot(driver, result.getName());
	            try {
	                logger.addScreenCaptureFromPath(screenshotPath); // Add screenshot to Extent Report
	            } catch (Exception e) {
	                logger.log(Status.FAIL, "Failed to attach screenshot: " + e.getMessage());
	            }
	        } else {
	            logger.log(Status.FAIL, "Driver was null, could not capture screenshot.");
	        }
	    }

	    @Override
	    public void onTestSkipped(ITestResult result) {
	    	logger = extent.createTest(result.getName());  // Create a new test entry
	    	logger.log(Status.SKIP, "Test case SKIPPED: " + result.getName());
	    }

	    @Override
	    public void onFinish(ITestContext context) {
	        extent.flush();  // This will flush the report and write it to the file
	    }
	}
	
