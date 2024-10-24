package org.example.ui.base_tests.page_objects.wbpages;

import org.example.ui.base_tests.selenium.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class MainPage extends BasePage {
    private final By searchField = By.id("searchInput");

    public MainPage(WebDriver driver) {
        super(driver);
        waitPageLoadsWb();
    }

    public SearchResultPage searchItem(String item){
        driver.findElement(searchField).click();
        driver.findElement(searchField).sendKeys(item);
        driver.findElement(searchField).sendKeys(Keys.ENTER);
        return new SearchResultPage(driver);
    }
}
