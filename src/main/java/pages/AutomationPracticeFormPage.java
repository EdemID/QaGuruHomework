package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.CustomAssertions;
import helpers.Util;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AutomationPracticeFormPage {

    private final SelenideElement uploadPicture = $("#uploadPicture");
    private final SelenideElement subjects = $("#subjectsWrapper");
    private final SelenideElement stateAndCity = $("#stateCity-wrapper");

    public AutomationPracticeFormPage checkTextInField(String expectedText) {

        CustomAssertions.assertShouldHave($(byText("Student Registration Form")), text(expectedText));
        return this;
    }

    public AutomationPracticeFormPage enterFirstNameAndLastName(String firstName, String lastName) {
        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);

        return this;
    }

    public AutomationPracticeFormPage enterEmail(String email) {
        $("#userEmail").setValue(email);

        return this;
    }

    public AutomationPracticeFormPage enterMobile(String mobile) {
        $("#userNumber").setValue(mobile);

        return this;
    }

    public AutomationPracticeFormPage checkboxGender(String value) {
        $("#genterWrapper").$(byText(value)).click();

        return this;
    }

    public AutomationPracticeFormPage checkboxHobbies(String... values) {
        for (String value : values) {
            $x(String.format("//label[text()='%s']", value)).click();
        }
        return this;
    }

    public AutomationPracticeFormPage setDateOfBirthday(String day, String month, String year) {
        $(".react-datepicker-wrapper").click();
        $(".react-datepicker__year-select").selectOption(year);
        $(".react-datepicker__month-select").selectOption(month);
        $(".react-datepicker__month").$(byText(day)).click();

        return this;
    }

    public AutomationPracticeFormPage setSubjects(String... values) {
        Arrays.stream(values).forEach(value -> {
            subjects.$("input").setValue(value).sendKeys(Keys.RETURN);
        });
        return this;
    }

    public AutomationPracticeFormPage setCurrentAddress(String value) {
        $("#currentAddress").setValue(value);

        return this;
    }

    public AutomationPracticeFormPage setStateAndCity(String state, String city) {
        stateAndCity.$(byText("Select State")).click();
        stateAndCity.$(byText(state)).click();
        stateAndCity.$(byText("Select City")).click();
        stateAndCity.$(byText(city)).click();

        return this;
    }

    public AutomationPracticeFormPage clickSubmit() {
        Util.clickSubmit();

        return this;
    }

    public AutomationPracticeFormPage uploadPicture(String filename) {
        uploadPicture.uploadFromClasspath(filename);

        return this;
    }


    public void checkForm(Map<String, String> expectedValues) {
        $("#example-modal-sizes-title-lg").shouldHave(text("Thanks for submitting the form"));

        for (Map.Entry<String, String> entry : expectedValues.entrySet()) {
            CustomAssertions.assertShouldHave($(".table-responsive").$(byText(entry.getKey())).parent(), text(entry.getValue()));
        }
    }
}
