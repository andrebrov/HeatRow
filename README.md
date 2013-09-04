[![Build Status](https://travis-ci.org/andrebrov/HeatRow.png?branch=master)](https://travis-ci.org/andrebrov/HeatRow)
###HeatRow
Goal of this selenium plugin is to create visual reports that will show you what elements of UI are covered (clicked, changed) by tests. 
Initial version of this library just show what was covered, next version will create heat map that should show how often each element was clicked.

###How to add this library yo project

First of all add next maven artifact:

```
<dependency>
    <groupId>com.mindgames</groupId>
    <artifactId>heatrow</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

Then you should add HeatRowEventListener to your driver, for example:

```java
WebDriver driver = new FirefoxDriver();
EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
HeatRowEventListener listener = new HeatRowEventListener();
eventDriver.register(listener);
```

When all tests passed you should call getReports() method that will generate reports in /screenshots folder