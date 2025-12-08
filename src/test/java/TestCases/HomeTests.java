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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.Properties;

public class HomeTests extends Browser_Initiation {
    private final String HOME_URL = "http://localhost:5173/main";
    private final String Login_URL = "http://localhost:5173/";
    private WebDriver driver;

    private HomePage homePage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() throws InterruptedException {
        driver = startBrowser(Login_URL, "edge");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@admin.com", "StR0n9P@$$w0rd");

        Thread.sleep(1500);

        homePage = new HomePage(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    @Test
    public void verifyUserIdIsDisplayed() {
        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(homePage.isUserIdDisplayed(), "User ID is not displayed!");

        String userIdText = homePage.getUserIdText();
        System.out.println(userIdText);
        Assert.assertTrue(userIdText.startsWith("User ID:"), "User ID text is incorrect!");
    }

    @DataProvider(name = "submitIncomeData")
    public Object[][] submitIncomeData() {
        return new Object[][]{
                {"01-01-2025", "567","2025","1", "2025-01-01"}
        };
    }

    @Test(dataProvider = "submitIncomeData")
    public void submitIncomeTest(String date,String amount, String year, String month, String outdate) throws Exception {
        homePage.submitIncome(date, amount);
        Thread.sleep(5000);
        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear(year);
        historyPage.selectMonth(month);
        Thread.sleep(5000);
        Assert.assertTrue(historyPage.isTransactionPresent(outdate, amount), "Submitted income not found in history!");
    }

    @DataProvider(name = "submitExpenseData")
    public Object[][] submitExpenseData() {
        return new Object[][]{
                {"02-02-2025", "123","2025","2", "Transportation", "2025-02-02"}
        };
    }

    @Test(dataProvider = "submitExpenseData")
    public void submitExpenseTest(String date,String amount, String year, String month, String category, String outdate) throws Exception {
        homePage.submitExpense(date, amount, category);
        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear(year);
        historyPage.selectMonth(month);
        Assert.assertTrue(historyPage.isTransactionPresent(outdate, amount), "Submitted expense not found in history!");
    }

    @DataProvider(name = "submitNegativeIncomeData")
    public Object[][] submitNegativeIncomeData() {
        return new Object[][]{
                {"12-12-2025", "-300","2025","12", "2025-12-12"}
        };
    }

    @Test(dataProvider = "submitIncomeData", description = "Bug")
    public void submitNegativeIncomeTest(String date,String amount, String year, String month, String outdate) throws Exception {
        homePage.submitIncome(date, amount);
        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear(year);
        historyPage.selectMonth(month);
        Assert.assertTrue(historyPage.isTransactionPresent(outdate, amount), "You can't make a trx with negative value!");
    }

    @SneakyThrows
    @Test
    public void submitIncomeMissingDateTest() {
        WebElement dateInput = driver.findElement(By.id("date"));
        WebElement amountInput = driver.findElement(By.id("amount"));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        dateInput.clear();
        amountInput.clear();
        amountInput.sendKeys("1000");

        submitBtn.click();

        Thread.sleep(2000);

        String message = homePage.getValidationMessage(dateInput);
        Assert.assertEquals(message, "Please fill out this field.");
    }

    @SneakyThrows
    @Test
    public void submitIncomeMissingAmountTest() {
        WebElement dateInput = driver.findElement(By.id("date"));
        WebElement amountInput = driver.findElement(By.id("amount"));
        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit']"));

        dateInput.clear();
        dateInput.sendKeys("2025-01-01");
        amountInput.clear();

        submitBtn.click();

        Thread.sleep(2000);

        String message = homePage.getValidationMessage(amountInput);
        Assert.assertEquals(message, "Please fill out this field.");
    }

    @Test
    public void openInsights() throws Exception {

        homePage.clickInsights();
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("insights"));
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

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, Login_URL);

        String userIdText;
        try {
            userIdText = homePage.getUserIdText();
        } catch (NoSuchElementException e) {
            userIdText = "User ID element not found!";
        }

        Assert.assertEquals(userIdText, "User ID element not found!");
    }


    @SneakyThrows
    @AfterMethod
    public void afterMethod(Method method, ITestResult result) {

        driver.get(HOME_URL);

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