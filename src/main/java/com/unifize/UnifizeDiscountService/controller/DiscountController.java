package com.unifize.UnifizeDiscountService.controller;

import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.DiscountedPrice;
import com.unifize.UnifizeDiscountService.model.dto.DiscountRequestDto;
import com.unifize.UnifizeDiscountService.model.dto.DiscountValidationResponseDto;
import com.unifize.UnifizeDiscountService.model.dto.DiscountedPriceDto;
import com.unifize.UnifizeDiscountService.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/calculate")
    public ResponseEntity<DiscountedPriceDto> calculateDiscounts(@RequestBody DiscountRequestDto request) {
        DiscountedPrice discountedPrice = discountService.calculateCartDiscounts(
                request.getCartItems(),
                request.getCustomer(),
                Optional.ofNullable(request.getPaymentInfo())
        );
        return ResponseEntity.ok(DiscountedPriceDto.from(discountedPrice));
    }

    @PostMapping("/validate")
    public ResponseEntity<DiscountValidationResponseDto> validateDiscountCode(
            @RequestParam String code,
            @RequestBody DiscountRequestDto request) {
        boolean valid = discountService.validateDiscountCode(code, request.getCartItems(), request.getCustomer());
        return ResponseEntity.ok(new DiscountValidationResponseDto(valid, "Discount code is valid."));
    }
}
