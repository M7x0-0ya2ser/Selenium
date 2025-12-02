package TestCases;

import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/";

    private WebDriver driver;
    private LoginPage loginPage;
    private GetScreenShot screenshot;

    private static ExtentTest test;
    private static ExtentReports extent;

    @BeforeClass
    public void setUp() {
        extent = new ExtentReports("LoginTestsReport.html", true);
        driver = startBrowser(URL, "fire");

        loginPage = new LoginPage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        if (test != null)
            extent.endTest(test);

        extent.flush();
        driver.quit();
    }

    // ===== TEST CASES =====

    @Test
    public void validLoginTest() throws Exception {
        test = extent.startTest("Valid Login Test");

        loginPage.login("Muhammad.Yasser1764664944405@gmail.com", "StR0n9P@$$w0rd");

        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/main");
    }

    @Test
    public void invalidLoginTest() throws Exception {
        test = extent.startTest("Invalid Login - Missing Fields");

        loginPage.login("", "");

        String errors = loginPage.getAllErrors();
        Assert.assertTrue(errors.contains("Email is required"));
        Assert.assertTrue(errors.contains("Password is required"));

    }
}
