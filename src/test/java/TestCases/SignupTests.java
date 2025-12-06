package TestCases;

import Pages.SignupPage;
import Util.Browser_Initiation;
import Util.GetScreenShot;
import com.relevantcodes.extentreports.LogStatus;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class SignupTests extends Browser_Initiation {

    private WebDriver driver;
    private final String URL = "http://localhost:5173/Signup";

    private SignupPage signupPage;

    @BeforeClass
    public void setUp() {
        driver = startBrowser(URL, "edge");
        signupPage = new SignupPage(driver);
    }

    @BeforeMethod
    public void openSignupPage() {
        driver.get(URL);
    }

    @AfterSuite
    public void tearDownSuite() {
        driver.quit();
    }

    @DataProvider(name = "validSignupData")
    public Object[][] validSignupData() {
        return new Object[][]{
                {
                        "Muhammad",
                        "Yasser",
                        "user" + System.currentTimeMillis() + "@gmail.com",
                        "StR0n9P@$$w0rd",
                        "Full-time Employee"
                }
        };
    }

    @Test(dataProvider = "validSignupData")
    public void validSignupTest(String fname, String lname, String email, String password, String occupation) throws Exception {

        System.out.println("Generated Email: " + email);

        signupPage.signup(fname, lname, email, password, occupation);

        Thread.sleep(1500);

        Assert.assertEquals(driver.getCurrentUrl(), "http://localhost:5173/");
    }

    @DataProvider(name = "missingFieldsData")
    public Object[][] missingFieldsData() {

        return new Object[][]{
                {"", "Yasser", "user" + System.currentTimeMillis() + "@gmail.com", "StR0n9P@$$w0rd", "Full-time Employee"},
                {"Mostafa", "", "mostafa" + System.currentTimeMillis() + "@gmail.com", "Pass12345!", "Student"},
                {"Mahmoud", "Karim", "", "SecureP@ss1", "Freelancer"},
                {"Mahmoud", "Ezzat", "mahmoud" + System.currentTimeMillis() + "@gmail.com", "MyP@ssw0rd123", ""}
        };
    }

    @Test(dataProvider = "missingFieldsData")
    public void signupMissingFieldsTestWithDataProvider(String fname, String lname, String email, String password, String occupation) throws Exception {

        signupPage.signup(fname, lname, email, password, occupation);

        Thread.sleep(800);

        String alert = signupPage.getAlertMessage();
        Assert.assertTrue(alert.contains("All fields are required"));
    }

    @DataProvider(name = "invalidEmailData")
    public Object[][] invalidEmailData() {

        return new Object[][]{
                {"Muhammad", "Yasser", "invalidemail", "StR0n9P@$$w0rd", "Student"},
                {"Ali", "Omar", "wrong.format", "StrongPass123!", "Teacher"},
                {"Sara", "Mahmoud", "aaaaa", "StrongPass12!", "Student"}
        };
    }

    @Test(dataProvider = "invalidEmailData")
    public void signupInvalidEmailTest(String fname, String lname, String email, String password, String occupation) throws Exception {

        signupPage.signup(fname, lname, email, password, occupation);

        signupPage.clickCreateAccount();

        String validationMsg = signupPage.getEmailValidationMessage();

        Assert.assertTrue(validationMsg.contains("Please include an '@'"));
    }

    @DataProvider(name = "existedEmailData")
    public Object[][] existedEmailData() {
        return new Object[][]{
                {"Muhammad", "Yasser", "admin@admin.com", "StR0n9P@$$w0rd", "Student"},
        };
    }

    @Test(dataProvider = "existedEmailData")
    public void signupWithExistedEmailTest(String fname, String lname, String email, String password, String occupation) throws Exception {

        signupPage.signup(fname, lname, email, password, occupation);

        String alert = signupPage.getAlertMessage();

        Assert.assertTrue(alert.contains("Email already exists"));
    }


    @DataProvider(name = "weakPasswordData")
    public Object[][] weakPasswordData() {

        return new Object[][]{
                {"Muhammad", "Yasser", "user" + System.currentTimeMillis() + "@gmail.com", "S", "Student"},
                {"Ali", "Ibrahim", "test" + System.currentTimeMillis() + "@gmail.com", "123", "Student"},
                {"Sara", "Kamal", "sara" + System.currentTimeMillis() + "@gmail.com", "weak", "Student"}
        };
    }

    @Test(dataProvider = "weakPasswordData")
    public void signupWithWeakPasswordTest(String fname, String lname, String email, String password, String occupation) throws Exception {

        signupPage.signup(fname, lname, email, password, occupation);

        String alertMessage = signupPage.getAlertMessage();

        Assert.assertEquals(alertMessage, "At least 8 characters");
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
