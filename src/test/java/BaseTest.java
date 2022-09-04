import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BaseTest {

    private static DesiredCapabilities capabilities = new DesiredCapabilities();
    private static ChromeOptions options = new ChromeOptions();

    @BeforeAll
    private static void beforeAllTests() {
        options.addArguments("--lang=ru-ru");
        options.addArguments("start-maximized");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }
}
