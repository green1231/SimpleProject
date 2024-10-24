package org.example.ui.selenoid;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ExtendWith(AllureLogsExtension.class)
public class SelectDifferentBrowsersTest {

    @AfterEach
    public void tearDown(){
        Selenide.closeWebDriver();
    }

    @Test
    @BrowserType(browser = BrowserType.Browser.CHROME, isRemote = false)
    public void selenoidTest(){
        Selenide.open("https://vk.com");
        $x("//div[@class='login_mobile_header']").should(Condition.text("wrong value"));
    }
    @Test
    @BrowserType(browser = BrowserType.Browser.FIREFOX, isRemote = false)
    public void selenoidFirefoxTest(){
        Selenide.open("https://vk.com");
    }

    @Test
    @BrowserType(browser = BrowserType.Browser.CHROME, isRemote = true)
    public void selenoidDownloadFileTest() throws IOException {
        Selenide.open("https://www.pdf995.com/samples/");
        File pdf = $x("//td[@data-sort='pdf.pdf']/a").download();
        PDF pdfReader = new PDF(pdf);
        Assertions.assertEquals("Software 995", pdfReader.author);
    }

    @Test
    @BrowserType(browser = BrowserType.Browser.CHROME, isRemote = true)
    public void selenoidUploadFileTest(){
        Selenide.open("http://85.192.34.140:8081/");
        $(By.xpath("//div[@class='card-body']//h5[text()='Elements']")).click();
        $(By.xpath("//span[text()='Upload and Download']")).click();

        String keysToJpeg = System.getProperty("user.dir") + "/src/test/resources/threadqa.jpeg";
        SelenideElement uploadBtn = $(By.id("uploadFile"));

        uploadBtn.sendKeys(keysToJpeg);

        $(By.id("uploadedFilePath")).should(Condition.partialText("threadqa.jpeg"));
    }
}
