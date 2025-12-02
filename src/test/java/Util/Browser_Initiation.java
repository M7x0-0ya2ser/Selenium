package Util;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class Browser_Initiation {

    public static ExtentReports extentReports;
    public static ExtentTest logger;

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
            } else if (browserName.toLowerCase().contains("fire")) {
                System.setProperty("webdriver.gecko.driver", projectPath + "/Drivers/geckodriver.exe");
                FirefoxOptions options = new FirefoxOptions();
                driver = new FirefoxDriver(options);
            } else if (browserName.toLowerCase().contains("internet")) {
                System.setProperty("webdriver.ie.driver", projectPath + "/Drivers/IEDriverServer.exe");
                InternetExplorerOptions options = new InternetExplorerOptions();
                driver = new InternetExplorerDriver(options);
            } else if (browserName.toLowerCase().contains("edge")) {
                System.setProperty("webdriver.edge.driver", projectPath + "/Drivers/msedgedriver.exe");
                EdgeOptions options = new EdgeOptions();
                driver = new EdgeDriver(options);
            } else {
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

    @BeforeMethod
    public void before_test(Method method) {
        if (extentReports != null) {
            logger = extentReports.startTest(method.getName());
        } else {
            System.err.println("ExtentReports is null, test logging may not work.");
        }
    }

    @SneakyThrows
    @BeforeClass
    public void beforeClass() {
        Generate_Report_File();
    }

    public void Generate_Report_File() {
        String className = this.getClass().getSimpleName();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        extentReports = new ExtentReports("Reports/" + className + "_" + timeStamp + ".html");
        extentReports.addSystemInfo("Project", "Switching_APIs");
        extentReports.addSystemInfo("Author", "Muhammed Yasser");
    }

    @AfterClass
    public void afterClass() {
        if (extentReports != null) {
            extentReports.flush();
        }

    }
}
