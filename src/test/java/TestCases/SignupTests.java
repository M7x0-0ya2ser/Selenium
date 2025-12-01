package TestCases;

import Pages.SignupPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import Util.PageBase;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

public class SignupTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/Signup";
    private WebDriver driver;

    private SignupPage signupPage;
    private GetScreenShot screenshot;

    private static ExtentTest test;
    private static ExtentReports extent;

    @BeforeClass
    public void setUp() {

        extent = new ExtentReports("SignupTestsReport.html", true);
        driver = startBrowser(URL, "fire");

        signupPage = new SignupPage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        if (test != null) {
            extent.endTest(test);
        }
        extent.flush();
        driver.quit();
    }

    // Test Cases

    @Test
    public void validSignupTest() throws Exception {
        test = extent.startTest("Valid Signup Test");

        signupPage.signup("John", "Doe", "john.doe" + System.currentTimeMillis() + "@gmail.com", "StR0n9P@$$w0rd", "Full-time Employee");

        Thread.sleep(2000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , "http://localhost:5173/");
    }

    @Test
    public void signupMissingFieldsTest() throws Exception {

        test = extent.startTest("Invalid Signup - Missing Fields");

        signupPage.signup("", "Doe", "wrongemailformat@test", "", "Student");

        String alert = signupPage.getAlertMessage();
        Assert.assertTrue(alert.contains("All fields are required"));
    }
}
