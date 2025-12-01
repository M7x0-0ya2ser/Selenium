package TestCases;

import Pages.InsightsPage;
import Pages.LoginPage;
import Util.Browser_Initiation;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;

public class InsightsTests extends Browser_Initiation {

    private final String LOGIN_URL = "http://localhost:5173/";
    private final String INSIGHTS_URL = "http://localhost:5173/insights";

    private WebDriver driver;
    private InsightsPage insightsPage;

    private static ExtentTest test;
    private static ExtentReports extent;

    @BeforeClass
    public void setUp() throws Exception {
        extent = new ExtentReports("InsightsTestsReport.html", true);

        driver = startBrowser(LOGIN_URL, "fire");

        // Login
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1000);

        // Navigate to Insights
        driver.get(INSIGHTS_URL);
        Thread.sleep(1200);

        insightsPage = new InsightsPage(driver);
    }

    @AfterSuite
    public void tearDownSuite() {
        //insightsPage.takeScreenshot("InsightsPage_Final");

        if (test != null)
            extent.endTest(test);

        extent.flush();
        driver.quit();
    }

    // ======================= TESTS ========================

    @Test
    public void testSelectYearAndMonth() throws Exception {
        test = extent.startTest("Select Year & Month Test");

        insightsPage.selectYear("2025");
        insightsPage.selectMonth("1");

        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , "http://localhost:5173/insights");

        insightsPage.takeScreenshot("Summary & Insights");
    }


    @Test
    public void testBackButton() throws Exception {
        test = extent.startTest("Click Back Button");

        insightsPage.clickBack();
        Thread.sleep(800);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , "http://localhost:5173/main");
    }

    @Test
    public void testLogout() throws Exception {
        test = extent.startTest("Logout Test");

        insightsPage.logout();
        Thread.sleep(1000);

        Assert.assertEquals(driver.getCurrentUrl(), LOGIN_URL);
    }
}
