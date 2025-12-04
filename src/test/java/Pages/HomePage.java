package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends PageBase {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.CLASS_NAME, className = "logout_button")
    WebElement logoutButton;

    @FindBy(how = How.CLASS_NAME, className = "insights-button")
    WebElement insightsButton;

    @FindBy(how = How.XPATH, xpath = "//p[contains(@class,'user-id')]")
    WebElement userIdLabel;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Income Form']/following::input[@name='transactionDate'][1]")
    WebElement incomeDate;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Income Form']/following::input[@name='amount'][1]")
    WebElement incomeAmount;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Income Form']/following::button[text()='Submit'][1]")
    WebElement incomeSubmit;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Expense Form']/following::input[@name='transactionDate'][1]")
    WebElement expenseDate;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Expense Form']/following::input[@name='amount'][1]")
    WebElement expenseAmount;

    @FindBy(how = How.ID, id = "category")
    WebElement categorySelect;

    @FindBy(how = How.XPATH, xpath = "//h2[text()='Expense Form']/following::button[text()='Submit'][1]")
    WebElement expenseSubmit;

    @FindBy(how = How.XPATH, xpath = "//button[text()='History']")
    WebElement historyButton;

    public String getUserId() {
        waitElementToDisplay(userIdLabel, 3);
        return userIdLabel.getText();
    }

    public void submitIncome(String date, String amount) {
        waitElementToDisplay(incomeDate, 2);
        incomeDate.sendKeys(date);
        incomeAmount.sendKeys(amount);
        incomeSubmit.click();
        handleAlertIfPresent();
    }

    public void submitExpense(String date, String amount, String category) {
        waitElementToDisplay(expenseDate, 3);
        expenseDate.sendKeys(date);
        expenseAmount.sendKeys(amount);
        selectCategory(category);
        expenseSubmit.click();
        handleAlertIfPresent();
    }

    public void selectCategory(String category) {
        waitElementToDisplay(categorySelect, 3);
        categorySelect.click();
        categorySelect.findElement(By.xpath("//option[text()='" + category + "']")).click();
    }

    public void goToHistory() {
        waitElementToBeClickable(historyButton, 3);
        historyButton.click();
    }

    public void clickInsights() {
        waitElementToBeClickable(insightsButton, 3);
        insightsButton.click();
    }

    public void logout() {
        waitElementToBeClickable(logoutButton, 3);
        logoutButton.click();
    }

    public void handleAlertIfPresent() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (Exception ignored) {}
    }
}