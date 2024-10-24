package org.example.ui.stepik_auth_api;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$x;

public class StepikUIAuthTests {
    private String login = "gobitil216@estudys.com";
    private String pass = "5UPp_Uc5E*_&L45";
    @Test
    public void uiAuthTest(){
        Selenide.open("https://stepik.org/catalog");
        $x("//a[contains(@class,'navbar__auth_login')]").click();
        $x("//input[@name='login']").sendKeys(login);
        $x("//input[@name='password']").sendKeys(pass);
        $x("//button[@type='submit']").click();
        $x("//img[@class='navbar__profile-img']").should(Condition.visible).click();
        $x("//li[@data-qa='menu-item-profile']").should(Condition.visible).click();
        $x("//h1").should(Condition.text("Skoz Nelson"));
    }
}
