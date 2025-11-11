package playwrightTraditional;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookstoreTest extends BaseTest {

    @Test
    public void testBookstoreSearchAndAddToCart() {
        // --- Wait for site to fully load ---
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2000);

        // --- Locate search box (current BNCollege markup) ---
        Locator searchBox = page.locator("input[placeholder*='Search']");
        searchBox.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        searchBox.fill("earbuds");
        page.keyboard().press("Enter");

        // --- Wait for results to load ---
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // --- Expand and apply filters ---
        page.locator("text=Brand").click();
        page.locator("label:has-text('JBL')").click();
        page.waitForTimeout(1000);

        page.locator("text=Color").click();
        page.locator("label:has-text('Black')").click();
        page.waitForTimeout(1000);

        page.locator("text=Price").click();
        page.locator("label:has-text('Over $50')").click();
        page.waitForTimeout(2000);

        // --- Click on the correct JBL product ---
        Locator product = page.locator("a:has-text('JBL Quantum True Wireless Noise Cancelling Gaming')");
        product.first().click();

        // --- Wait for product page to load ---
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(3000);

        // --- Assert key product elements ---
        String productName = page.locator("h1, .pdp-header-title").innerText();
        assertTrue(productName.contains("JBL Quantum"), "Product name missing JBL Quantum");

        assertTrue(page.locator("text=SKU").isVisible(), "SKU not visible");
        assertTrue(page.locator("text=$").isVisible(), "Price not visible");
        assertTrue(page.locator(".pdp-description, .product-description").isVisible(), "Description not visible");

        // --- Add to cart ---
        Locator addToCart = page.locator("button:has-text('Add to Cart')");
        addToCart.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        addToCart.click();

        // --- Wait for cart update ---
        page.waitForTimeout(4000);

        // --- Assert cart count updated ---
        Locator cartCount = page.locator(".cart-quantity, [data-cart-count]");
        cartCount.waitFor(new Locator.WaitForOptions().setTimeout(60000));
        String countText = cartCount.innerText().trim();
        assertTrue(countText.contains("1"), "Cart count not updated to 1");

        // --- Go to cart ---
        Locator cartIcon = page.locator("a[href*='cart'], .cart-icon");
        cartIcon.first().click();

        // --- Final assertion: cart page visible ---
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertTrue(page.locator("text=/Shopping Cart/i").first().isVisible(), "Cart page not visible");
    }
}
