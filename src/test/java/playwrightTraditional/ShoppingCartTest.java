package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest extends BaseTest {

    @Test
    public void testShoppingCartDetails() {
        page.navigate("https://depaul.bncollege.com/cart");
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // --- Verify on Cart page ---
        assertTrue(page.locator("h1.page-title:has-text('Your Shopping Cart')").isVisible());

        // --- Verify product present ---
        assertTrue(page.locator("text=JBL Quantum True Wireless").first().isVisible());

        // --- Quantity & price ---
        page.waitForSelector("input[type='number']");
        assertEquals("1", page.locator("input[type='number']").inputValue());
        assertTrue(page.locator("text=$149.98").isVisible());

        // --- Select FAST pickup ---
        page.locator("label:has-text('FAST In-Store Pickup')").click();
        page.waitForTimeout(3000);

        // --- Sidebar totals ---
        assertTrue(page.locator("text=Subtotal").innerText().contains("149.98"));
        assertTrue(page.locator("text=Handling").innerText().contains("2.00"));
        assertTrue(page.locator("text=TBD").first().isVisible());
        assertTrue(page.locator("text=151.98").first().isVisible());

        // --- Promo code ---
        if (page.locator("input[name='promoCode']").isVisible()) {
            page.fill("input[name='promoCode']", "TEST");
            page.click("button:has-text('Apply')");
            page.waitForTimeout(2000);
            assertTrue(page.locator("text=/invalid|expired/i").first().isVisible());
        }

        // --- Proceed to checkout ---
        page.click("button:has-text('Proceed to Checkout')");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }
}
