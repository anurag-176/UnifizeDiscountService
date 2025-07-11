package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.dto.DiscountPolicyRequestDto;
import com.unifize.UnifizeDiscountService.repository.DiscountPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiscountPolicyServiceImpl implements DiscountPolicyService {

    @Autowired
    private final DiscountPolicyRepository repository;

    @Override
    public DiscountPolicy createPolicy(DiscountPolicyRequestDto dto) {
        DiscountPolicy policy = dto.toEntity();
        policy.setId(UUID.randomUUID().toString());
        return repository.save(policy);
    }

    @Override
    public DiscountPolicy updatePolicy(String id, DiscountPolicyRequestDto dto) {
        Optional<DiscountPolicy> existing = repository.findById(id);
        if (existing.isEmpty()) throw new RuntimeException("Policy not found");

        DiscountPolicy updated = dto.toEntity();
        updated.setId(id);
        return repository.save(updated);
    }

    @Override
    public void deletePolicy(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<DiscountPolicy> getAllPolicies() {
        return (List<DiscountPolicy>) repository.findAll();
    }
}

