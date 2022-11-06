import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.interfaces.Props;
import helpers.CustomAttachments;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public abstract class BaseTest {

    protected static Props props = Props.props;
    private static DesiredCapabilities capabilities = new DesiredCapabilities();
    private static ChromeOptions options = new ChromeOptions();

    @BeforeAll
    static void config() {
        Configuration.baseUrl = System.getProperty("baseUrl", "https://demoqa.com");  // "https://demoqa.com"
        Configuration.browser = System.getProperty("browserName", "chrome");
        Configuration.browserSize = System.getProperty("browserSize", "1024x720");
        String browserName = System.getProperty("browserName");
        String remote = System.getProperty("conf.remote");
        if (remote != null) {
            Configuration.remote = remote; // "https://user1:1234@selenoid.autotests.cloud/wd/hub"
            capabilities.setCapability("enableVNC", true);
            capabilities.setCapability("enableVideo", true);
        }
        if ("chrome".equals(browserName)){
            options.addArguments("lang=ru-ru");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        }
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    void beforeEachTest() {
        // Добавляет сценарий - не шаги, а действия над элементами
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void afterEachTest() {
        Selenide.closeWebDriver();
    }

    @AfterEach
    void addAttachments() {
        CustomAttachments.screenshotAs("Скриншот последней страницы");
        CustomAttachments.pageSource();
        if (System.getProperty("conf.remote") != null) {
            CustomAttachments.addVideo();
        }
    }
}
