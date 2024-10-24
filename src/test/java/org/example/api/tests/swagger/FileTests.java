package org.example.api.tests.swagger;

import io.qameta.allure.Attachment;
import org.example.api.assertions.Conditions;
import org.example.api.services.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.File;

public class FileTests extends BaseApiTest {
    private final FileService fileService = new FileService();

    @Attachment(value = "downloaded", type = "image/png")
    private byte[] attachFile(byte[] bytes) {
        return bytes;
    }

    @Test
    public void positiveDownloadTest() {
        byte[] file = fileService.downloadBaseImage().asResponse().asByteArray();
        attachFile(file);
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        Assertions.assertEquals(expectedFile.length(), file.length);
    }

    @Test
    public void positiveUploadTest() {
        File expectedFile = new File("src/test/resources/threadqa.jpeg");
        fileService.uploadFile(expectedFile)
                .should(Conditions.hasStatusCode(200))
                .should(Conditions.hasMessage("file uploaded to server"));

        byte[] actualFile = fileService.downloadLastFile().asResponse().asByteArray();
        Assertions.assertTrue(actualFile.length != 0);
        Assertions.assertEquals(expectedFile.length(), actualFile.length);
    }
}