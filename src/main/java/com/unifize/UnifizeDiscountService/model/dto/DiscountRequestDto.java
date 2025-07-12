package com.unifize.UnifizeDiscountService.model.dto;

import com.unifize.UnifizeDiscountService.model.CartItem;
import com.unifize.UnifizeDiscountService.model.CustomerProfile;
import com.unifize.UnifizeDiscountService.model.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequestDto {
    private List<CartItem> cartItems;
    private CustomerProfile customer;
    private PaymentInfo paymentInfo; // optional
}
