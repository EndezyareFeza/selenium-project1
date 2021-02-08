package org.example;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */

public class AppTest {

    private static final Logger log = Logger.getLogger(AppTest.class);

    @Test
    public void login() {

        //A test project for www.gittigidiyor.com site

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Mahir\\Desktop\\selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor jse;

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        //Optional to add
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

        // Entry to the site
        driver.get("https://www.gittigidiyor.com/");

        //User sign in to the site
        driver.get("https://www.gittigidiyor.com/uye-girisi");

        try {
            driver.findElement(By.id("L-UserNameField")).sendKeys("your email address");
            driver.findElement(By.id("L-PasswordField")).sendKeys("your password");

            Thread.sleep(2000);
            driver.findElement(By.id("gg-login-enter")).click();

            //Search for an item then click search button
            driver.findElement(By.name("k")).sendKeys("bilgisayar");
            driver.findElement(By.tagName("button")).click();

            //Pick up a random item in the list
            driver.findElement(By.id("item-info-block-649306364")).click();

            //Get the price of the item
            String normalPriceString = driver.findElement(By.id("sp-price-lowPrice")).getText();

            jse = (JavascriptExecutor) driver;
            jse.executeScript("window.scrollBy(0,800)");


            //Add the item to the basket
            driver.findElement(By.id("add-to-basket")).click();
            Thread.sleep(2000);

            //Entry to the basket page
            driver.get("https://www.gittigidiyor.com/sepetim");

            //Get the item's price
            String totalPriceString = driver.findElement(By.cssSelector(".new-price-box .new-price")).getText();

            //Formatting the prices to check the values
            Float normalPrice = getPrice(normalPriceString);
            Float totalPrice = getPrice(totalPriceString);

            //Check the prices
            if (normalPrice != totalPrice) {
                log.info("There is a discount in the basket.");
            } else {
                log.info("");
            }
            //   assertTrue(normalPrice == totalPrice);

            //Increase the number of item in the basket
            driver.findElement(By.cssSelector(".plus.icon-plus.gg-icon.gg-icon-plus")).click();

            String newTotalPriceString = driver.findElement(By.className("new-price")).getText();
            Float newTotalPrice = getPrice(newTotalPriceString);

            //Check the prices
            if (normalPrice * 2 != newTotalPrice) {
                log.error("There is a discount in the basket.");
            } else {
                log.info("");
            }

            //assertTrue(normalPrice * 2 == newTotalPrice);


            Thread.sleep(2000);

            //Delete the items in the basket
            driver.findElement(By.cssSelector("a.btn-delete.btn-update-item.hidden-m")).click();


            //Check to see that no items are available
            WebElement boxText = driver.findElement(By.xpath("//*[text()='Sepetinizde ürün bulunmamaktadır.']"));
            log.info(boxText);

            Thread.sleep(2000);
            driver.quit();


        } catch (InterruptedException e) {
            log.error(e);
        }

    }

    //Method to format the price values
    private float getPrice(String price) {
        price = price.replaceAll("TL", "");
        price = price.replaceAll("\\.", "");
        price = price.replaceAll(",", "\\.");
        return Float.parseFloat(price);
    }
}
