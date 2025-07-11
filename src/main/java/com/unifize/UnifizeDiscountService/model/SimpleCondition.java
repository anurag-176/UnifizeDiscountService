package com.unifize.UnifizeDiscountService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCondition implements DiscountConditionNode {
    private String field;               // e.g. "product.brand"
    private ComparatorType comparator; // EQUALS, GT, etc.
    private String value;              // Always string, parsed dynamically
}
