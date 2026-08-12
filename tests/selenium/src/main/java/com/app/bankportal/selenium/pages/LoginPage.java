package com.app.bankportal.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickLoginMode() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("show-login"))).click();
    }

    public void clickSignupMode() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("show-signup"))).click();
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement field = driver.findElement(By.id("password"));
        field.clear();
        field.sendKeys(password);
    }

    public void submit() {
        driver.findElement(By.id("auth-submit")).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='login-error']"))).getText();
    }

    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid='login-success']"))).getText();
    }

    public void signup(String username, String password) {
        clickSignupMode();
        enterUsername(username);
        enterPassword(password);
        submit();
    }

    public void login(String username, String password) {
        clickLoginMode();
        enterUsername(username);
        enterPassword(password);
        submit();
    }
}