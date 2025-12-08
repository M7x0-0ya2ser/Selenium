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
import org.testng.annotations.*;

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


    @DataProvider(name = "selectData")
    public Object[][] selectData() {
        return new Object[][]{
                {"2025", "1"}
        };
    }

    @Test(dataProvider = "selectData")
    public void verifyTableRowsLoaded(String year, String month) {

        driver.get(HISTORY_URL);

        historyPage.selectYear(year);
        historyPage.selectMonth(month);

        int rows = historyPage.getTransactionCount();
        Assert.assertTrue(rows > 0);
    }

    @DataProvider(name = "editData")
    public Object[][] editData() {
        return new Object[][]{
                {"2025", "12", "2025-12-12", "12-12-2025", "2500", "Food" }
        };
    }

    @Test(dataProvider = "selectData")
    public void selectYearAndMonthTest(String year, String month) throws Exception {
        driver.get(HISTORY_URL);

        historyPage.selectYear(year);
        historyPage.selectMonth(month);

        Thread.sleep(1000);

        int rowCount = historyPage.getTransactionCount();
        Assert.assertTrue(rowCount > 0, "No rows were loaded after selecting year and month");
    }

    @Test(dataProvider = "editData")
    public void clickEditOnFirstRow(String year, String month, String date, String outdate, String amount, String category) throws Exception {
        historyPage.selectYear(year);
        historyPage.selectMonth(month);

        historyPage.clickEditOnRow(0);

        historyPage.editTransactionDate(0, outdate);
        historyPage.editAmount(0, amount);
        historyPage.selectCategory(0, category);

        historyPage.clickEditOnRow(0);

        Assert.assertTrue(historyPage.isTransactionPresent(date, amount), "Transaction updated successfully");
    }

    @Test(dataProvider = "selectData")
    public void clickDeleteOnFirstRow(String year, String month) throws Exception {

        historyPage.selectYear(year);
        historyPage.selectMonth(month);

        Thread.sleep(1000);

        int beforeCount = historyPage.getTransactionCount();

        historyPage.clickDeleteOnRow(0);
        Thread.sleep(1500);

        int afterCount = historyPage.getTransactionCount();

        Assert.assertTrue(afterCount < beforeCount, "Row was NOT deleted. Before = " + beforeCount + ", After = " + afterCount);
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
