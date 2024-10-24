package org.example.ui.base_tests.selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.example.ui.base_tests.page_objects.unitickets.UtMainSelenidePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;


public class UtSelenideTest {
    @Test
    public void tableCheckTest(){
        Selenide.open("http://85.192.34.140:8081/");
        $x("//h5[text()='Elements']").click();
        $x("//span[text()='Web Tables']").click();

        ElementsCollection rows = $$x("//div[@class='rt-tr-group']");

        List<String> rowsBefore = rows.texts()
                .stream()
                .map(x->x.trim())
                .filter(x -> !x.isEmpty())
                .toList();

        $x("//button[@id='addNewRecordButton']").click();
        $x("//input[@id='firstName']").sendKeys("John");
        $x("//input[@id='lastName']").sendKeys("Week");
        $x("//input[@id='userEmail']").sendKeys("testemial@mail.ru");
        $x("//input[@id='age']").sendKeys("20");
        $x("//input[@id='salary']").sendKeys("30000");
        $x("//input[@id='department']").sendKeys("VK");
        $x("//button[@id='submit']").click();


        List<String> rowsAfter = rows.texts()
                .stream()
                .map(String::trim)
                .filter(x -> !x.isEmpty())
                .toList();

        Assertions.assertNotEquals(rowsBefore, rowsAfter);
    }
    @Test
    public void firstSelenideTest(){
        int expectedDayForward = 25;
        int expectedDayBack = 30;

        Selenide.open("https://uniticket.ru/");

        UtMainSelenidePage mainPage = new UtMainSelenidePage();
        mainPage.setCityFrom("Казань")
                .setCityTo("Дубай")
                .setDayForward(expectedDayForward)
                .setDayBack(expectedDayBack)
                .search()
                .waitForPage()
                .waitForTitleDisappear()
                .assertMainDayBack(expectedDayBack)
                .assertMainDayForward(expectedDayForward)
                .assertAllDaysBackShouldHaveDay(expectedDayBack)
                .assertAllDaysForwardShouldHaveDay(expectedDayForward);
    }
}
