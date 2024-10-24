package org.example.ui.base_tests.page_objects.unitickets;

import org.example.ui.base_tests.selenium.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UtMainPage extends BasePage {
    private By cityFromField = By.xpath("//input[@placeholder='Откуда']");
    private By listOfCityFrom = By.xpath("//div[@class='origin field active']//div[@class='city']");
    private By cityToField = By.xpath("//input[@placeholder='Куда']");
    private By listOfCityTo = By.xpath("//div[@class='destination field active']//div[@class='city']");
    private By dateForward = By.xpath("//input[@placeholder='Туда']");
    private By dateBack = By.xpath("//input[@placeholder='Обратно']");
    private String dayInCalendar = "//span[text()='%d']";
    private By searchBtn = By.xpath("//div[@class='search_btn']");

    public UtMainPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(cityFromField));
        wait.until(ExpectedConditions.elementToBeClickable(searchBtn));
    }

    public UtMainPage setCityFrom(String city) {
        driver.findElement(cityFromField).clear();
        driver.findElement(cityFromField).sendKeys(city);
        driver.findElement(cityFromField).click();
        waitForTextPresentedInList(listOfCityFrom, city).click();
        return this;
    }

    public UtMainPage setCityTo(String city) {
        driver.findElement(cityToField).clear();
        driver.findElement(cityToField).sendKeys(city);
        driver.findElement(cityToField).click();
        waitForTextPresentedInList(listOfCityTo, city).click();
        return this;
    }

    public UtMainPage setDayForward(int day) {
        driver.findElement(dateForward).click();
        getDay(day).click();
        wait.until(ExpectedConditions.invisibilityOf(getDay(day)));
        return this;
    }

    public UtMainPage setDayBack(int day) {
        getDay(day).click();
        wait.until(ExpectedConditions.invisibilityOf(getDay(day)));
        return this;
    }

    private WebElement getDay(int day) {
        By dayLocator = By.xpath(String.format(dayInCalendar, day));
        return driver.findElement(dayLocator);
    }

    public UtSearchPage search() {
        driver.findElement(searchBtn).click();
        return new UtSearchPage(driver);
    }
}
