package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InsightsPage extends PageBase {

    public InsightsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[1]")
    WebElement yearDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[2]")
    WebElement monthDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[div[contains(text(),'Total Expenses')]]/p[@class='card-text']")
    WebElement totalExpenses;

    @FindBy(how = How.XPATH, xpath = "//div[contains(text(),'Top Spending Category')]/following-sibling::div")
    WebElement topSpendingCategory;

    @FindBy(how = How.XPATH, xpath = "//div[div[contains(text(),'Total Income')]]/p[@class='card-text']")
    WebElement totalIncome;

    @FindBy(how = How.TAG_NAME, tagName = "canvas")
    WebElement chartCanvas;

    @FindBy(how = How.XPATH, xpath = "//button[contains(text(),'Back')]")
    WebElement backButton;

    @FindBy(how = How.CLASS_NAME, className = "logout_button")
    WebElement logoutButton;

    public void selectYear(String year) {
        waitElementToDisplay(yearDropdown, 3);
        new Select(yearDropdown).selectByValue(year);
    }

    public void selectMonth(String month) {
        waitElementToDisplay(monthDropdown, 3);
        new Select(monthDropdown).selectByValue(month);
    }

    public String getTopCategory() {
        waitElementToDisplay(topSpendingCategory, 3);
        return topSpendingCategory.getText().trim();
    }

    public String getTotalIncome() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement totalIncomeElement = wait.until(driver ->
                driver.findElement(By.xpath("//div[div[contains(text(),'Total Income')]]/p[@class='card-text']"))
        );
        return totalIncomeElement.getText().replaceAll("[^0-9]", "").trim();
    }

    public String getTotalExpenses() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement totalExpensesElement = wait.until(driver ->
                driver.findElement(By.xpath("//div[div[contains(text(),'Total Expenses')]]/p[@class='card-text']"))
        );
        return totalExpensesElement.getText().replaceAll("[^0-9]", "").trim();
    }


    public boolean isChartDisplayed() {
        waitElementToDisplay(chartCanvas, 3);
        return chartCanvas.isDisplayed();
    }

    public void clickBack() {
        waitElementToBeClickable(backButton, 3);
        backButton.click();
    }

    public void logout() {
        waitElementToBeClickable(logoutButton, 3);
        logoutButton.click();
    }
}
