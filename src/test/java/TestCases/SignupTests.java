package TestCases;

import Pages.SignupPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import Util.PageBase;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class SignupTests extends Browser_Initiation {

    private final String URL = "http://localhost:5173/Signup";
    private WebDriver driver;

    private SignupPage signupPage;
    private GetScreenShot screenshot;


    @BeforeClass
    public void setUp() {

        driver = startBrowser(URL, "fire");

        signupPage = new SignupPage(driver);
        screenshot = new GetScreenShot();
    }

    @DataProvider(name = "signupData")
    public Object[][] signupData() {
        return new Object[][]{
                {"Muhammad", "Yasser", "Muhammad.Yasser" + System.currentTimeMillis() + "@gmail.com", "StR0n9P@$$w0rd", "Full-time Employee"},
                {"Mostafa", "Hussein", "Mostafa.Hussein" + System.currentTimeMillis() + "@gmail.com", "Pass12345!", "Student"},
                {"Mahmoud", "Kaarem", "Mahmoud.Kaarem" + System.currentTimeMillis() + "@gmail.com", "SecureP@ss1", "Freelancer"},
                {"Mahmoud", "Ezzat", "Mahmoud.Ezzat" + System.currentTimeMillis() + "@gmail.com", "MyP@ssw0rd123", "Student"}
        };
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }


    @Test(dataProvider = "signupData")
    public void signupTestWithDataProvider(String fname, String lname, String email, String password, String occupation) throws Exception {
        signupPage.signup(fname, lname, email, password, occupation);

        driver.get("http://localhost:5173/Signup");

        System.out.println("Generated Email: " + email);

        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:5173/Signup");
    }

    @Test
    public void validSignupTest() throws Exception {

        String email = "Muhammad.Yasser" + System.currentTimeMillis() + "@gmail.com";

        System.out.println("Generated Email: " + email);

        signupPage.signup("Muhammad", "Yasser", email, "StR0n9P@$$w0rd", "Full-time Employee");

        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl , "http://localhost:5173/");
    }

    @Test
    public void signupMissingFieldsTest() throws Exception {

        signupPage.signup("", "Yasser", "wrongemailformat@test", "", "Student");

        String alert = signupPage.getAlertMessage();

        Assert.assertTrue(alert.contains("All fields are required"));
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
