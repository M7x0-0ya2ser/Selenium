package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HistoryPage extends PageBase {

    public HistoryPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[1]")
    WebElement yearDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[2]")
    WebElement monthDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[@class='transaction-row' and not(contains(@class,'header-row'))]")
    List<WebElement> transactionRows;

    @FindBy(how = How.CLASS_NAME, className = "logout_button")
    WebElement logoutButton;

    // ========================= METHODS =========================

    public void selectYear(String year) {
        waitElementToDisplay(yearDropdown, 3);
        Select select = new Select(yearDropdown);
        select.selectByValue(year);    // "2025"
    }

    public void selectMonth(String month) {
        waitElementToDisplay(monthDropdown, 3);
        Select select = new Select(monthDropdown);
        select.selectByValue(month);   // "1"
    }

    public void waitElementsToDisplay(List<WebElement> elements, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfAllElements(elements));
    }


    public int getTransactionCount() {
        waitElementsToDisplay(transactionRows, 3);
        return transactionRows.size();
    }

    public void clickEditOnRow(int index) {
        WebElement row = transactionRows.get(index);
        WebElement editBtn = row.findElement(By.className("edit-button"));
        waitElementToBeClickable(editBtn, 3);
        editBtn.click();
    }

    public void clickDeleteOnRow(int index) {
        WebElement row = transactionRows.get(index);
        WebElement deleteBtn = row.findElement(By.className("delete-button"));
        waitElementToBeClickable(deleteBtn, 3);
        deleteBtn.click();
    }

    public void logout() {
        waitElementToBeClickable(logoutButton, 3);
        logoutButton.click();
    }
}
