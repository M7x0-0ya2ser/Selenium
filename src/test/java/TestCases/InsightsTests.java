package TestCases;

import Pages.InsightsPage;
import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class InsightsTests extends Browser_Initiation {

    private final String LOGIN_URL = "http://localhost:5173/";
    private final String INSIGHTS_URL = "http://localhost:5173/insights";
    private final String HOME_URL = "http://localhost:5173/main";
    private GetScreenShot screenshot;
    private WebDriver driver;
    private InsightsPage insightsPage;

    @BeforeClass
    public void setUp() throws Exception {

        driver = startBrowser(LOGIN_URL, "fire");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1000);

        driver.get(INSIGHTS_URL);
        Thread.sleep(1200);
        screenshot = new GetScreenShot();
        insightsPage = new InsightsPage(driver);
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    @Test
    public void testSelectYearAndMonth() throws Exception {
        driver.get(INSIGHTS_URL);
        insightsPage.selectYear("2025");
        insightsPage.selectMonth("12");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , INSIGHTS_URL);
        insightsPage.takeScreenshot("Summary & Insights");
    }


    @Test
    public void testBackButton() throws Exception {
        insightsPage.clickBack();
        Thread.sleep(800);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , HOME_URL);
    }

    @Test(priority = 99)
    public void testLogout() throws Exception {

        insightsPage.logout();
        Thread.sleep(1000);

        Assert.assertEquals(driver.getCurrentUrl(), LOGIN_URL);
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
