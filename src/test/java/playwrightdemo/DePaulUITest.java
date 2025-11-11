package playwrightdemo;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DePaulUITest {

    static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;

    @BeforeAll
    static void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
        );
    }

    @AfterAll
    static void tearDownClass() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setupTest() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void tearDownTest() {
        context.close();
    }

    @Test
    void testDePaulHomePageTitle() {
        page.navigate("https://www.depaul.edu/");
        String title = page.title();
        System.out.println("Page title: " + title);
        assertTrue(title.contains("DePaul University"), "Title should contain 'DePaul University'");
    }
/*    @Test
    void dumpAllClickableElements() {
        page.navigate("https://www.depaul.edu/");
        page.waitForTimeout(3000); // wait for dynamic content

        // Grab all elements with click handlers or role=button
        Locator clickable = page.locator("[role='button'], a, div, span, svg");
        int count = clickable.count();
        System.out.println("Found " + count + " clickable elements:");

        for (int i = 0; i < count; i++) {
            String role = clickable.nth(i).getAttribute("role");
            String ariaLabel = clickable.nth(i).getAttribute("aria-label");
            String text = clickable.nth(i).innerText();
            System.out.println("Element " + i + ": role='" + role + "' aria-label='" + ariaLabel + "' text='" + text + "'");
        }
    }*/

    @Test
    void testSearchFeatureSmartDetection() {
        System.out.println("🌐 Navigating to DePaul University site...");
        page.navigate("https://www.depaul.edu/");
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        System.out.println("✅ Page loaded (DOM content ready)");

        // Allow a few seconds for JS-driven elements to appear
        page.waitForTimeout(3000);

        // Try multiple ways to locate the search trigger
        Locator searchTrigger = page.locator(
                "a[aria-label*='Search'], " +        // link with aria-label
                        "div[aria-label*='Search'], " +      // div button
                        "button[aria-label*='Search'], " +   // button
                        "a:has-text('Search'), " +           // any link containing text Search
                        "div:has-text('Search'), " +         // any div containing text Search
                        "svg.icon-search"                    // icon
        );

        int count = searchTrigger.count();
        System.out.println("Found " + count + " potential search triggers");

        if (count == 0) {
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("no_search_button.png")));
            Assertions.fail("Search trigger not found");
        }

        // Click the first visible trigger
        Locator visibleSearch = searchTrigger.filter(new Locator.FilterOptions().setHasText("Search")).first();
        if (!visibleSearch.isVisible()) {
            visibleSearch = searchTrigger.first();
        }

        System.out.println("Click search button");
        visibleSearch.click(new Locator.ClickOptions().setTimeout(10000));

        // Wait for search input
        Locator slideInput = page.locator("#search_slide_input");
        Locator mainInput = page.locator("#search_input");

        Locator searchBox = slideInput.isVisible() ? slideInput : mainInput;

        searchBox.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15000));

        System.out.println("Search input visible");
        searchBox.fill("computer science");
        searchBox.press("Enter");

        // Wait for the results to appear
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForTimeout(3000);

        System.out.println("Waiting for results page");
        Assertions.assertTrue(page.title().toLowerCase().contains("search") ||
                        page.url().contains("search"),
                "Results page should open after searching");
    }



}