package com.yourcompany.selenium.tests;

import com.yourcompany.selenium.base.BaseTest;
import com.yourcompany.selenium.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest extends BaseTest {

    @Test
    void showsErrorMessage_whenCredentialsInvalid() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);

        loginPage.login("nouser@test.com", "whatever");

        String error = loginPage.getErrorMessage();
        assertTrue(error.toLowerCase().contains("not found") ||
                   error.toLowerCase().contains("invalid"));
    }

    @Test
    void redirectsToDashboard_whenCredentialsValid() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open(BASE_URL);

        loginPage.login("test@test.com", "correctpass");

        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }
}
