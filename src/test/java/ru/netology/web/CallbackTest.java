package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackTest {

    private WebDriver driver;
    private static String driverPath= "";

    static {
        String OS = System.getProperty("os.name").toLowerCase();
        boolean IS_WINDOWS = OS.indexOf("win") >= 0;
        boolean IS_LINUX = OS.indexOf("nux") >= 0;
        if (IS_WINDOWS)
            driverPath = "driver/windows/chromedriver.exe";
        if (IS_LINUX)
            driverPath = "driver/linux/chromedriver";
    }

//    @Test
//    void checkOsDriverPatch() { System.out.println(driverPath); }

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", driverPath);
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestV1() {
        driver.get("http://localhost:9999");
        List<WebElement> elements = driver.findElements(By.className("input__control"));
        elements.get(0).sendKeys("Василий");
        elements.get(1).sendKeys("+79270000000");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertTrue(text.contains("Ваша заявка успешно отправлена!"));
    }

    @Test
    void shouldTestV2() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertTrue(text.contains("Ваша заявка успешно отправлена!"));
    }

}
