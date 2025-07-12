package com.unifize.UnifizeDiscountService.service.engine.targetStrategy;

import com.unifize.UnifizeDiscountService.model.CartItem;
import java.util.List;

public interface DiscountTargetStrategy {
    List<CartItem> apply(List<CartItem> items);
}
