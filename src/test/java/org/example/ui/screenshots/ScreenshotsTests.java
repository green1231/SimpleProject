package org.example.ui.screenshots;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class ScreenshotsTests {
    private String testName;
    private static File outputDir;

    @BeforeEach
    public void initTestName(TestInfo info) {
        testName = info.getTestMethod().get().getName();
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWindow();
        Configuration.browserCapabilities = new SelenideConfig().browserCapabilities();
    }

    @BeforeAll
    public static void initFolder() {
        outputDir = new File("build/screenshots");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
    }

    @Test
    public void web1080pTest() {
        Configuration.browserSize = "1920x1080";
        Selenide.open("https://threadqa.ru");
        By ignoredPictureBlock = By.xpath("//div[@class='left_part']");
        assertFullScreen(ignoredPictureBlock);
    }

    @Test
    public void mobileIphoneXrTest() {
        // System.setProperty("chromeoptions.mobileEmulation", "deviceName=iPhone XR");
//        Map<String, String> mobileEmulation = new HashMap<>();
//        mobileEmulation.put("deviceName", "iPhone XR");
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
//        Configuration.browserCapabilities = chromeOptions;

        Configuration.browserSize = "414x896";
        Selenide.open("https://threadqa.ru");
        assertFullScreen();
    }

    @SneakyThrows
    private void assertFullScreen(By ignoredElement) {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(3000))
                .addIgnoredElement(ignoredElement)
                .takeScreenshot(WebDriverRunner.getWebDriver());
        File actualScreen = new File(outputDir.getAbsolutePath() + "/" + testName + ".png");
        ImageIO.write(screenshot.getImage(), "png", actualScreen);

        File expectedScreen = new File(String.format("src/test/resources/references/%s.png", testName));
        if (!expectedScreen.exists()) {
            throw new RuntimeException("No reference image, download it from build/screenshots");
        }
        assertImages(actualScreen, expectedScreen);
    }

    @SneakyThrows
    private void assertFullScreen() {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(3000))
                .takeScreenshot(WebDriverRunner.getWebDriver());
        File actualScreen = new File(outputDir.getAbsolutePath() + "/" + testName + ".png");
        ImageIO.write(screenshot.getImage(), "png", actualScreen);

        File expectedScreen = new File(String.format("src/test/resources/references/%s.png", testName));
        if (!expectedScreen.exists()) {
            throw new RuntimeException("No reference image, download it from build/screenshots");
        }
        assertImages(actualScreen, expectedScreen);
    }

    @SneakyThrows
    private void assertImages(File actual, File expected) {
        ImageDiff differ = new ImageDiffer()
                .makeDiff(ImageIO.read(actual), ImageIO.read(expected))
                .withDiffSizeTrigger(10);
        if (differ.hasDiff()) {
            BufferedImage diffImage = differ.getMarkedImage();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(diffImage, "png", bos);
            byte[] image = bos.toByteArray();
            Allure.getLifecycle().addAttachment("diff", "image/png", "png", image);
        }
        Assertions.assertFalse(differ.hasDiff());
    }
}
