package Pages;

import Util.PageBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


public class LoginPage extends PageBase {


    // Elements
    @FindBy(how = How.ID, id = "username")
    WebElement username;
    @FindBy(how = How.ID, id = "password")
    WebElement password;
    @FindBy(how = How.ID, id = "btnSubmit")
    WebElement signInButton;  // Used a more stable XPath instead of absolute path
    @FindBy(how = How.XPATH, xpath = "/html/body/div/div/div/div")
    WebElement alertMessage;  // Used a more stable XPath
    @FindBy(how = How.ID, id = "details-button")
    WebElement advancedButtonSecurity;
    @FindBy(how = How.ID, id = "proceed-link")
    WebElement proceed;
    @FindBy(how = How.CLASS_NAME, className = "yzr-username")
    WebElement yzr_username;

    public LoginPage(WebDriver driver) {
        super(driver);

    }

    // Methods
    public void openLoginPage() {
        waitElementToDisplay(advancedButtonSecurity, 30);
        advancedButtonSecurity.click();
    }

    public void proceedToLoginPage() {
        waitElementToDisplay(proceed, 30);
        proceed.click();
    }

    public void login(String usernameText, String passwordText) {
        waitElementToDisplay(username, 30);
        waitElementToDisplay(password, 30);
        username.sendKeys(usernameText);
        password.sendKeys(passwordText);
        waitElementToBeClickable(signInButton, 30);
        signInButton.click();

    }

    public String getInvalidLoginAlert() {
        waitElementToBeClickable(alertMessage, 30);
        return alertMessage.getText();

    }

    public String getYzrUsername() {
        waitElementToBeClickable(yzr_username, 30);
        return yzr_username.getText();

    }
}

