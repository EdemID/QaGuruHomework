import com.github.javafaker.Faker;
import helpers.CustomDate;
import org.junit.jupiter.api.Test;
import pages.AutomationPracticeFormPage;

import java.util.Calendar;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class UITest extends BaseTest {

    private Calendar birthday;
    private String firstName,
            lastName,
            email,
            mobile,
            currentAddress;

    {
        Faker faker = new Faker(new Locale("en"));
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();
        mobile = faker.phoneNumber().subscriberNumber(10);
        currentAddress = faker.address().fullAddress();
        birthday = CustomDate.toCalendar(faker.date().birthday(10, 24));
    }

    @Test
    void studentRegistration() {
        open(props.automationPracticeFormURL());

        AutomationPracticeFormPage automationPracticeFormPage = new AutomationPracticeFormPage();
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
