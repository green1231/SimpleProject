package org.example.ui.selenoid;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.io.Files;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AllureLogsAttachment {
    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource(){
        return WebDriverRunner.getWebDriver().getPageSource().getBytes(StandardCharsets.UTF_8);
    }
    @Attachment(value = "Page screen", type = "image/png")
    public static byte[] pageScreen(){
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }
    @Attachment(value = "Browser logs", type = "text/plain")
    public static String getLogs(){
        String browser = ((RemoteWebDriver)WebDriverRunner.getWebDriver()).getCapabilities().getBrowserName();
        if(browser.equals("chrome")){
            return String.join("\n", Selenide.getWebDriverLogs(LogType.BROWSER));
        }
        return null;
    }
    @Attachment(value = "Video HTML", type = "text/html", fileExtension = ".html")
    public static String getVideoUrl(String sessionId){
        String url = "http://localhost:4444/video/" + sessionId + ".mp4";
        return "<html><body><a href=\"" + url + "\">" + url + "</a><video width='100%' height='400px' controls autoplay><source src='"
                + url + "' type='video/mp4'></video></body></html>";
    }

    @Attachment(value = "Видео", type = "video/mp4", fileExtension = ".mp4")
    public static byte[] attachVideo(String sessionId) {
        Selenide.closeWebDriver();
        String url = "http://localhost:4444/video/" + sessionId + ".mp4";
        try {
            File mp4 = new File(System.getProperty("java.io.tmpdir") + "temp.mp4");
            mp4.deleteOnExit();
            Thread.sleep(3000);
            FileUtils.copyURLToFile(new URL(url), mp4);
            return Files.toByteArray(mp4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
