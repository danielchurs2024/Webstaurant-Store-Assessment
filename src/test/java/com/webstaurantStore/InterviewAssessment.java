package com.webstaurantStore;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class InterviewAssessment {

    public static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {

        // Instantiating Chrome Driver using WebDriverManager BUT no need to use this with the latest version of selenium
        // WebDriverManager.chromedriver().setup();

        // This will instantiate Chrome Driver - No need to use WebDriverManger
        driver = new ChromeDriver();

        // Setting browser settings for good practice
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigating to site
        String url = "https://www.webstaurantstore.com/";
        driver.get(url);

        // Searching for Stainless Work table
        WebElement searchTextBox = driver.findElement(By.xpath("(//*[@id='searchval'])[1]"));
        searchTextBox.sendKeys("Stainless work table");
        WebElement searchButton = driver.findElement(By.xpath("(//button[@value='Search'])[1]"));
        searchButton.click();

        // Code to navigate through pagination functionality
        for (int i = 0; i < 10; i++) {

            // After navigating to the next page, we again call this static method which verifies that all results contain the word 'Table'
            verifyTextOnSearchResults("Table");

            WebElement nextPageButton = driver.findElement(By.xpath("//li[@class='inline-block leading-4 align-top rounded-r-md']"));

            // Using JS to scroll into the view of the pagination functionality
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", nextPageButton);

            if (nextPageButton.isDisplayed() && nextPageButton.isEnabled()) {
                nextPageButton.click();
            } else {
                System.out.println("No more pages available.");
                break;
            }
        }

        // Adding last option of the last page to the cart
        List<WebElement> addToCartLastButton = driver.findElements(By.xpath("//input[@name='addToCartButton']"));
        WebElement lastOption = addToCartLastButton.get(addToCartLastButton.size() -1);
        lastOption.click();

        // Navigating to cart
        WebElement viewCartButton = driver.findElement(By.xpath("//*[contains(text(),'View Cart')]"));
        viewCartButton.click();

        // Emptying cart
        WebElement emptyCartButton = driver.findElement(By.xpath("(//*[contains(text(),'Empty')])[1]"));
        emptyCartButton.click();

        WebElement modalDialogEmptyCartButton = driver.findElement(By.xpath("(//*[contains(text(),'Empty')])[3]"));
        modalDialogEmptyCartButton.click();
        driver.quit();
    }

    /**
     * THIS METHOD VERIFIES THAT ALL RESULTS ON A PAGE CONTAIN THE WORD 'TABLE' - YOU CAN MAKE THIS DYNAMIC BY PARAMETERIZING TEXT SO THAT YOU MAY VERIFY OTHER VALUES ON PAGE IF NEEDED
     */
    public static void verifyTextOnSearchResults(String text) {
        List<WebElement> searchResults = driver.findElements(By.xpath("/html/body/div[2]/div/div[4]/div[1]/div[3]/div"));
        for (WebElement searchResult : searchResults) {
            String textOfResult = searchResult.getText();
            assertTrue(textOfResult.contains(text));
        }
    }

    /***
     * THIS METHOD VERIFIES IF A BOOLEAN CONDITION IS TRUE, AssertionError IS BEING HANDLED SO THAT PROGRAM MAY CONTINUE TO EXECUTE AND WE CAN RETRIEVE STACK TRACE MESSAGES AT END OF EXECUTION
     * @param isTrue
     */
    public static void assertTrue(boolean isTrue) {
        try {
            Assert.assertTrue(isTrue);
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }
}
