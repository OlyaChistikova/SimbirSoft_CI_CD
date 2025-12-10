package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JavaScriptExecutorHelper {
    private WebDriver driver;

    /**
     * Конструктор.
     * @param driver WebDriver, с которым работает скрипт.
     */
    public JavaScriptExecutorHelper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Функция создания скриншота всей страницы
     * @param driver = объект вебдрайвера
     * */
    @Step("Делаем скриншот страницы")
    public static void takeScreenshot(WebDriver driver) {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driver);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot.getImage(), "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(imageBytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
