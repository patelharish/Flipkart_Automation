package Ecommerce_Automation.Shopping_app;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlipkartTest {

    WebDriver driver;
    WebDriverWait wait;
    Logger log = LoggerFactory.getLogger(FlipkartTest.class);

    // Setup method to initialize WebDriver and navigate to the application
    @BeforeTest
    @Parameters("browser") // Adding TestNG parameter for cross-browser execution
    public void setUp(String browser) {
    	try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    log.info("Launching Chrome browser...");
                    driver = new ChromeDriver();
                    break;

                case "edge":
                    log.info("Launching Edge browser...");
                    driver = new EdgeDriver();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            log.info("Navigating to Flipkart...");
            driver.get("https://www.flipkart.com/");
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Explicit wait of 10 seconds

        } catch (Exception e) {
            log.error("Error in setup: " + e.getMessage());
        }
    }

    @Test(priority = 2)
    public void searchForProduct() {
        // Search for Bluetooth speakers
        log.info("Searching for 'bluetooth speakers'...");
        WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search for Products, Brands and More']")));
        search.sendKeys("bluetooth speakers");
        search.sendKeys(Keys.ENTER);
    }

    @Test(priority = 3)
    public void applyPriceFilter() throws InterruptedException {
        // Select minimum price filter
    	Thread.sleep(4000);
        log.info("Selecting minimum price of 3000...");
        WebElement minPriceBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//select[@class='Gn+jFg'])[1]")));
        Select minOptions = new Select(minPriceBtn);
        minOptions.selectByValue("3000");

        // Select maximum price filter
        Thread.sleep(4000);
        log.info("Selecting maximum price of 10000...");
        WebElement maxPrice = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//select[@class='Gn+jFg'])[2]")));
        Select maxOptions = new Select(maxPrice);
        maxOptions.selectByValue("10000");
    }

    @Test(priority = 4)
    public void applyStarRatingFilter() throws InterruptedException {
        // Scroll down the page to load more results
    	Thread.sleep(3000);
        log.info("Scrolling down the page...");
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");

        // Select 4+ star rating filter
        log.info("Applying 4+ star rating filter...");
        WebElement fourStarRating = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='XqNaEv'])[1]")));
        fourStarRating.click();
    }

    @Test(priority = 5)
    public void sortItemsLowToHigh() throws InterruptedException {
        // Sort items by 'Price -- Low to High'
        log.info("Sorting items by 'Price -- Low to High'...");
        Thread.sleep(3000);
        WebElement sortByPrice = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[normalize-space()='Price -- Low to High'])[1]")));
        sortByPrice.click();
    }

    @Test(priority = 6)
    public void selectFifthItem() throws InterruptedException {
        // Scroll further down to load more items
    	Thread.sleep(3000);
        log.info("Scrolling down further...");
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 600);");

        // Click on the 5th item in the results
        log.info("Clicking on the 5th item...");
        WebElement fifthItem = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='_4WELSP'])[5]")));
        fifthItem.click();
    }

    @Test(priority = 7)
    public void switchToNewTabAndAddToCart() {
        // Store the current window handle (parent tab)
        String parentTab = driver.getWindowHandle();

        // Switch to the newly opened tab
        Set<String> allTabs = driver.getWindowHandles();
        for (String tab : allTabs) {
            if (!tab.equals(parentTab)) {
                driver.switchTo().window(tab);
                log.info("Switched to the new tab...");
            }
        }

        // Add the item to the cart
        log.info("Adding the item to the cart...");
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add to cart']")));
        addToCartButton.click();
    }

    @Test(priority = 8)
    public void verifyItemInCart() {
        // Verify that the item is in the cart
        log.info("Verifying the item in the cart...");
        String actualItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Price (1 item)')]"))).getText();
        String expectedItem = "Price (1 item)";
        Assert.assertEquals(actualItem, expectedItem, "Item count mismatch!");
        log.info("Item count verified successfully!");
    }

    @Test(enabled=false)
    public void verifyItemPrice() {
        // Verify the price of the item in the cart
        log.info("Verifying the price in the cart...");
        String actualPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(text(),'₹3,096')])[1]"))).getText();
        String expectedPrice = "₹3,096";
        Assert.assertEquals(actualPrice, expectedPrice, "Price mismatch!");
        log.info("Price verified successfully!");
    }

    @Test(priority = 10)
    public void removeItemFromCart() {
        // Remove the item from the cart
        log.info("Removing the item from the cart...");
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[normalize-space()='Remove'])[1]")));
        removeButton.click();
        WebElement confirmRemove = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//div[@class='sBxzFz fF30ZI A0MXnh'])[1]")));
        confirmRemove.click();
    }

    @Test(priority = 11)
    public void switchBackToParentTab() {
        // Switch back to the parent tab
        String parentTab = driver.getWindowHandle();
        driver.switchTo().window(parentTab);
        log.info("Switched back to the parent tab...");
    }

    // Cleanup method to close the browser after the test
    @AfterTest
    public void tearDown() {
        if (driver != null) {
            log.info("Closing the browser...");
            driver.quit();
        }
    }
}

