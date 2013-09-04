package com.mindgames.heatrow;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.events.WebDriverEventListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrebrov
 * Date: 06.03.13
 * Time: 9:44
 */
public class HeatRowEventListener implements WebDriverEventListener {

    private Map<Page, Set<Location>> data = new HashMap<Page, Set<Location>>();

    public void afterClickOn(WebElement element, WebDriver driver) {

    }

    public void beforeClickOn(WebElement element, WebDriver driver) {
        Page page = takeScreenshot(driver);
        extractElementLocationAndSave(page, element);
    }

    public void afterChangeValueOf(WebElement element, WebDriver driver) {

    }

    public void beforeChangeValueOf(WebElement element, WebDriver driver) {
        Page page = takeScreenshot(driver);
        extractElementLocationAndSave(page, element);
    }

    private Page takeScreenshot(WebDriver driver) {
        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "./target/screenshots/" + source.getName();
            File destFile = new File(path);
            destFile.createNewFile();
            FileUtils.copyFile(source, destFile);
            Page page = new Page();
            page.setScreenshot(destFile);
            return page;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void extractElementLocationAndSave(Page page, WebElement element) {
        Location location = new Location();
        Point topLeftPoint = element.getLocation();
        location.setTopLeftPoint(topLeftPoint);
        Dimension size = element.getSize();
        location.setDimension(size);
        Set<Location> locationSet = data.get(page);
        if (locationSet == null) {
            locationSet = new HashSet<Location>();
        }
        locationSet.add(location);
        data.put(page, locationSet);
    }

    public Set<Location> getLocations() {
        List<Set<Location>> values = new ArrayList<Set<Location>>(data.values());
        Set<Location> locations = new HashSet<Location>();
        for (Set<Location> value : values) {
            locations.addAll(value);
        }
        return locations;
    }

    public Set<Page> getPages() {
        return data.keySet();
    }

    public List<Report> getReports() {
        List<Report> reports = new ArrayList<Report>();
        for (Page page : data.keySet()) {
            Set<Location> locations = data.get(page);
            try {
                Image pageScreenShot = ImageIO.read(page.getScreenshot());
                ImageIcon img = new ImageIcon(pageScreenShot);
                BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D ig2 = bi.createGraphics();
                ig2.drawImage(ImageIO.read(page.getScreenshot()), null, 0, 0);
                ig2.setPaint(Color.yellow);
                for (Location location : locations) {
                    Point topLeftPoint = location.getTopLeftPoint();
                    Dimension dimension = location.getDimension();
                    ig2.drawRect(topLeftPoint.getX(), topLeftPoint.getY(),
                            dimension.getWidth(), dimension.getHeight());
                    ig2.fillRect(topLeftPoint.getX(), topLeftPoint.getY(),
                            dimension.getWidth(), dimension.getHeight());
                }
                File reportImage = new File("Report_" + page.getScreenshot().getName());
                ImageIO.write(bi, "PNG", reportImage);
                reports.add(new Report(reportImage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return reports;
    }

    public void beforeNavigateTo(String url, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterNavigateTo(String url, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeNavigateBack(WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterNavigateBack(WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeNavigateForward(WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterNavigateForward(WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeScript(String script, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void afterScript(String script, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onException(Throwable throwable, WebDriver driver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
