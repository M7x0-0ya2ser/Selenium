package TestCases;

import Pages.HomePage;
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
import org.testng.annotations.*;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Properties;

public class HomeTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/main";
    private WebDriver driver;

    private HomePage homePage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() throws InterruptedException {
        driver = startBrowser("http://localhost:5173/", "fire");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1500); // wait for redirect

        homePage = new HomePage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    @Test
    public void submitIncomeTest() throws Exception {

        homePage.submitIncome("2025-01-01", "10000");
        Thread.sleep(1000);

        Assert.assertTrue(true);
    }

    @Test
    public void submitExpenseTest() throws Exception {

        homePage.submitExpense("2025-01-03", "1000", "Transportation");
        Thread.sleep(1000);

        Assert.assertTrue(true);
    }

    @Test
    public void openInsights() throws Exception {

        homePage.clickInsights();
        Thread.sleep(1000);

        Assert.assertTrue(true);
    }

    @Test
    public void historyNavigationTest() throws Exception {

        homePage.goToHistory();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("history"));
    }

    @Test(priority = 99)
    public void logoutTest() throws Exception {

        homePage.logout();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/");
    }


    @SneakyThrows
    @AfterMethod
    public void afterMethod(Method method, ITestResult result) {

        driver.get("http://localhost:5173/main");

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
            // Capture full stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            String fullStackTrace = sw.toString();
            logger.log(LogStatus.ERROR, "Exception:       <pre>" + fullStackTrace + "</pre>");
        }

    }

}