import com.github.javafaker.Faker;
import helpers.CustomDate;
import org.junit.jupiter.api.Test;
import pages.AutomationPracticeFormPage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static helpers.CustomDate.calendarToString;

public class SeventhHomeworkTest extends BaseTest {

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
