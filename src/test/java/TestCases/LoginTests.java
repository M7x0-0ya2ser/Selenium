package TestCases;

import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import Util.PageBase;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class LoginTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/";
    private WebDriver driver;
    private LoginPage loginPage;
    private PageBase PageBase;
    private GetScreenShot screenshot;

    @BeforeClass
    public void setUp() {
        driver = startBrowser(URL, "fire");
        loginPage = new LoginPage(driver);
        PageBase = new PageBase(driver);
        screenshot = new GetScreenShot();
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }


    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return new Object[][]{
                {
                        "Muhammmad" + System.currentTimeMillis() + "@gmail.com",
                        "StR0n9P@$$w0rd"
                }
        };
    }

    @Test(dataProvider = "validLoginData")
    public void validLoginTest(String email, String password) throws Exception {
        System.out.println("Generated Email For Test: " + email);

        loginPage.login(email, password);

        Thread.sleep(1000);

        Assert.assertEquals(driver.getCurrentUrl(), URL);
    }

    @DataProvider(name = "missingLoginData")
    public Object[][] missingLoginData() {
        return new Object[][]{
                {"", ""},
                {"", "password123"},
                {"test@gmail.com", ""}
        };
    }

    @Test(dataProvider = "missingLoginData")
    public void missingLoginTest(String email, String password) throws Exception {

        loginPage.clearAllFields();

        loginPage.login(email, password);

        String errors = loginPage.getAllErrors();

        if (email.isEmpty()) {
            Assert.assertTrue(errors.contains("Email is required"));
        }
        if (password.isEmpty()) {
            Assert.assertTrue(errors.contains("Password is required"));
        }
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {

        return new Object[][]{
                {"Muhamad2@gmail.com", "StR0n9P@$$w0rd"},
                {"admin@admin.com", "2323"}
        };
    }

    @Test(dataProvider = "invalidLoginData")
    public void invaliddataLoginTest(String email, String password) throws Exception {

        loginPage.login(email, password);

        Thread.sleep(1000);

        String alert = loginPage.getAlertMessage();

        Assert.assertTrue(alert.contains("Invalid email or password"));
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
