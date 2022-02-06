package com.none.testpremiums.controllers;

import com.none.testpremiums.calculator.PremiumCalculator;
import com.none.testpremiums.controllers.policies.Policy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
public class PremiumsController {
    @Autowired
    private PremiumCalculator premiumCalculator;

    @ResponseBody
    @PostMapping("/calculatePremium")
    public BigDecimal calculatePremium(@RequestBody Policy policy) {
        //Validation can be implemented later
        premiumCalculator.calculate(policy);
        return premiumCalculator.calculate(policy);
    }
}
