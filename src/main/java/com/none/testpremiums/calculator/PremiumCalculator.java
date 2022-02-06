package com.none.testpremiums.calculator;

import com.none.testpremiums.controllers.policies.Policy;
import com.none.testpremiums.controllers.policies.PolicyObject;
import com.none.testpremiums.controllers.policies.PolicySubObject;
import com.none.testpremiums.controllers.policies.RiskType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PremiumCalculator {
    public PremiumCalculator() {
    }

    public static final BigDecimal DEFAULT_COEFFICIENT_FIRE = new BigDecimal("0.014");
    public static final BigDecimal COEFFICIENT_FIRE = new BigDecimal("0.024");
    public static final BigDecimal DEFAULT_COEFFICIENT_THEFT = new BigDecimal("0.11");
    public static final BigDecimal COEFFICIENT_THEFT = new BigDecimal("0.05");

    public BigDecimal calculate(final Policy policy) {
        List<PolicyObject> policyObjects = getPolicyObjects(policy);

        if (CollectionUtils.isEmpty(policyObjects)) {
            return BigDecimal.ZERO; // Can be an option to throw exception if there are no policyObjects
        }
        final List<PolicySubObject> subObjectsByFire = new ArrayList<>();
        final List<PolicySubObject> subObjectsByTheft = new ArrayList<>();

        for (PolicyObject policyObject : policyObjects) {
            subObjectsByFire.addAll(getSubObjectsByRiskType(policyObject, RiskType.FIRE));
            subObjectsByTheft.addAll(getSubObjectsByRiskType(policyObject, RiskType.THEFT));
        }

        if (CollectionUtils.isNotEmpty(subObjectsByFire) && CollectionUtils.isNotEmpty(subObjectsByTheft)) {
            final BigDecimal fireSum = calculateBasedOnType(subObjectsByFire, RiskType.FIRE);
            final BigDecimal theftSum = calculateBasedOnType(subObjectsByTheft, RiskType.THEFT);

            return fireSum.add(theftSum);
        } else {
            if (CollectionUtils.isNotEmpty(subObjectsByFire)) {

                return calculateBasedOnType(subObjectsByFire, RiskType.FIRE);
            }
            if (CollectionUtils.isNotEmpty(subObjectsByTheft)) {

                return calculateBasedOnType(subObjectsByTheft, RiskType.THEFT);
            }
        }
        return BigDecimal.ZERO; // Can be an option to throw Exception if CollectionUtils.isEmpty(SubObjects);
    }

    protected List<PolicyObject> getPolicyObjects(final Policy policy) {
        return new ArrayList<>(policy.getPolicyObjects());
    }

    protected List<PolicySubObject> getSubObjectsByRiskType(final PolicyObject policyObject, final RiskType riskType) {
        final List<PolicySubObject> subObjects = policyObject.getSubObjects();
        return subObjects.stream()
                .filter(subObject -> riskType.equals(subObject.getRiskType()))
                .collect(Collectors.toList());
    }

    protected BigDecimal calculateBasedOnType(List<PolicySubObject> policySubObjects, RiskType riskType) {
        BigDecimal sum = BigDecimal.ZERO;
        for (PolicySubObject subObject : policySubObjects) {
            sum = sum.add(subObject.getInsuredSum());
        }
        BigDecimal coefficient = getCoefficient(riskType, sum);
        BigDecimal multipliedValue = sum.multiply(coefficient);

        return multipliedValue.setScale(2, RoundingMode.HALF_UP);

    }

    protected BigDecimal getCoefficient(RiskType riskType, BigDecimal sum) {
        switch (riskType) {
            case FIRE:
                return sum.compareTo(new BigDecimal("100")) > 0 ? PremiumCalculator.COEFFICIENT_FIRE : PremiumCalculator.DEFAULT_COEFFICIENT_FIRE;
            case THEFT:
                return sum.compareTo(new BigDecimal("15")) >= 0 ? PremiumCalculator.COEFFICIENT_THEFT : PremiumCalculator.DEFAULT_COEFFICIENT_THEFT;
            default:
                return BigDecimal.ZERO; // can be an option of throwing Exception for RiskTypes
        }
    }
}
