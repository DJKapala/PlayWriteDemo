package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PickupInfoTest extends BaseTest {

    @Test
    public void testPickupInformation() {
        assertTrue(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Pickup Information")).isVisible());
        assertTrue(page.locator("text=DePaul University Loop Campus & SAIC").isVisible());
        assertTrue(page.locator("text=I’ll pick them up").isVisible());

        assertTrue(page.locator("text=Subtotal").innerText().contains("149.98"));
        assertTrue(page.locator("text=Handling").innerText().contains("2.00"));
        assertTrue(page.locator("text=TBD").isVisible());
        assertTrue(page.locator("text=151.98").isVisible());

        page.click("button:has-text('Continue')");
    }
}
