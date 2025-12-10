package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SuccessAuthorisationPage {
    private WebDriver driver;

    /**
     * Локатор для отображения ответа авторизации
     */
    @FindBy(xpath = "//p[@class='ng-scope'][1]")
    private WebElement authResponse;

    /**
     * Локатор для кнопки "Logout"
     */
    @FindBy(xpath = "//a[contains(text(), 'Logout')]")
    private WebElement buttonLogout;

    /**
     * Конструктор страницы успешной авторизации
     */
    public SuccessAuthorisationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /**
     * Получает текст ответа авторизации
     */
    @Step("Проверяем текст ответа авторизации")
    public String getAuthResponse() {
        return authResponse.getText();
    }

    /**
     * Нажимает кнопку "Logout" и возвращает объект страницы авторизации
     * @return новый экземпляр страницы авторизации
     */
    @Step("Нажимаем кнопку Logout и возвращаемся на страницу авторизации")
    public AuthorisationPage clickLogout() {
        buttonLogout.click();
        return new AuthorisationPage(driver);
    }
}
