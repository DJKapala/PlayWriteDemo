package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @BeforeEach
    void setUp() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))
                .setRecordVideoSize(1280, 720)
        );
        page = context.newPage();
        page.navigate("https://depaul.bncollege.com/");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // --- Handle the campus selection popup safely ---
        try {
            Locator campusSelector = page.locator("#bned_modal_preferred_campus_selector");

            if (campusSelector.isVisible()) {
                campusSelector.selectOption(new SelectOption().setValue("85")); // Use value, not label (more reliable)

                // Click Continue or Save if available
                Locator saveButton = page.locator("button:has-text('Save')");
                Locator continueButton = page.locator("button:has-text('Continue')");

                if (saveButton.isVisible()) {
                    saveButton.click();
                } else if (continueButton.isVisible()) {
                    continueButton.click();
                }

                page.waitForLoadState(LoadState.NETWORKIDLE);
            }
        } catch (Exception e) {
            System.out.println("No campus selection popup appeared, continuing...");
        }
    }


    @AfterEach
    void tearDown() {
        context.close();
    }

    @AfterAll
    static void tearDownAll() {
        playwright.close();
    }
}
