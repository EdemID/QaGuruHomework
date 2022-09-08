import com.github.javafaker.Faker;
import helpers.CustomDate;
import org.junit.jupiter.api.Test;
import pages.AutomationPracticeFormPage;

import java.util.Calendar;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class UITest extends BaseTest {

    Faker faker = new Faker(new Locale("en"));

    @Test
    void studentRegistration() {
        var firstName = faker.name().firstName();
        var lastName = faker.name().lastName();
        var email = faker.internet().emailAddress();
        var mobile = faker.phoneNumber().subscriberNumber(10);
        var currentAddress = faker.address().fullAddress();
        var birthday = CustomDate.toCalendar(faker.date().birthday(10, 24));

        open(props.automationPracticeFormURL());

        var automationPracticeFormPage = new AutomationPracticeFormPage();
        automationPracticeFormPage.getFormHeader().shouldHave(text("Student Registration Form"));

        automationPracticeFormPage
                .enterFirstNameAndLastName(firstName, lastName)
                .enterInFieldInput("Email", email)
                .enterInFieldInput("Mobile", mobile)
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

        automationPracticeFormPage.checkName(firstName + " " + lastName);
    }
}
