package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ContactInfoTest extends BaseTest {

    @Test
    public void testContactInformation() {
        assertTrue(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Contact Information")).isVisible());

        page.fill("input[name='firstName']", "John");
        page.fill("input[name='lastName']", "Doe");
        page.fill("input[name='email']", "john.doe@example.com");
        page.fill("input[name='phone']", "3125551234");

        // Assert sidebar
        assertTrue(page.locator("text=Subtotal").innerText().contains("149.98"));
        assertTrue(page.locator("text=Handling").innerText().contains("2.00"));
        assertTrue(page.locator("text=TBD").isVisible());
        assertTrue(page.locator("text=151.98").isVisible());

        page.click("button:has-text('Continue')");
    }
}

