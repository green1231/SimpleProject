package org.example.ui.base_tests.selenium;

import org.example.ui.base_tests.page_objects.wbpages.ItemPage;
import org.example.ui.base_tests.page_objects.wbpages.MainPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WbFilterTests extends BaseTest {
    @BeforeEach
    public void openSite(){
        driver.get("https://www.wildberries.ru/");
    }
    @Test
    public void searchResultTest(){
        String expectedItem = "iPhone";
        Integer expectedPriceMax = 60000;
        Integer expectedPriceMin = 36000;

        ItemPage itemPage = new MainPage(driver)
                .searchItem(expectedItem)
                .openFilters()
                .setMinPrice(expectedPriceMin)
                .setMaxPrice(expectedPriceMax)
                .applyFilters()
                .openItem();

        String actualName = itemPage.getItemName();
        Integer actualPrice = itemPage.getItemPrice();

        Assertions.assertTrue(actualName.toLowerCase().contains(expectedItem.toLowerCase()));
        Assertions.assertTrue(actualPrice >= expectedPriceMin && actualPrice <= expectedPriceMax);
    }
}
