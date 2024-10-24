package org.example.ui.selenoid;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureLogsExtension implements AfterTestExecutionCallback {
    @Override
    public void afterTestExecution(ExtensionContext context) {
        context.getExecutionException().ifPresent(x->{
            AllureLogsAttachment.pageSource();
            AllureLogsAttachment.pageScreen();
            AllureLogsAttachment.getLogs();
            AllureLogsAttachment.getVideoUrl(Selenide.sessionId().toString());
            AllureLogsAttachment.attachVideo(Selenide.sessionId().toString());
        });
    }
}
