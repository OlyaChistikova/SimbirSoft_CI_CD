package tests;

import helpers.JavaScriptExecutorHelper;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

@Getter
public class BaseTest {
    private RemoteWebDriver driver;
    protected boolean useIncognito = false;
    protected SoftAssert softAssert;

    @BeforeMethod(description = "Настройка браузера перед запуском тестов")
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub/"), options);        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        softAssert = new SoftAssert();
    }

    @Step("Делаем скриншот в случае провала теста")
    public void takeScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            JavaScriptExecutorHelper.takeScreenshot(driver);
        }
    }

    @AfterMethod(description = "Закрываем браузер")
    public void tearDown(ITestResult result){
        takeScreenshotOnFailure(result);
        if (driver != null){
            driver.quit();
        }
    }
}