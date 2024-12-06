package utilities;



	import java.io.File;
	import java.io.IOException;
	import java.text.SimpleDateFormat;
	import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
	import org.openqa.selenium.TakesScreenshot;
	import org.openqa.selenium.WebDriver;

	public class ScreenshotUtility {

	    // Method to capture screenshot
	    public static String captureScreenshot(WebDriver driver, String screenshotName) {
	        String dateName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	        String destination = System.getProperty("user.dir") + "/screenshots/" + screenshotName + "_" + dateName + ".png";
	        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	        try {
	            FileUtils.copyFile(src, new File(destination));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return destination;
	    }
	}

