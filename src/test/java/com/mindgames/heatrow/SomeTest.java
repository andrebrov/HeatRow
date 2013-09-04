package com.mindgames.heatrow;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: andrebrov
 * Date: 25.07.13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class SomeTest {

    @Test
    public void test() {
        WebDriver FF_driver = new FirefoxDriver();

        EventFiringWebDriver driver = new EventFiringWebDriver(FF_driver);

        HeatRowEventListener myListener = new HeatRowEventListener();
        driver.register(myListener);
        driver.get("http://en.wikipedia.org/wiki/Main_Page");
        //find the About link
        WebElement about = driver.findElement
                (By.xpath("//*[@id='n-aboutsite']/a"));

        // click to the link
        about.click();
        myListener.getReports();

        driver.quit();
    }
}
