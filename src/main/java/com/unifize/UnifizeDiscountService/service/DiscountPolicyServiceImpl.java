package com.unifize.UnifizeDiscountService.service;

import com.unifize.UnifizeDiscountService.exception.DiscountValidationException;
import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import com.unifize.UnifizeDiscountService.model.dto.DiscountPolicyRequestDto;
import com.unifize.UnifizeDiscountService.model.engine.ConditionNode;
import com.unifize.UnifizeDiscountService.repository.DiscountPolicyRepository;
import com.unifize.UnifizeDiscountService.service.engine.condition.ConditionExpressionParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountPolicyServiceImpl implements DiscountPolicyService {

    @Autowired
    private final DiscountPolicyRepository repository;

    @Override
    public DiscountPolicy createPolicy(DiscountPolicyRequestDto dto) throws DiscountValidationException {
        DiscountPolicy policy = dto.toEntity();
        policy.setId(UUID.randomUUID().toString());

        validateConditionExpression(dto, policy);

        return repository.save(policy);
    }

    private static void validateConditionExpression(DiscountPolicyRequestDto dto, DiscountPolicy policy) throws DiscountValidationException {
        if (StringUtils.hasLength(policy.getConditionExpression())) {
            ConditionNode node =ConditionExpressionParser.parse(dto.getConditionExpression());
            log.info("Expression passes check: {}", node.getExpression());
        }
    }

    @Override
    public DiscountPolicy updatePolicy(String id, DiscountPolicyRequestDto dto) {
        Optional<DiscountPolicy> existing = repository.findById(id);
        if (existing.isEmpty()) throw new RuntimeException("Policy not found");

        DiscountPolicy updated = dto.toEntity();
        updated.setId(id);

        validateConditionExpression(dto, updated);

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

