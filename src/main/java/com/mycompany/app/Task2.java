package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;

public class Task2 {

    public static String getIPAddress() {
        WebDriver webDriver = null;
        String ipAddress = null;

        try {
            ChromeOptions options = configureChromeOptions();

            setupChromeDriver();

            webDriver = new ChromeDriver(options);

            webDriver.get("https://api.ipify.org/?format=json");
            System.out.println("Загружена страница: https://api.ipify.org/?format=json");

            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String json_str = elem.getText();
            System.out.println("JSON ответ: " + json_str);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json_str);
            ipAddress = (String) obj.get("ip");

        } catch (Exception e) {
            System.out.println("Ошибка в Task2: " + e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
        return ipAddress;
    }

    private static ChromeOptions configureChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        String chromePath = "chrome-win64/chrome.exe";
        File chromeFile = new File(chromePath);

        if (chromeFile.exists()) {
            options.setBinary(chromePath);
        } else {
            String[] standardPaths = {
                    "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe",
                    "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe",
                    System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe"
            };

            for (String path : standardPaths) {
                File standardChrome = new File(path);
                if (standardChrome.exists()) {
                    options.setBinary(path);
                    break;
                }
            }
        }
        return options;
    }

    private static void setupChromeDriver() {
        String driverPath = "chromedriver-win64/chromedriver.exe";
        File driverFile = new File(driverPath);

        if (driverFile.exists()) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }
    }
}