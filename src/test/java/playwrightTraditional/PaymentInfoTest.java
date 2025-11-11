package playwrightTraditional;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentInfoTest extends BaseTest {

    @Test
    public void testPaymentInfoPage() {
        assertTrue(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Payment Information")).isVisible());

        assertTrue(page.locator("text=Subtotal").innerText().contains("149.98"));
        assertTrue(page.locator("text=Handling").innerText().contains("2.00"));
        assertTrue(page.locator("text=15.58").isVisible());
        assertTrue(page.locator("text=167.56").isVisible());

        page.click("text=< Back to Cart");
    }
}

