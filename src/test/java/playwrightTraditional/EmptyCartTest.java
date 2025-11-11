package playwrightTraditional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyCartTest extends BaseTest {

    @Test
    public void testEmptyCart() {
        page.navigate("https://depaul.bncollege.com/cart");
        page.click("button:has-text('Remove')");
        page.waitForTimeout(2000);
        assertTrue(page.locator("text=Your cart is empty").isVisible());
    }
}

