package com.app.bankportal.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("logout-btn"))).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCreateAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("create-account-btn"))).click();
    }

    public void clickCreateCreditCard() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("create-card-btn"))).click();
    }

    public void clickRefresh() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("refresh-btn"))).click();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout-btn"))).click();
    }

    public String getBalancePill() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".balance-pill"))).getText();
    }

    public String getAccountsList() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("accounts-list"))).getText();
    }

    public String getCreditList() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("credit-list"))).getText();
    }
    
}