package org.example.ui.selenoid;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.$x;

@ExtendWith(AllureLogsExtension.class)
//Для каждого теста устанавливается параметр запуска
@BrowserType(browser = BrowserType.Browser.CHROME, isRemote = false)
public class SelectBrowserOnClassTests {
    @Test
    public void mobileVkHeaderTest(){
        Selenide.open("https://vk.com");
        $x("//div[@class='login_mobile_header']")
                .should(Condition.text("ВКонтакте для мобильных устройств"));
    }

    @Test
    public void threadQaMainPageTitleTest(){
        Selenide.open("https://threadqa.ru");
        $x("//h3").should(Condition.text("QA AUTOMATION"));
    }
}
