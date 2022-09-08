import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.interfaces.Props;
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
    static void beforeAllTests() {
        options.addArguments("--lang=ru-ru");
        options.addArguments("--start-maximized");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Configuration.browserCapabilities = capabilities;
        Configuration.baseUrl = "https://demoqa.com";
    }

    @BeforeEach
    void beforeEachTest() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @AfterEach
    void afterEachTest() {
        Selenide.closeWebDriver();
    }
}
