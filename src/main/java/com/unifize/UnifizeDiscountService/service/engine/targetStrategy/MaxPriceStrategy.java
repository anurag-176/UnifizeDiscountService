package com.unifize.UnifizeDiscountService.service.engine.targetStrategy;

import com.unifize.UnifizeDiscountService.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MaxPriceStrategy implements DiscountTargetStrategy {
    @Override
    public List<CartItem> apply(List<CartItem> items) {
        return items.stream()
                .max(Comparator.comparing(CartItem::getPrice))
                .map(List::of)
                .orElse(List.of());
    }
}
