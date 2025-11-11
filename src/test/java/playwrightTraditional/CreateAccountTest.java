package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountTest extends BaseTest {

    @Test
    public void testCreateAccountPage() {
        page.navigate("https://depaul.bncollege.com/checkout");

        assertTrue(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Create Account")).isVisible());
        page.click("button:has-text('Proceed as Guest')");
    }
}

