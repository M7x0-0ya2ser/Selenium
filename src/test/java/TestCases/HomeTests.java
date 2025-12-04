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

    private final String URL = "http://localhost:5173/main";
    private WebDriver driver;

    private HomePage homePage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() throws InterruptedException {
        driver = startBrowser("http://localhost:5173/", "edge");

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

    @Test
    public void submitIncomeTest() throws Exception {
        String date = "2025-01-01";
        String amount = "234";

        homePage.submitIncome(date, amount);

        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Assert.assertTrue(historyPage.isTransactionPresent(date, amount), "Submitted income not found in history!");
    }

    @Test
    public void submitExpenseTest() throws Exception {
        String date = "2025-01-03";
        String amount = "1000";
        String category = "Transportation";

        homePage.submitExpense(date, amount, category);

        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Assert.assertTrue(historyPage.isTransactionPresent(date, amount), "Submitted expense not found in history!");
    }

    @Test(description = "Bug")
    public void submitNegativeIncomeTest() throws Exception {
        String date = "2025-01-01";
        String amount = "-250";

        homePage.submitIncome(date, amount);

        homePage.goToHistory();
        HistoryPage historyPage = new HistoryPage(driver);
        historyPage.selectYear("2025");
        historyPage.selectMonth("1");

        Assert.assertTrue(historyPage.isTransactionPresent(date, amount), "You can't make a trx with negative value!");
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
        Assert.assertEquals(currentUrl, "http://localhost:5173/");

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