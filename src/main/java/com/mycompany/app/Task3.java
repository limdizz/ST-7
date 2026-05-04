package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Task3 {

    public static void getWeatherForecast() {
        WebDriver webDriver = null;

        try {
            ChromeOptions options = configureChromeOptions();
            setupChromeDriver();
            webDriver = new ChromeDriver(options);

            String url = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";

            webDriver.get(url);
            System.out.println("Загружена страница с прогнозом погоды");

            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String json_str = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json_str);

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            String line = createLine(80);
            System.out.println("\n" + line);
            System.out.printf("%-5s %-25s %-15s %-15s\n", "№", "Дата/время", "Температура (°C)", "Осадки (мм)");
            System.out.println(createDashLine(80));

            for (int i = 0; i < times.size(); i++) {
                String time = (String) times.get(i);
                Number temp = (Number) temperatures.get(i);
                Number rain = (Number) rains.get(i);

                System.out.printf("%-5d %-25s %-15.1f %-15.2f\n",
                        (i + 1), time, temp.doubleValue(), rain.doubleValue());
            }
            System.out.println(line + "\n");

            saveForecastToFile(times, temperatures, rains);

        } catch (Exception e) {
            System.out.println("Ошибка в Task3: " + e);
            e.printStackTrace();
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

    private static ChromeOptions configureChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        String chromePath = "chrome-win64/chrome.exe";
        File chromeFile = new File(chromePath);

        if (chromeFile.exists()) {
            options.setBinary(chromePath);
            System.out.println("Chrome найден по пути: " + chromePath);
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
                    System.out.println("Chrome найден по стандартному пути: " + path);
                    break;
                }
            }
        }

        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        return options;
    }

    private static void setupChromeDriver() {
        String driverPath = "chromedriver-win64/chromedriver.exe";
        File driverFile = new File(driverPath);

        if (driverFile.exists()) {
            System.setProperty("webdriver.chrome.driver", driverPath);
            System.out.println("Chromedriver найден по пути: " + driverPath);
        } else {
            System.err.println("Chromedriver не найден по пути: " + driverPath);
        }
    }

    private static void saveForecastToFile(JSONArray times, JSONArray temperatures, JSONArray rains) {
        try {
            File resultDir = new File("result");
            if (!resultDir.exists()) {
                resultDir.mkdir();
            }

            PrintWriter writer = new PrintWriter(new FileWriter("result/forecast.txt"));

            String line = createLine(80);
            writer.println(line);
            writer.printf("%-5s %-25s %-15s %-15s\n", "№", "Дата/время", "Температура (°C)", "Осадки (мм)");
            writer.println(createDashLine(80));

            for (int i = 0; i < times.size(); i++) {
                String time = (String) times.get(i);
                Number temp = (Number) temperatures.get(i);
                Number rain = (Number) rains.get(i);

                writer.printf("%-5d %-25s %-15.1f %-15.2f\n",
                        (i + 1), time, temp.doubleValue(), rain.doubleValue());
            }
            writer.println(line);

            writer.close();
            System.out.println("Прогноз погоды сохранен в файл: result/forecast.txt");

        } catch (Exception e) {
            System.out.println("Ошибка при сохранении файла: " + e);
        }
    }

    private static String createLine(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("=");
        }
        return sb.toString();
    }

    private static String createDashLine(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append("-");
        }
        return sb.toString();
    }
}