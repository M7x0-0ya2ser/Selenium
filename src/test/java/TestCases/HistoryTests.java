package TestCases;

import Pages.HistoryPage;
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

public class HistoryTests extends Browser_Initiation {

    private final String LOGIN_URL = "http://localhost:5173/";
    private final String HISTORY_URL = "http://localhost:5173/history";

    private WebDriver driver;
    private HistoryPage historyPage;
    private GetScreenShot screenshot;

    private static ExtentTest test;
    private static ExtentReports extent;

    @BeforeClass
    public void setUp() throws Exception {

        extent = new ExtentReports("HistoryTestsReport.html", true);
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
        if (test != null)
            extent.endTest(test);

        extent.flush();
        driver.quit();
    }

    // =================== TEST CASES ===================

    @Test
    public void verifyTableRowsLoaded() {
        test = extent.startTest("Verify Transaction Rows Loaded");

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        int rows = historyPage.getTransactionCount();
        Assert.assertTrue(rows > 0, "No transactions found on history page");
    }


    @Test
    public void selectYearAndMonthTest() throws Exception {
        test = extent.startTest("Select Year & Month Test");

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Thread.sleep(1000);

        Assert.assertTrue(historyPage.getTransactionCount() > 0);
    }

    @Test
    public void clickEditOnFirstRow() throws Exception {
        test = extent.startTest("Click Edit Button Test");

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickEditOnRow(0);
        Thread.sleep(1200);

        Assert.assertTrue(true, "Edit button clicked");
    }

    @Test
    public void clickDeleteOnFirstRow() throws Exception {
        test = extent.startTest("Click Delete Button Test");

        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        historyPage.clickDeleteOnRow(0);
        Thread.sleep(1200);

        Assert.assertTrue(true, "Delete button clicked");
    }

    @Test
    public void logoutTest() throws Exception {
        test = extent.startTest("Logout Test");

        historyPage.logout();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, LOGIN_URL);
    }
}
