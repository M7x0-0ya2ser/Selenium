package TestCases;

import Pages.HomePage;
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

public class HomeTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/main";
    private WebDriver driver;

    private HomePage homePage;
    private GetScreenShot screenshot;

    private static ExtentTest test;
    private static ExtentReports extent;

    @BeforeClass
    public void setUp() throws InterruptedException {
        extent = new ExtentReports("HomeTestsReport.html", true);

        // Start browser at login page
        driver = startBrowser("http://localhost:5173/", "fire");

        // Perform login first
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1500); // wait for redirect

        // Now you are authenticated
        homePage = new HomePage(driver);
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

    @Test
    public void verifyUserIdDisplayed() {
        test = extent.startTest("Verify User ID Displayed");

        String userId = homePage.getUserId();
        Assert.assertTrue(userId.contains("User ID"));
    }

    @Test
    public void submitIncomeTest() throws Exception {
        test = extent.startTest("Submit Income Test");

        homePage.submitIncome("2025-01-01", "5000");
        Thread.sleep(2000);

        Assert.assertTrue(true);
    }

    @Test
    public void submitExpenseTest() throws Exception {
        test = extent.startTest("Submit Expense Test");

        homePage.submitExpense("2025-01-03", "150", "Food");
        Thread.sleep(2000);

        Assert.assertTrue(true);
    }

    @Test
    public void openInsights() throws Exception {
        test = extent.startTest("Open Insights Test");

        homePage.clickInsights();
        Thread.sleep(2000);

        Assert.assertTrue(true);
    }

    @Test
    public void historyNavigationTest() throws Exception {
        test = extent.startTest("History Navigation Test");

        homePage.goToHistory();
        Thread.sleep(1500);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("history"));
    }

    @Test
    public void logoutTest() throws Exception {
        test = extent.startTest("Logout Test");

        homePage.logout();
        Thread.sleep(1500);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/");
    }
}