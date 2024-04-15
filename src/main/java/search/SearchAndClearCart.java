package search;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author Ryan Kendricks
 * Date: 04/15/2024
 */
public class SearchAndClearCart {

    public static void main(String[] args) throws InterruptedException {

        // Set Chrome Driver
        System.setProperty("webdriver.chrome.driver", "C:\\test\\chromedriver.exe");
        // Create new ChromeDriver instance
        WebDriver driver = new ChromeDriver();
        // Set the WebstaurantStore.com Page Url
        String baseUrl = "https://www.webstaurantstore.com/";

        // Maximize Browser and Navigate to Base Url
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // Find the Search Input Box to search for "stainless work table"
        WebElement searchInput = driver.findElement(By.id("searchval"));
        if (searchInput.isDisplayed()) {
            searchInput.sendKeys("stainless work table");
            System.out.println("Set Search Term to Search Input Box.");
        } else {
            System.out.println("Cannot Set Search Term to Search Input Box.");
        }

        // Click Search Submit button
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        if (submitButton.isDisplayed()) {
            submitButton.click();
            System.out.println("Clicked Search Submit Button.");
        } else {
            System.out.println("Search Submit Button is not displayed. Cannot click.");
        }

        // Check the search result ensuring every product has the word 'Table' in its title.
        List<WebElement> productTitles = driver.findElements(By.xpath("//span[@data-testid='itemDescription']"));
        List<String> titles = new ArrayList<>();

        for (WebElement item : productTitles) {
            String title = item.getText();
            titles.add(title);
        }

        boolean productTitlesContainTable = checkTitles(titles);
        if (productTitlesContainTable) {
            System.out.println("ALL titles contain the word 'Table'.");
        } else {
            System.out.println("Not all titles contain the word 'Table'.");
        }

        // Add the last of found items to Cart.
        // Click on last pagination number
        WebElement paginationLast = driver.findElement(By.xpath("//a[contains(@aria-label,'last page')]"));
        if (paginationLast.isDisplayed()) {
            paginationLast.click();
            System.out.println("Clicked Last Pagination Button.");
        } else {
            System.out.println("Last Pagination Button is not displayed. Cannot click.");
        }

        // Get Add to Cart Buttons for last page of products
        List<WebElement> productsAddToCart = driver.findElements(By.name("addToCartButton"));

        // Get Last Product Add to Cart Button
        WebElement lastAddToCart = productsAddToCart.get(productsAddToCart.size() - 1);

        // Scroll to Last Product Add to Cart Button
        JavascriptExecutor jScript = (JavascriptExecutor) driver;
        jScript.executeScript("arguments[0].scrollIntoView(true);", lastAddToCart);

        // Click on Last Product Add To Cart Button
        if (lastAddToCart.isDisplayed()) {
            lastAddToCart.click();
            System.out.println("Clicked Last Add to Cart Button.");
        } else {
            System.out.println("Last Add To Cart Button is not displayed. Cannot click.");
        }

        // Need to go to the Home page before cart to see the product in the cart. Don't know why, but this works
        driver.get("https://www.webstaurantstore.com/");
        // Go to Cart
        driver.get("https://www.webstaurantstore.com/cart/");

        // Empty Cart.
        WebDriverWait wait = new WebDriverWait(driver, 5);
        WebElement clearItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='deleteCartItemButton itemDelete__link itemDelete--positioning']")));
        clearItem.click();
        System.out.println("Clicked Product Remove X Button");

        WebElement noItem = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[@class='header-1']")));
        if (noItem.isDisplayed()) {
            System.out.println("Cart is empty.");
        } else {
            System.out.println("Cart is NOY empty.");
        }

        // Quit Driver
        driver.quit();
    }

    private static boolean checkTitles(List<String> titles) {
        for (String title : titles) {
            if (!title.contains("Table")) {
                return false;
            }
        }
        return true;
    }
}