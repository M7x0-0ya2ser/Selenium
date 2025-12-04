package TestCases;

import Pages.LoginPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import Util.PageBase;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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

    @DataProvider(name = "loginDataFromJSON")
    public Object[][] loginDataFromJSON() throws Exception {

        String json = new String(Files.readAllBytes(Paths.get("src/test/java/users.json")));
        org.json.JSONArray arr = new JSONArray(json);
        Object[][] data = new Object[arr.length()][2];

        for (int i = 0; i < arr.length(); i++) {
            JSONObject user = arr.getJSONObject(i);
            data[i][0] = user.getString("email");
            data[i][1] = user.getString("password");
        }
        return data;
    }


    @Test(dataProvider = "loginDataFromJSON")
    public void loginTestWithJSON(String email, String password) throws Exception {

        loginPage.login(email, password);

        driver.get("http://localhost:5173/");

        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/");
    }


    @Test
    public void validLoginTest() throws Exception {
        loginPage.login("Muhammad.Yasser1764711285192@gmail.com", "StR0n9P@$$w0rd");
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/main");
    }

    @Test
    public void missingLoginTest() throws Exception {
        loginPage.login("", "");
        String errors = loginPage.getAllErrors();
        Assert.assertTrue(errors.contains("Email is required"));
        Assert.assertTrue(errors.contains("Password is required"));
    }

    @Test
    public void invaliddataLoginTest() throws Exception {
        loginPage.login("Muhammad2@gmail.com", "StR0n9P@$$w0rd");

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
