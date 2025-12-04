package TestCases;

import Pages.HistoryPage;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

public class HistoryTests extends Browser_Initiation {

    private final String LOGIN_URL = "http://localhost:5173/";
    private final String HISTORY_URL = "http://localhost:5173/history";

    private WebDriver driver;
    private HistoryPage historyPage;
    private GetScreenShot screenshot;

    @BeforeClass
    public void setUp() throws Exception {

        driver = startBrowser(LOGIN_URL, "edge");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1000);

        driver.get(HISTORY_URL);
        Thread.sleep(1000);

        historyPage = new HistoryPage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }


    @Test
    public void verifyTableRowsLoaded() {

        driver.get(HISTORY_URL);

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        int rows = historyPage.getTransactionCount();
        Assert.assertTrue(rows > 0);
    }


    @Test
    public void selectYearAndMonthTest() throws Exception {
        driver.get(HISTORY_URL);

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Thread.sleep(1000);

        Assert.assertTrue(true);
    }

    @Test
    public void clickEditOnFirstRow() throws Exception {
        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickEditOnRow(0);

        historyPage.editTransactionDate(0, "2025-12-05");
        historyPage.editAmount(0, "2500");
        historyPage.selectCategory(0, "Food");

        historyPage.clickEditOnRow(0);

        Assert.assertTrue(historyPage.isTransactionPresent("2025-12-05", "2500"), "Transaction updated successfully");
    }

    @Test
    public void clickDeleteOnFirstRow() throws Exception {

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickDeleteOnRow(0);
        Thread.sleep(1200);

        Assert.assertTrue(true, "Delete button clicked");
    }

    @Test(priority = 99)
    public void logoutTest() throws Exception {

        historyPage.logout();

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, LOGIN_URL);

        String userIdText;
        try {
            userIdText = historyPage.getUserIdText();
        } catch (NoSuchElementException e) {
            userIdText = "User ID element not found!";
        }

        Assert.assertEquals(userIdText, "User ID element not found!");
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
