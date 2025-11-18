package Util;

import java.util.Collections;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeDriver;

public class Browser_Initiation {

    public static WebDriver startBrowser(String url, String browserName) {
        WebDriver driver = null;
        String projectPath = System.getProperty("user.dir"); // Get dynamic project path

        try {
            if (browserName.toLowerCase().contains("chr")) {
                System.setProperty("webdriver.chrome.driver", projectPath + "/Drivers/chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("start-maximized");
                options.setExperimentalOption("useAutomationExtension", false);
                options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
                driver = new ChromeDriver(options);
            }
            else if (browserName.toLowerCase().contains("fire")) {
                System.setProperty("webdriver.gecko.driver", projectPath + "/Drivers/geckodriver.exe");
                FirefoxOptions options = new FirefoxOptions();
                driver = new FirefoxDriver(options);
            }
            else if (browserName.toLowerCase().contains("internet")) {
                System.setProperty("webdriver.ie.driver", projectPath + "/Drivers/IEDriverServer.exe");
                InternetExplorerOptions options = new InternetExplorerOptions();
                driver = new InternetExplorerDriver(options);
            }
            else if (browserName.toLowerCase().contains("edge")) {
                System.setProperty("webdriver.edge.driver", projectPath + "/Drivers/msedgedriver.exe");
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            }
            else {
                System.out.println("❌ Unsupported browser: " + browserName);
                return null;
            }

            if (driver != null) {
                driver.manage().deleteAllCookies();
                driver.manage().window().maximize();
                driver.navigate().to(url);
            }

        } catch (Exception e) {
            System.out.println("❌ Error initializing browser: " + e.getMessage());
        }
        return driver;
    }
}
