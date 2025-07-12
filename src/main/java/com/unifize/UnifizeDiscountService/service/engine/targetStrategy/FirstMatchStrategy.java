package com.unifize.UnifizeDiscountService.service.engine.targetStrategy;

import com.unifize.UnifizeDiscountService.model.CartItem;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class FirstMatchStrategy implements DiscountTargetStrategy {
    @Override
    public List<CartItem> apply(List<CartItem> items) {
        return items.isEmpty() ? List.of() : List.of(items.get(0));
    }
}
