package helpers;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class Util {

    private static SelenideElement submit = $("[type=submit]");

    public static void clickSubmit() {
        submit.sendKeys(Keys.RETURN);
    }
}
