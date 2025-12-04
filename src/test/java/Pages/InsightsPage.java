package Pages;

import Util.PageBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class InsightsPage extends PageBase {

    public InsightsPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[1]")
    WebElement yearDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[contains(@class,'date-selector')]/select[2]")
    WebElement monthDropdown;

    @FindBy(how = How.XPATH, xpath = "//div[contains(text(),'Total Expenses')]/following-sibling::div")
    WebElement totalExpenses;

    @FindBy(how = How.XPATH, xpath = "//div[contains(text(),'Top Spending Category')]/following-sibling::div")
    WebElement topSpendingCategory;

    @FindBy(how = How.XPATH, xpath = "//div[contains(text(),'Total Income')]/following-sibling::div")
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

    public String getTotalExpenses() {
        waitElementToDisplay(totalExpenses, 3);
        return totalExpenses.getText().trim();
    }

    public String getTopCategory() {
        waitElementToDisplay(topSpendingCategory, 3);
        return topSpendingCategory.getText().trim();
    }

    public String getTotalIncome() {
        waitElementToDisplay(totalIncome, 3);
        return totalIncome.getText().trim();
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
