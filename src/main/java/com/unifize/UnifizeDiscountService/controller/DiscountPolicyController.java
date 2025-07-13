package com.unifize.UnifizeDiscountService.controller;

import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.dto.DiscountPolicyRequestDto;
import com.unifize.UnifizeDiscountService.model.dto.DiscountPolicyResponseDto;
import com.unifize.UnifizeDiscountService.service.DiscountPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/discount-policies")
@RequiredArgsConstructor
public class DiscountPolicyController {

    private final DiscountPolicyService discountPolicyService;

    @PostMapping
    public ResponseEntity<DiscountPolicyResponseDto> create(@Valid @RequestBody DiscountPolicyRequestDto requestDto) {
        DiscountPolicy created = discountPolicyService.createPolicy(requestDto);
        return ResponseEntity.ok(DiscountPolicyResponseDto.from(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountPolicyResponseDto> update(@PathVariable String id,
                                                            @Valid @RequestBody DiscountPolicyRequestDto requestDto) {
        DiscountPolicy updated = discountPolicyService.updatePolicy(id, requestDto);
        return ResponseEntity.ok(DiscountPolicyResponseDto.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        discountPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DiscountPolicyResponseDto>> list() {
        List<DiscountPolicy> all = discountPolicyService.getAllPolicies();
        return ResponseEntity.ok(
                all.stream().map(DiscountPolicyResponseDto::from).collect(Collectors.toList())
        );
    }
}
