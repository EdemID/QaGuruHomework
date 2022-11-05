import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import helpers.CustomDate;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.Test;
import pages.AutomationPracticeFormPage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static helpers.CustomDate.calendarToString;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

public class TenthHomeworkTest extends BaseTest {

    private static final String REPOSITORY = "EdemID/qaGuruHomework";

    @Test
    void addListener() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open("https://github.com/");
        $(".header-search-input").shouldHave(Condition.visible).sendKeys(REPOSITORY);
        $(".header-search-input").submit();
        $(linkText(REPOSITORY)).shouldHave(Condition.visible).click();

        $("#issues-tab").should(Condition.visible);
        $(withText("Gruzer")).should(Condition.exist);
    }

    @Test
    void steps() {
        step("Открыть github.com", new Allure.ThrowableContextRunnableVoid<>() {
            @Override
            public void run(Allure.StepContext context) throws Throwable {
                open("https://github.com/");
                System.out.println(context);
            }
        });
        step("Найти " + REPOSITORY, () -> {
            $(".header-search-input").shouldHave(Condition.visible).sendKeys(REPOSITORY);
            $(".header-search-input").submit();
        });
        step("Выбрать проект " + REPOSITORY, () -> $(linkText(REPOSITORY)).shouldHave(Condition.visible).click());
        step("Проверить на видимость вкладку Issues", new Allure.ThrowableRunnableVoid() {
            @Override
            public void run() {
                $("#issues-tab").should(Condition.visible);
            }
        });
        step("Проверить на существование пользователя Gruzer", () -> $(withText("Gruzer")).should(Condition.exist));
    }

    @Test
    void annotatedStep() {
        Faker faker = new Faker();

        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        var email = faker.internet().emailAddress();
        var mobile = faker.phoneNumber().subscriberNumber(10);
        var currentAddress = faker.address().fullAddress();
        var birthday = CustomDate.toCalendar(faker.date().birthday(10, 24));

        open(props.automationPracticeFormURL());

        var automationPracticeFormPage = new AutomationPracticeFormPage();

        automationPracticeFormPage
                .checkTextInField("Student Registration Form")
                .enterFirstNameAndLastName(firstName, lastName)
                .enterEmail(email)
                .enterMobile(mobile)
                .checkboxGender("Male")
                .checkboxHobbies("Reading", "Sports")
                .uploadPicture("img/picture.bmp")
                .setDateOfBirthday(
                        String.valueOf(birthday.get(Calendar.DAY_OF_MONTH)),
                        birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("en")),
                        String.valueOf(birthday.get(Calendar.YEAR)))
                .setSubjects("Maths", "English")
                .setCurrentAddress(currentAddress)
                .setStateAndCity("NCR", "Gurgaon")
                .clickSubmit();

        Map<String, String> expectedValues = new HashMap<>();
        expectedValues.put("Student Name", firstName + " " + lastName);
        expectedValues.put("Student Email", email);
        expectedValues.put("Mobile", mobile);
        expectedValues.put("Date of Birth", calendarToString(birthday));
        automationPracticeFormPage.checkForm(expectedValues);
    }
}
