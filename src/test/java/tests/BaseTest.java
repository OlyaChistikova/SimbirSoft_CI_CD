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
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class BaseTest {
    private RemoteWebDriver driver;
    protected boolean useIncognito = false;
    protected SoftAssert softAssert;

    @BeforeMethod(description = "Настройка браузера перед запуском тестов")
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            /* How to add test badge */
            put("name", "Test badge...");

            /* How to set session timeout */
            put("sessionTimeout", "15m");

            /* How to set timezone */
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            /* How to add "trash" button */
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            /* How to enable video recording */
            put("enableVideo", true);
        }});
//        String remoteUrl = System.getenv("SELENIUM_URL");
        String remoteUrl = ("http://172.21.48.1:8080");
        driver = new RemoteWebDriver(new URL(remoteUrl), options);
        driver.manage().window().maximize();
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