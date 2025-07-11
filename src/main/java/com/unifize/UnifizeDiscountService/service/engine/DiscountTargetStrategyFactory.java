package com.unifize.UnifizeDiscountService.service.engine;

import com.unifize.UnifizeDiscountService.model.DiscountTargetStrategyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.EnumMap;
import java.util.Map;

@Component
public class DiscountTargetStrategyFactory {

    private final Map<DiscountTargetStrategyEnum, DiscountTargetStrategy> strategyMap = new EnumMap<>(DiscountTargetStrategy.class);

    @Autowired
    public DiscountTargetStrategyFactory(
            FirstMatchStrategy firstMatchStrategy,
            MinPriceStrategy minPriceStrategy,
            MaxPriceStrategy maxPriceStrategy) {

        strategyMap.put(DiscountTargetStrategyEnum.FIRST_MATCH, firstMatchStrategy);
        strategyMap.put(DiscountTargetStrategyEnum.MIN_PRICE, minPriceStrategy);
        strategyMap.put(DiscountTargetStrategyEnum.MAX_PRICE, maxPriceStrategy);
    }

    public DiscountTargetStrategy getStrategy(DiscountTargetStrategyEnum type) {
        return strategyMap.get(type);
    }
}
