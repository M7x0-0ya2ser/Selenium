package Util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetScreenShot {

    public String capture(WebDriver driver, String baseName) {
        try {

            String screenshotDir = System.getProperty("user.dir") + File.separator + "Screenshots";
            Files.createDirectories(Paths.get(screenshotDir));

            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String cleanedName = baseName.replaceAll("\\s+", "");
            String fileName = cleanedName + "_" + date + ".png";

            File screenshot;

            if (driver instanceof FirefoxDriver) {
                screenshot = ((FirefoxDriver) driver).getFullPageScreenshotAs(OutputType.FILE);
            }
            else {
                screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            }

            File outputFile = new File(screenshotDir + File.separator + fileName);
            FileUtils.copyFile(screenshot, outputFile);

            System.out.println("Screenshot saved: " + outputFile.getAbsolutePath());
            return outputFile.getAbsolutePath();

        } catch (Exception e) {
            System.out.println("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
}
