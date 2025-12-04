package TestCases;

import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LoginTests extends Browser_Initiation {
    private final String URL = "http://localhost:5173/";
    private WebDriver driver;
    private LoginPage loginPage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() {
        driver = startBrowser(URL, "fire");
        loginPage = new LoginPage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    @Test
    public void validLoginTest() throws Exception {
        loginPage.login("Muhammad.Yasser1764711285192@gmail.com", "StR0n9P@$$w0rd");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/main");
    }

    @Test
    public void invalidLoginTest() throws Exception {
        loginPage.login("", "");
        String errors = loginPage.getAllErrors();
        Assert.assertTrue(errors.contains("Email is required"));
        Assert.assertTrue(errors.contains("Password is required"));
    }

    @SneakyThrows
    @AfterMethod
    public void afterMethod(Method method, ITestResult result) {

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.log(LogStatus.PASS, "Test Passed");
                break;
            case ITestResult.FAILURE:
                logger.log(LogStatus.FAIL, "Test Failed");
                break;
            default:
                logger.log(LogStatus.SKIP, "Test Skipped");
                break;
        }

        if (result.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            String fullStackTrace = sw.toString();
            logger.log(LogStatus.ERROR, "Exception:       <pre>" + fullStackTrace + "</pre>");
        }
    }
}
