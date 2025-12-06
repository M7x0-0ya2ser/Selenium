package Util;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageBase {

    protected WebDriver driver;
    protected Actions builder;
    protected JavascriptExecutor js;

    public PageBase(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.builder = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    protected void handleVerticalScroll(int pixels) {
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    public void waitElementToDisplay(WebElement element, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitElementToBeClickable(WebElement element, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void switchToLastTab() {
        Set<String> windowHandles = driver.getWindowHandles();
        List<String> handlesList = new ArrayList<>(windowHandles);
        driver.switchTo().window(handlesList.get(handlesList.size() - 1));
    }

    public void takeScreenshot(String baseName) {
        try {

            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            
            String fileName = baseName.replaceAll("\\s+", "") + "_" + date + ".png"; // remove spaces

            File screenshot = ((FirefoxDriver) driver).getFullPageScreenshotAs(OutputType.FILE);

            File outputFile = new File("./Screenshots/" + fileName);
            FileUtils.copyFile(screenshot, outputFile);

            System.out.println("Full page screenshot saved: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error taking full page screenshot: " + e.getMessage());
        }
    }

    public Alert switchToAlert() {
        return driver.switchTo().alert();
    }
}
