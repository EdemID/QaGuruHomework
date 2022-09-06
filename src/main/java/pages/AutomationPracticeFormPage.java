package pages;

import com.codeborne.selenide.SelenideElement;
import helpers.CustomDate;
import helpers.Util;
import org.openqa.selenium.Keys;

import java.util.Arrays;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AutomationPracticeFormPage {

    private SelenideElement formHeader = $(byText("Student Registration Form"));
    private SelenideElement firstNameElement = $("#firstName");
    private SelenideElement lastNameElement = $("#lastName");
    private SelenideElement nameElement = $(byText("Student Name"));
    private SelenideElement gender = $("#genterWrapper");
    private SelenideElement subjects = $("#subjectsWrapper");
    private SelenideElement currentAddress = $("#currentAddress");
    private SelenideElement stateAndCity = $("#stateCity-wrapper");
    private String fieldPath = "//label[text()='%s']/ancestor::div[@class='mt-2 row']//input";
    private String checkboxPath = "//label[text()='%s']";

    public SelenideElement getFormHeader() {
        return formHeader;
    }

    public AutomationPracticeFormPage enterFirstNameAndLastName(String firstName, String lastName) {
        firstNameElement.shouldHave(attribute("placeholder", "First Name")).val(firstName);
        lastNameElement.shouldHave(attribute("placeholder", "Last Name")).val(lastName);

        return this;
    }

    public AutomationPracticeFormPage enterInFieldInput(String field, String value) {
        $x(String.format(fieldPath, field)).val(value);

        return this;
    }

    public AutomationPracticeFormPage checkboxGender(String value) {
        gender.$(byText(value)).click();

        return this;
    }

    public AutomationPracticeFormPage checkboxHobbies(String... values) {
        for (String value : values) {
            $x(String.format(checkboxPath, value)).click();
        }
        return this;
    }

    public AutomationPracticeFormPage setDateOfBirthday(String day, String month, String year) {
        CustomDate.setDate(day, month, year);

        return this;
    }

    public AutomationPracticeFormPage setSubjects(String... values) {
        Arrays.stream(values).forEach(value -> {
            subjects.$("input").val(value).sendKeys(Keys.RETURN);
        });
        return this;
    }

    public AutomationPracticeFormPage setCurrentAddress(String value) {
        currentAddress.setValue(value);

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

    public void checkName(String name) {
        nameElement.parent().shouldHave(text(name));
    }
}
