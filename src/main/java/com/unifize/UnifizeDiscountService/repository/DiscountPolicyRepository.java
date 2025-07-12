package com.unifize.UnifizeDiscountService.repository;

import com.unifize.UnifizeDiscountService.model.DiscountPolicy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DiscountPolicyRepository extends CrudRepository<DiscountPolicy, String> {

    Optional<DiscountPolicy> findByVoucherCode(String voucherCode);

}
