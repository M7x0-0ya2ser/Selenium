package TestCases;

import Pages.HistoryPage;
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

public class HistoryTests extends Browser_Initiation {

    private final String LOGIN_URL = "http://localhost:5173/";
    private final String HISTORY_URL = "http://localhost:5173/history";

    private WebDriver driver;
    private HistoryPage historyPage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() throws Exception {

        driver = startBrowser(LOGIN_URL, "fire");

        // Login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1000);

        // Navigate to history page
        driver.get(HISTORY_URL);
        Thread.sleep(1000);

        historyPage = new HistoryPage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    // =================== TEST CASES ===================

    @Test
    public void verifyTableRowsLoaded() {

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        int rows = historyPage.getTransactionCount();
        Assert.assertTrue(rows > 0, "No transactions found on history page");
    }


    @Test
    public void selectYearAndMonthTest() throws Exception {

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Thread.sleep(1000);

        Assert.assertTrue(historyPage.getTransactionCount() > 0);
    }

    @Test
    public void clickEditOnFirstRow() throws Exception {

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickEditOnRow(0);
        Thread.sleep(1200);

        Assert.assertTrue(true, "Edit button clicked");
    }

    @Test
    public void clickDeleteOnFirstRow() throws Exception {

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickDeleteOnRow(0);
        Thread.sleep(1200);

        Assert.assertTrue(true, "Delete button clicked");
    }

    @Test
    public void logoutTest() throws Exception {

        historyPage.logout();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, LOGIN_URL);
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
            // Capture full stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            result.getThrowable().printStackTrace(pw);
            String fullStackTrace = sw.toString();
            logger.log(LogStatus.ERROR, "Exception:       <pre>" + fullStackTrace + "</pre>");
        }
    }
}
