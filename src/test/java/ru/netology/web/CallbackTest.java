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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    //
    // task 1
    //

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

    //
    // task 2
    //

    @Test
    void emptyUser() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.tagName("button")).click();
        WebElement element = form.findElement(By.cssSelector("[data-test-id=name]"));
        // input input_type_text input_view_default input_size_m input_width_available input_has-label input_invalid input_theme_alfa-on-white
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

    @Test
    void emptyPhone() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.tagName("button")).click();
        WebElement element = form.findElement(By.cssSelector("[data-test-id=phone]"));
        // input input_type_tel input_view_default input_size_m input_width_available input_has-label input_invalid input_theme_alfa-on-white
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

    @Test
    void invalidUser() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivanov");
        form.findElement(By.tagName("button")).click();
        WebElement element = form.findElement(By.cssSelector("[data-test-id=name]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

    @Test
    void invalidPhone() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7927");
        form.findElement(By.tagName("button")).click();
        WebElement element = form.findElement(By.cssSelector("[data-test-id=phone]"));
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

    @Test
    void checkBoxNotPressed() {
        driver.get("http://localhost:9999");
        WebElement form = driver.findElement(By.xpath("//form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.tagName("button")).click();
        WebElement element = form.findElement(By.cssSelector("[data-test-id=agreement]"));
        // checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid
        boolean b = element.getAttribute("class").contains("input_invalid");
        assertTrue(b);
    }

}
