package Util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetScreenShot {

    public String capture(WebDriver driver, String screenShotName) {
        try {
            // Ensure screenshots directory exists
            String screenshotDir = System.getProperty("user.dir") + File.separator + "Screenshots";
            Files.createDirectories(Paths.get(screenshotDir));

            // Capture the screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String dest = screenshotDir + File.separator + screenShotName + ".png";
            File destination = new File(dest);
            FileUtils.copyFile(source, destination);

            return dest; // Return file path for reference
        } catch (IOException e) {
            System.err.println("❌ Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }
}
