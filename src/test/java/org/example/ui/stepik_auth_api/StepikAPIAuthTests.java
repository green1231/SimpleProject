package org.example.ui.stepik_auth_api;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import java.util.*;

import static com.codeborne.selenide.Selenide.$x;
import static io.restassured.RestAssured.given;

public class StepikAPIAuthTests {
    private StepikAuth testUser;
    @BeforeEach
    public void init(){
        Selenide.open("https://stepik.org/catalog");
        testUser = new StepikAuth("gobitil216@estudys.com","5UPp_Uc5E*_&L45");
    }

    @Test
    public void checkUserName(){
        authApi(testUser);
        $x("//img[@class='navbar__profile-img']").should(Condition.visible).click();
        $x("//li[@data-qa='menu-item-profile']").should(Condition.visible).click();
        $x("//h1").should(Condition.text("Skoz Nelson"));
    }

    private void authApi(StepikAuth user){
        Set<Cookie> cookiesBrowser = WebDriverRunner.getWebDriver().manage().getCookies();
        Map<String,String> authHeaders  = new HashMap<>();
        authHeaders.put("referer","https://stepik.org/catalog?auth=login");
        authHeaders.put("origin", "https://stepik.org");
        authHeaders.put("authority", "stepik.org");

        List<io.restassured.http.Cookie> raCookies = new ArrayList<>();
        for (Cookie cookie : cookiesBrowser) {
            io.restassured.http.Cookie temp = new io.restassured.http.Cookie
                    .Builder(cookie.getName(), cookie.getValue())
                    .setDomain(cookie.getDomain())
                    .setPath("/").build();
            raCookies.add(temp);
            if(cookie.getName().equals("csrftoken")){
                authHeaders.put("X-csrftoken", cookie.getValue());
            }
        }

        Map<String, String> authCookies = given().contentType(ContentType.JSON)
                .body(user)
                .headers(authHeaders)
                .cookies(new Cookies(raCookies))
                .post("https://stepik.org/api/users/login")
                .then().log().all().extract().cookies();

        Cookie csrf = new Cookie("csrftoken", authCookies.get("csrftoken"), "stepik.org", "/", null);
        Cookie sessionId = new Cookie("sessionid", authCookies.get("sessionid"), "stepik.org", "/", null);

        WebDriverRunner.getWebDriver().manage().addCookie(csrf);
        WebDriverRunner.getWebDriver().manage().addCookie(sessionId);
        Selenide.refresh();
    }
}
