package TestCases;

import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import Util.PageBase;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTests extends Browser_Initiation {
    private final String URL = "https://dashboard-yazara-uat.banknbox.com";
    private WebDriver driver;
    private PageBase pageBase;
    private LoginPage loginPage;

    private final String validUser = "ahmed.mossad";
    private final String validPassword = "P@ssw0rd1";
    private final String invalidUser = "jdlkasdad";
    private final String invalidPassword = "jhhjfdgahjf";

    private static ExtentTest test;
    private static ExtentReports extent;
    private GetScreenShot screenshot;

    @BeforeClass
    public void setUp() {
        extent = new ExtentReports("ExtentReport.html", true);
        driver = startBrowser(URL, "edge");
        pageBase = new PageBase(driver);
        screenshot = new GetScreenShot();
        loginPage = new LoginPage(driver);
    }

    @AfterSuite
    public void tearDownSuite() {
        if (test != null) {
            extent.endTest(test);
        }
        extent.flush();
        driver.quit();
    }

    @Test
    public void invalidLoginTest() throws Exception {
        test = extent.startTest("Invalid User Login");

        loginPage.login(invalidUser, invalidPassword);

        String errorMsg = loginPage.getInvalidLoginAlert();
        Assert.assertEquals(errorMsg, "Invalid Credentials");
    }

    @Test
    public void validLoginTest() throws Exception {
        test = extent.startTest("Invalid User Login");

        loginPage.login(validUser, validPassword);

        Thread.sleep(5000);

        String username = loginPage.getYzrUsername();
        Assert.assertEquals(username, validUser);
    }

}
