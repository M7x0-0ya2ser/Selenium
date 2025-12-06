package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage extends PageBase {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.ID, id = "email")
    WebElement emailInput;

    @FindBy(how = How.ID, id = "password")
    WebElement passwordInput;

    @FindBy(how = How.CLASS_NAME, className = "login-button")
    WebElement loginButton;

    @FindBy(how = How.CLASS_NAME, className = "error-text")
    List<WebElement> errorMessages;

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'alert') or contains(@class,'error')]")
    WebElement alertMessage;


    public void enterEmail(String emailText) {
        waitElementToDisplay(emailInput, 3);
        emailInput.clear();
        emailInput.sendKeys(emailText);
    }

    public void enterPassword(String pass) {
        waitElementToDisplay(passwordInput, 3);
        passwordInput.clear();
        passwordInput.sendKeys(pass);
    }

    public void clearEmailField() {
        waitElementToDisplay(emailInput, 2);
        emailInput.clear();
    }

    public void clearPasswordField() {
        waitElementToDisplay(passwordInput, 2);
        passwordInput.clear();
    }

    public void clearAllFields() {
        clearEmailField();
        clearPasswordField();
    }

    public void handleAlertIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
        }
    }

    public void clickLogin() {
        waitElementToBeClickable(loginButton, 3);
        loginButton.click();
        handleAlertIfPresent();
    }

    public void login(String email, String password) {

        enterEmail(email);
        enterPassword(password);
        clickLogin();
    }


    public void waitElementsToDisplay(List<WebElement> elements, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public String getAllErrors() {
        waitElementsToDisplay(errorMessages, 10);

        StringBuilder errors = new StringBuilder();
        for (WebElement e : errorMessages) {
            errors.append(e.getText()).append(" | ");
        }
        return errors.toString().trim();
    }

    public String getAlertMessage() {
        waitElementToDisplay(alertMessage, 2);
        return alertMessage.getText();
    }
}
