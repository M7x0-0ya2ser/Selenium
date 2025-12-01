package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SignupPage extends PageBase {

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.ID, id = "firstName")
    WebElement firstName;

    @FindBy(how = How.ID, id = "lastName")
    WebElement lastName;

    @FindBy(how = How.ID, id = "email")
    WebElement email;

    @FindBy(how = How.ID, id = "password")
    WebElement password;

    @FindBy(how = How.CLASS_NAME, className = "occupation-option")
    List<WebElement> occupationOptions;

    @FindBy(how = How.CLASS_NAME, className = "auth-button")
    WebElement createAccountButton;

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'alert') or contains(@class,'error')]")
    WebElement alertMessage;

    public void enterFirstName(String fname) {
        waitElementToDisplay(firstName, 3);
        firstName.clear();
        firstName.sendKeys(fname);
    }

    public void enterLastName(String lname) {
        waitElementToDisplay(lastName, 3);
        lastName.clear();
        lastName.sendKeys(lname);
    }

    public void enterEmail(String emailText) {
        waitElementToDisplay(email, 3);
        email.clear();
        email.sendKeys(emailText);
    }

    public void enterPassword(String pass) {
        waitElementToDisplay(password, 3);
        password.clear();
        password.sendKeys(pass);
    }

    public void selectOccupation(String occupation) {
        waitElementsToDisplay(occupationOptions, 3);
        for (WebElement option : occupationOptions) {
            if (option.getText().equalsIgnoreCase(occupation)) {
                option.click();
                break;
            }
        }
    }

    public void waitElementsToDisplay(List<WebElement> elements, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void acceptAlertIfPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert: " + alert.getText());
            alert.accept();
        } catch (NoAlertPresentException e) {
        }
    }

    public void clickCreateAccount() {
        waitElementToBeClickable(createAccountButton, 3);
        createAccountButton.click();
        handleAlertIfPresent();
    }

    public void handleAlertIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
            // ignore
        }
    }


    public void signup(String fname, String lname, String mail, String pass, String occupation) {
        enterFirstName(fname);
        enterLastName(lname);
        enterEmail(mail);
        enterPassword(pass);
        selectOccupation(occupation);
        clickCreateAccount();
    }

    public String getAlertMessage() {
        waitElementToDisplay(alertMessage, 3);
        return alertMessage.getText();
    }
}
