package org.example.ui.selenoid;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@ExtendWith(BrowserTypeAnnotationProcessing.class)
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BrowserType {
    Browser browser();
    boolean isRemote() default true;
    enum Browser {
        FIREFOX, CHROME
    }
}
