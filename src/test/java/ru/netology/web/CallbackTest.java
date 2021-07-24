package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CallbackTest {

    static ChromeOptions options = new ChromeOptions();
    WebDriver driver;
    WebElement form, element;

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

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", driverPath);
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void shouldTestV1() {
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
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        assertTrue(text.contains("Ваша заявка успешно отправлена!"));
    }

    @Test
    void emptyUser() {
        form = driver.findElement(By.xpath("//form"));
        form.findElement(By.tagName("button")).click();
        element = form.findElement(By.cssSelector("[data-test-id=name]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
        element = form.findElement(By.cssSelector("[data-test-id=name] span.input__sub"));
        assertTrue(element.getText().equals("Поле обязательно для заполнения"));
    }

    @Test
    void emptyPhone() {
        form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.tagName("button")).click();
        element = form.findElement(By.cssSelector("[data-test-id=phone]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
        element = form.findElement(By.cssSelector("[data-test-id=phone] span.input__sub"));
        assertTrue(element.getText().equals("Поле обязательно для заполнения"));
    }

    @Test
    void invalidUser() {
        form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov");
        form.findElement(By.tagName("button")).click();
        element = form.findElement(By.cssSelector("[data-test-id=name]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
        element = form.findElement(By.cssSelector("[data-test-id=name] span.input__sub"));
        assertTrue(element.getText().equals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void invalidPhone() {
        form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7927");
        form.findElement(By.tagName("button")).click();
        element = form.findElement(By.cssSelector("[data-test-id=phone]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
        element = form.findElement(By.cssSelector("[data-test-id=phone] span.input__sub"));
        assertTrue(element.getText().equals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void checkBoxNotPressed() {
        form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.tagName("button")).click();
        element = form.findElement(By.cssSelector("[data-test-id=agreement]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

}
