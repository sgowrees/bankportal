package com.app.bankportal.selenium.tests;

import com.app.bankportal.selenium.config.WebDriverConfig;
import com.app.bankportal.selenium.pages.DashboardPage;
import com.app.bankportal.selenium.pages.LoginPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    private static WebDriver driver;
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;

    private static final String FRONTEND_URL = "http://localhost:3000";
    private static final String USERNAME = "seleniumuser1";
    private static final String PASSWORD = "password123";

    @BeforeAll
    static void setup() {
        driver = WebDriverConfig.createDriver();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        driver.get(FRONTEND_URL);
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void signup_success() {
        loginPage.signup(USERNAME, PASSWORD);
        String success = loginPage.getSuccessMessage();
        assertTrue(success.contains("Account created"),
            "Expected signup success message, got: " + success);
    }

    @Test
    @Order(2)
    void login_success() {
        loginPage.login(USERNAME, PASSWORD);
        assertTrue(dashboardPage.isLoaded(),
            "Dashboard should be visible after successful login");
    }

    @Test
    @Order(3)
    void logout_success() {
        dashboardPage.clickLogout();
        assertFalse(dashboardPage.isLoaded(),
            "Dashboard should not be visible after logout");
    }

    @Test
    @Order(4)
    void login_invalidUsername_showsError() {
        driver.get(FRONTEND_URL);
        loginPage.login("nonexistentuser999", PASSWORD);
        String error = loginPage.getErrorMessage();
        assertFalse(error.isEmpty(),
            "Error message should appear for invalid username");
    }

    @Test
    @Order(5)
    void login_wrongPassword_showsError() {
        driver.get(FRONTEND_URL);
        loginPage.login(USERNAME, "wrongpassword");
        String error = loginPage.getErrorMessage();
        assertFalse(error.isEmpty(),
            "Error message should appear for wrong password");
    }

    @Test
    @Order(6)
    void signup_duplicateUsername_showsError() {
        driver.get(FRONTEND_URL);
        loginPage.signup(USERNAME, PASSWORD);
        String error = loginPage.getErrorMessage();
        assertFalse(error.isEmpty(),
            "Error message should appear for duplicate username");
    }
}