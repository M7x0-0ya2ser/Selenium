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
    public List<WebElement> transactionRows;

    @FindBy(how = How.CLASS_NAME, className = "logout_button")
    WebElement logoutButton;


    public void selectYear(String year) {
        waitElementToDisplay(yearDropdown, 3);
        Select select = new Select(yearDropdown);
        select.selectByValue(year);
    }

    public void selectMonth(String month) {
        waitElementToDisplay(monthDropdown, 3);
        Select select = new Select(monthDropdown);
        select.selectByValue(month);
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

    public boolean isTransactionPresent(String date, String amount) {
        waitElementsToDisplay(transactionRows, 5);
        List<WebElement> rows =  driver.findElements((By) transactionRows);
        for (WebElement row : rows) {
            if (row.getText().contains(date) && row.getText().contains(amount)) {
                return true;
            }
        }
        return false;
    }

    public void editTransactionDate(int index, String newDate) {
        WebElement row = transactionRows.get(index);
        WebElement dateInput = row.findElement(By.name("transactionDate"));
        waitElementToBeClickable(dateInput, 3);
        dateInput.clear();
        dateInput.sendKeys(newDate);
    }

    public void editAmount(int index, String newAmount) {
        WebElement row = transactionRows.get(index);
        WebElement amountInput = row.findElement(By.name("amount"));
        waitElementToBeClickable(amountInput, 3);
        amountInput.clear();
        amountInput.sendKeys(newAmount);
    }

    public void selectCategory(int index, String categoryName) {
        WebElement row = transactionRows.get(index);
        WebElement categoryDropdown = row.findElement(By.name("category"));
        waitElementToDisplay(categoryDropdown, 3);
        new Select(categoryDropdown).selectByVisibleText(categoryName);
    }


    public String getUserIdText() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement userIdElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//p[contains(@class,'user-id')]")
            ));
            return userIdElement.getText().trim();
        } catch (TimeoutException e) {
            return "User ID element not found!";
        }
    }

    public void logout() {
        waitElementToBeClickable(logoutButton, 3);
        logoutButton.click();
    }
}
