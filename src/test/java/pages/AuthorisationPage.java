package pages;

import helpers.PropertyProvider;
import helpers.Waiters;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthorisationPage {
    private WebDriver driver;
    private static final String URL = PropertyProvider.getProperty("home.url");

    /**
     * Локатор поля ввода логина (username)
     */
    @FindBy(xpath = "//input[@id='username']")
    private WebElement usernameField;

    /**
     * Локатор поля ввода пароля (password)
     */
    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;

    /**
     * Локатор поля ввода описания пользователя (usernameDescription)
     */
    @FindBy(xpath = "//input[@ng-model='model[options.key]']")
    private WebElement usernameDescriptionField;

    /**
     * Локатор кнопки входа (Login)
     */
    @FindBy(xpath = "//button[@class='btn btn-danger']")
    public WebElement loginButton;

    /**
     * Локатор сообщения об ошибке авторизации
     */
    @FindBy(xpath = "//div[@ng-if='Auth.error']")
    private WebElement errorMessage;

    /**
     * Конструктор страницы авторизации
     *
     * @param driver WebDriver
     */
    public AuthorisationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.driver.get(URL);
    }

    /**
     * Проверка отображения поля ввода логина
     *
     * @return true, если поле отображается
     */
    @Step("Проверка что поле userName отображается")
    public boolean checkDisplayUsername() {
        Waiters.waitTimeForVisibilityOfElement(driver, usernameField);
        return usernameField.isDisplayed();
    }

    /**
     * Проверка отображения поля ввода пароля
     *
     * @return true, если поле отображается
     */
    @Step("Проверка что поле password отображается")
    public boolean checkDisplayPassword() {
        Waiters.waitTimeForVisibilityOfElement(driver, passwordField);
        return passwordField.isDisplayed();
    }

    /**
     * Проверка отображения поля описания пользователя
     *
     * @return true, если поле отображается
     */
    @Step("Проверка что поле userName Description отображается")
    public boolean checkDisplayUsernameDescription() {
        Waiters.waitTimeForVisibilityOfElement(driver, usernameDescriptionField);
        return usernameDescriptionField.isDisplayed();
    }

    /**
     * Проверка, что кнопка входа неактивна (disabled)
     *
     * @return true, если кнопка disabled
     */
    @Step("Проверяем, что кнопка входа неактивна")
    public boolean checkDisabledLoginButton() {
        Waiters.waitTimeForVisibilityOfElement(driver, loginButton);
        String disabledAttribute = loginButton.getAttribute("disabled");
        return disabledAttribute != null && disabledAttribute.equals("true");
    }

    /**
     * Установка значений в поля ввода
     *
     * @param username    логин
     * @param password    пароль
     * @param description описание пользователя
     * @return текущий объект страницы для цепочки вызовов
     */
    @Step("Задаем значения для авторизации")
    public AuthorisationPage setAuthorisationFields(String username, String password, String description) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        usernameDescriptionField.sendKeys(description);
        return this;
    }

    /**
     * Нажатие кнопки входа и переход на страницу успешной авторизации
     *
     * @return объект страницы SuccessAuthorisationPage
     */
    @Step("Нажимаем кнопку входа и переходим на страницу успешной авторизации")
    public SuccessAuthorisationPage successClickButtonLogin() {
        Waiters.waitTimeForClickableElement(driver, loginButton);
        loginButton.click();
        return new SuccessAuthorisationPage(driver);
    }

    /**
     * Просто клик по кнопке входа без перехода
     */
    @Step("Нажимаем кнопку входа без перехода на страницу авторизации")
    public void clickButtonLogin() {
        loginButton.click();
    }

    /**
     * Получение текста сообщения об ошибке авторизации
     *
     * @return текст сообщения
     */
    @Step("Получаем текст сообщения об ошибке авторизации")
    public String getErrorMessage() {
        Waiters.waitTimeForVisibilityOfElement(driver, errorMessage);
        return errorMessage.getText();
    }

    /**
     * Очистка всех полей ввода
     */
    @Step("Очищаем поля ввода")
    public AuthorisationPage clearInputFields() {
        usernameField.clear();
        passwordField.clear();
        usernameDescriptionField.clear();
        return this;
    }
}