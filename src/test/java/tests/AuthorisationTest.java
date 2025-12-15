package tests;

import helpers.PropertyProvider;
import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.AuthorisationPage;
import pages.SuccessAuthorisationPage;

public class AuthorisationTest extends BaseTest{
    private WebDriver driver;
    private AuthorisationPage authorisationPage;

    @BeforeMethod(description = "Открываем страницу авторизации")
    public void setUrl(){
        useIncognito = true;
        driver = getDriver();
        authorisationPage = new AuthorisationPage(driver);
    }

    @Test(description = "Проверка полей ввода")
    @Epic("Авторизация пользователя")
    @Feature("Поля ввода")
    @Story("Проверка отображения и доступности полей")
    @Severity(SeverityLevel.CRITICAL)
    public void checkInputFieldsTest(){
        Assert.assertTrue(authorisationPage.checkDisplayUsername());
        Assert.assertTrue(authorisationPage.checkDisplayPassword());
        Assert.assertTrue(authorisationPage.checkDisplayUsernameDescription());
        Assert.assertTrue(authorisationPage.checkDisabledLoginButton());
    }

    @DataProvider(name = "validDataAuth")
    public Object[][] validDataAuth(){
        return new Object[][]{
                {"angular", "password", "abc"},
                {"angular", "password", "description"}
        };
    }

    @Test(description = "Проверка успешной авторизации", dataProvider = "validDataAuth")
    @Epic("Авторизация пользователя")
    @Feature("Вход в систему")
    @Story("Успешный вход с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    public void checkSuccessfulAuthorizationTest(String username, String password, String description){
        authorisationPage.setAuthorisationFields(username, password, description);
        SuccessAuthorisationPage successAuthorisationPage = authorisationPage.successClickButtonLogin();
        Assert.assertEquals(successAuthorisationPage.getAuthResponse(), "You're logged in!!");
        successAuthorisationPage.clickLogout();
    }

    @DataProvider(name = "invalidDataAuth")
    public Object[][] invalidDataAuth(){
        return new Object[][]{
                {"abc", "password", "abc"},            // username невалидный
                {"angular", "psw", "abc"}              // password невалидный
        };
    }

    @Test(description = "Проверка авторизации с невалидными данными", dataProvider = "invalidDataAuth")
    @Epic("Авторизация пользователя")
    @Feature("Обработка ошибок")
    @Story("Авторизация с недопустимыми данными")
    @Severity(SeverityLevel.MINOR)
    public void checkUnSuccessAuthorizationInvalidDataTest(String username, String password, String description){
        authorisationPage.setAuthorisationFields(username, password, description).clickButtonLogin();
        Assert.assertEquals(authorisationPage.getErrorMessage(), "Username or password is incorrect");
        Assert.assertTrue(authorisationPage.clearInputFields().checkDisabledLoginButton());
    }

    @Test(description = "Проверка успешного разлогирования")
    @Epic("Авторизация пользователя")
    @Feature("Выход из системы")
    @Story("Успешный выход")
    @Severity(SeverityLevel.NORMAL)
    public void checkSuccessUnLoggingTest(){
        authorisationPage.setAuthorisationFields(PropertyProvider.getProperty("usernameAuthorisation"), PropertyProvider.getProperty("passwordAuthorisation"), PropertyProvider.getProperty("usernameAuthorisation"));
        SuccessAuthorisationPage successAuthorisationPage = authorisationPage.successClickButtonLogin();
        AuthorisationPage authorisationPageAfter = successAuthorisationPage.clickLogout();
        Assert.assertTrue(authorisationPageAfter.checkDisplayUsername());
        Assert.assertTrue(authorisationPageAfter.checkDisplayPassword());
        Assert.assertTrue(authorisationPageAfter.checkDisplayUsernameDescription());
    }

    @DataProvider(name = "emptyDataAuth")
    public Object[][] emptyDataAuth() {
        return new Object[][]{
                {"", "password", "description"},
                {"user", "", "description"},
                {"user", "password", ""},
                {"", "", "description"},
                {"user", "", ""},
                {"", "password", ""},
                {"", "", ""},
        };
    }

    @Test(description = "Тест авторизации с пустыми данными ввода", dataProvider = "emptyDataAuth")
    @Epic("Авторизация пользователя")
    @Feature("Обработка ошибок")
    @Story("Авторизация с пустыми данными ввода")
    @Severity(SeverityLevel.CRITICAL)
    public void checkUnSuccessAuthorizationEmptyDataTest(String username, String password, String description){
        authorisationPage.setAuthorisationFields(username, password, description);
        Assert.assertTrue(authorisationPage.checkDisabledLoginButton());
        authorisationPage.clearInputFields();
    }
}
