package com.mycompany.app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;

public class App {

    public static void main(String[] args) {
        System.out.println("=== Задание №1: Генерация пароля ===\n");
        generatePassword();

        System.out.println("\n=== Задание №2: Получение IP адреса ===\n");
        String ip = Task2.getIPAddress();
        System.out.println("IP адрес: " + ip);

        System.out.println("\n=== Задание №3: Прогноз погоды ===\n");
        Task3.getWeatherForecast();
    }

    public static void generatePassword() {
        ChromeOptions options = new ChromeOptions();

        String chromePath = "chrome-win64/chrome.exe";
        File chromeFile = new File(chromePath);

        if (chromeFile.exists()) {
            options.setBinary(chromePath);
            System.out.println("Chrome найден по пути: " + chromePath);
        } else {
            System.err.println("Chrome не найден по пути: " + chromePath);
            String[] standardPaths = {
                    "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                    "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
                    System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe"
            };
        }

        String driverPath = "chromedriver-win64/chromedriver.exe";
        File driverFile = new File(driverPath);

        if (driverFile.exists()) {
            System.setProperty("webdriver.chrome.driver", driverPath);
            System.out.println("Chromedriver найден по пути: " + driverPath);
        } else {
            System.err.println("Chromedriver не найден по пути: " + driverPath);
            return;
        }

        WebDriver webDriver = null;

        try {
            webDriver = new ChromeDriver(options);
            System.out.println("Браузер успешно запущен!");

            webDriver.get("https://www.calculator.net/password-generator.html");
            System.out.println("Страница загружена: https://www.calculator.net/password-generator.html");

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(45));
            By passwordLocator = By.cssSelector("#resultid div.verybigtext b");
            wait.until(ExpectedConditions.presenceOfElementLocated(passwordLocator));
            WebElement passwordEl = webDriver.findElement(passwordLocator);
            System.out.println("Сгенерированный пароль: " + passwordEl.getText());

        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }
}