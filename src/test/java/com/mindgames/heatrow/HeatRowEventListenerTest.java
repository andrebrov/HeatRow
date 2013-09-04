package com.mindgames.heatrow;

import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: andrebrov
 * Date: 06.03.13
 * Time: 9:45
 */
public class HeatRowEventListenerTest {

    private HeatRowEventListener listener;
    private WebElement element;
    private WebDriver driver;
    private File file;

    @BeforeMethod
    public void setup() throws IOException {
        listener = new HeatRowEventListener();
        element = mock(WebElement.class);
        Point point = new Point(10, 20);
        Dimension dimension = new Dimension(50, 40);
        when(element.getLocation()).thenReturn(point);
        when(element.getSize()).thenReturn(dimension);
        driver = mock(WebDriver.class, withSettings().extraInterfaces(TakesScreenshot.class));
        file = new File("./screenshot.png");
        int width = 200, height = 200;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D ig2 = bi.createGraphics();
        Font font = new Font("TimesRoman", Font.BOLD, 20);
        ig2.setFont(font);
        String message = "www.java2s.com!";
        FontMetrics fontMetrics = ig2.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(message);
        int stringHeight = fontMetrics.getAscent();
        ig2.setPaint(Color.black);
        ig2.drawString(message, (width - stringWidth) / 2, height / 2 + stringHeight / 4);
        ImageIO.write(bi, "PNG", file);
        when(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)).thenReturn(file);
    }

    @Test
    public void shouldRecordElementPositionBeforeClickOn() {
        listener.beforeClickOn(element, driver);
        Set<Location> locations = listener.getLocations();
        assertNotNull("Locations are null", locations);
        assertEquals("Wrong amount of locations elements", 1, locations.size());
        Location location = (Location) locations.toArray()[0];
        assertEquals("Wrong top left point", new Point(10, 20), location.getTopLeftPoint());
        assertEquals("Wrong bottom right point", new Dimension(50, 40), location.getDimension());
    }

    @Test
    public void shouldSaveElementOnlyOnceBeforeClickOn() {
        listener.beforeClickOn(element, driver);
        listener.beforeClickOn(element, driver);
        Set<Location> locations = listener.getLocations();
        assertEquals("Wrong amount of locations elements", 1, locations.size());
    }

    @Test
    public void shouldRecordElementBeforeChangeValueOf() {
        listener.beforeChangeValueOf(element, driver);
        Set<Location> locations = listener.getLocations();
        assertNotNull("Locations are null", locations);
        assertEquals("Wrong amount of locations elements", 1, locations.size());
        Location location = (Location) locations.toArray()[0];
        assertEquals("Wrong top left point", new Point(10, 20), location.getTopLeftPoint());
        assertEquals("Wrong bottom right point", new Dimension(50, 40), location.getDimension());
    }

    @Test
    public void shouldSaveElementOnlyOnceBeforeChangeValueOf() {
        listener.beforeClickOn(element, driver);
        listener.beforeClickOn(element, driver);
        Set<Location> locations = listener.getLocations();
        assertEquals("Wrong amount of locations elements", 1, locations.size());
    }

    @Test
    public void shouldTakePageScreenShotBeforeClick() {
        listener.beforeClickOn(element, driver);
        Set<Page> pages = listener.getPages();
        assertEquals("Two many pages", 1, pages.size());
        Page page = (Page) pages.toArray()[0];
        assertNotNull("Screenshot not presented", page.getScreenshot());
    }

    @Test
    public void shouldTakePageScreenShotBeforeChangeValue() {
        listener.beforeChangeValueOf(element, driver);
        Set<Page> pages = listener.getPages();
        assertEquals("Two many pages", 1, pages.size());
        Page page = (Page) pages.toArray()[0];
        assertNotNull("Screenshot not presented", page.getScreenshot());
    }

    @Test
    public void shouldPrintScreenshotsWithElementsBeforeTest() {
        listener.beforeClickOn(element, driver);
        List<Report> reports = listener.getReports();
        assertEquals("Wrong reports amount", 1, reports.size());
        assertNotNull("Image not presented", reports.get(0).getImage());
    }

    @AfterMethod
    public void tearDown() {
        file.delete();
    }

}
