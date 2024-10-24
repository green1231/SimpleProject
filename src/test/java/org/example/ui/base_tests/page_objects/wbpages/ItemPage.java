package org.example.ui.base_tests.page_objects.wbpages;

import org.example.ui.base_tests.selenium.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ItemPage extends BasePage {
    private By itemHeaderName = By.xpath("//div[@class='product-page__header']//h1");
    private By itemPrice = By.xpath("//span[@class='price-block__price']");
    public ItemPage(WebDriver driver) {
        super(driver);
    }
    public String getItemName(){
        return driver.findElement(itemHeaderName).getText();
    }

    public Integer getItemPrice(){
        String priceText = getTextJs(itemPrice);
        priceText = priceText.replaceAll("[^0-9.]", "");
        return Integer.parseInt(priceText);
    }
}
