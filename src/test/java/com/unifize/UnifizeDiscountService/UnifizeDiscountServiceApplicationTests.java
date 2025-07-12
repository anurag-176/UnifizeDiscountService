package com.unifize.UnifizeDiscountService;

import com.unifize.UnifizeDiscountService.model.*;
import com.unifize.UnifizeDiscountService.service.DiscountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class UnifizeDiscountServiceApplicationTests {

	@Autowired
	private DiscountService discountService;

//	@Test
	void testCalculateDiscounts_withPumaShoes_shouldApply40PercentOff() {
		// 👟 Prepare input
		Product product = Product.builder()
				.id("PROD3")
				.brand("PUMA")
				.brandTier(BrandTier.PREMIUM)
				.category("Shoes")
				.basePrice(BigDecimal.valueOf(4000))
				.currentPrice(null)
				.build();

		CartItem item = CartItem.builder()
				.product(product)
				.quantity(1)
				.build();

		CustomerProfile customer = CustomerProfile.builder()
				.id("C002")
				.name("Anu")
				.tier("SILVER")
				.build();

		// 🧮 Execute
		DiscountedPrice result = discountService.calculateCartDiscounts(
				List.of(item),
				customer,
				Optional.empty()
		);

		// ✅ Verify output
		assertEquals(BigDecimal.valueOf(4000), result.getOriginalPrice());
		assertEquals(0, result.getFinalPrice().compareTo(BigDecimal.valueOf(2400)));
		assertEquals(0, result.getAppliedDiscounts().get("PUMA Brand 40% Off").compareTo(BigDecimal.valueOf(1600)));
		assertEquals("Discounts calculated", result.getMessage());
	}
}