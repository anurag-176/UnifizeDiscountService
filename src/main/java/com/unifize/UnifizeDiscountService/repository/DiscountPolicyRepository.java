package com.unifize.UnifizeDiscountService.repository;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountPolicyRepository extends CrudRepository<DiscountPolicy, String> {

}
