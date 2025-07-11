package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.dto.DiscountPolicyRequestDto;
import java.util.List;

public interface DiscountPolicyService {

    DiscountPolicy createPolicy(DiscountPolicyRequestDto dto);

    DiscountPolicy updatePolicy(String id, DiscountPolicyRequestDto dto);

    void deletePolicy(String id);

    List<DiscountPolicy> getAllPolicies();
}
