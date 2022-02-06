package com.none.testpremiums.calculator;

import com.none.testpremiums.controllers.policies.Policy;
import com.none.testpremiums.controllers.policies.PolicyObject;
import com.none.testpremiums.controllers.policies.PolicySubObject;
import com.none.testpremiums.controllers.policies.RiskType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.none.testpremiums.calculator.PremiumCalculator.*;
import static org.junit.jupiter.api.Assertions.*;

class PremiumCalculatorTest {
    @Test
    void calculatePolicySumBasedOnTasksACCCriteria() {
        PremiumCalculator premiumCalculator = new PremiumCalculator();
        final Policy policy1 = populatePolicyWithOnePolicyObjectAndOneFireSubAndOneTheftSub("100.00", "8.00");
        final Policy policy2 = populatePolicyWithOnePolicyObjectAndOneFireSubAndOneTheftSub("500.00", "102.51");

        assertEquals(new BigDecimal("2.28"), premiumCalculator.calculate(policy1));
        assertEquals(new BigDecimal("17.13"), premiumCalculator.calculate(policy2));
    }

    @Test
    void calculatePolicySumWithOnlyFireSubObjects() {
        final PremiumCalculator premiumCalculator = new PremiumCalculator();
        Policy policy1 = new Policy();

        final PolicyObject policyObject1 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject1, RiskType.FIRE,
                "5.00", "6.00", "7.00");
        policy1.setPolicyObjects(Collections.singletonList(policyObject1));

        assertEquals(new BigDecimal("0.25"), premiumCalculator.calculate(policy1));

        final PolicyObject policyObject2 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject2, RiskType.FIRE,
                "100");
        List<PolicyObject> policyObjects = new ArrayList<>();
        policyObjects.add(policyObject1);
        policyObjects.add(policyObject2);
        policy1.setPolicyObjects(policyObjects);

        assertEquals(new BigDecimal("2.83"), premiumCalculator.calculate(policy1));
    }

    @Test
    void calculatePolicySumWithOnlyTheftSubObjects() {
        PremiumCalculator premiumCalculator = new PremiumCalculator();
        Policy policy1 = new Policy();

        final PolicyObject policyObject1 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject1, RiskType.THEFT,
                "1.00", "2.00", "3.00");
        policy1.setPolicyObjects(Collections.singletonList(policyObject1));

        assertEquals(new BigDecimal("0.66"), premiumCalculator.calculate(policy1));

        final PolicyObject policyObject2 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject2, RiskType.THEFT,
                "4.00");
        List<PolicyObject> policyObjects = new ArrayList<>();

        policyObjects.add(policyObject1);
        policyObjects.add(policyObject2);
        policy1.setPolicyObjects(policyObjects);

        assertEquals(new BigDecimal("1.10"), premiumCalculator.calculate(policy1));

        final Policy policy2 = new Policy();
        List<PolicyObject> policyObjects2 = new ArrayList<>();
        PolicyObject policyObject3 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject3, RiskType.THEFT, "100");
        policyObjects2.add(policyObject1);
        policyObjects2.add(policyObject2);
        policyObjects2.add(policyObject3);
        policy2.setPolicyObjects(policyObjects2);

        assertEquals(new BigDecimal("5.50"), premiumCalculator.calculate(policy2));
    }

    @Test
    void calculatePolicySumWithMultipleObjectsAndSubObjects() {
        final PremiumCalculator premiumCalculator = new PremiumCalculator();
        final Policy policy1 = new Policy();
        final PolicyObject policyObject1 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject1, RiskType.FIRE, "10.00", "20.00");

        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject1, RiskType.THEFT, "1.00", "3.00");
        policy1.setPolicyObjects(Collections.singletonList(policyObject1));
        // 0.42 + 0.44
        assertEquals(new BigDecimal("0.86"), premiumCalculator.calculate(policy1));

        final PolicyObject policyObject2 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject2, RiskType.FIRE, "80.00");

        final Policy policy2 = new Policy();
        final List<PolicyObject> policyObjects1 = new ArrayList<>();
        policyObjects1.add(policyObject1);
        policyObjects1.add(policyObject2);
        policy2.setPolicyObjects(policyObjects1);
        // 2.64 + 0.44
        assertEquals(new BigDecimal("3.08"), premiumCalculator.calculate(policy2));

        final Policy policy3 = new Policy();
        final List<PolicyObject> policyObjects2 = new ArrayList<>();
        PolicyObject policyObject3 = new PolicyObject();
        populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(policyObject3,RiskType.THEFT,"10.00","20.00");

        policyObjects2.add(policyObject2);
        policyObjects2.add(policyObject3);
        policy3.setPolicyObjects(policyObjects2);
        // 1.12 + 1.50
        assertEquals(new BigDecimal("2.62"), premiumCalculator.calculate(policy3));

        final Policy policy4 = new Policy();
        final List<PolicyObject> policyObjects3 = new ArrayList<>();
        policyObjects3.add(policyObject1);
        policyObjects3.add(policyObject2);
        policyObjects3.add(policyObject3);
        policy4.setPolicyObjects(policyObjects3);
        // 2.64 + 1.7
        assertEquals(new BigDecimal("4.34"),premiumCalculator.calculate(policy4));


    }

    @Test
    void calculateBasedOnFireType() {
        PremiumCalculator premiumCalculator = new PremiumCalculator();
        final PolicySubObject subObject1 = new PolicySubObject();
        subObject1.setRiskType(RiskType.FIRE);
        subObject1.setInsuredSum(new BigDecimal("1000.00"));
        final PolicySubObject subObject2 = new PolicySubObject();
        subObject2.setInsuredSum(new BigDecimal("5.00"));

        assertEquals(new BigDecimal("24.00"),
                premiumCalculator.calculateBasedOnType(Collections.singletonList(subObject1), RiskType.FIRE));
        assertEquals(new BigDecimal("0.07"),
                premiumCalculator.calculateBasedOnType(Collections.singletonList(subObject2), RiskType.FIRE));

        List<PolicySubObject> policySubObjects = new ArrayList<>();
        policySubObjects.add(subObject1);
        policySubObjects.add(subObject2);
        assertEquals(new BigDecimal("24.12"),
                premiumCalculator.calculateBasedOnType(policySubObjects, RiskType.FIRE));
    }

    @Test
    void calculateBasedOnTheftType() {
        PremiumCalculator premiumCalculator = new PremiumCalculator();
        final PolicySubObject subObject1 = new PolicySubObject();
        subObject1.setRiskType(RiskType.THEFT);
        subObject1.setInsuredSum(new BigDecimal("1000.00"));
        final PolicySubObject subObject2 = new PolicySubObject();
        subObject2.setInsuredSum(new BigDecimal("5.00"));

        assertEquals(new BigDecimal("50.00"),
                premiumCalculator.calculateBasedOnType(Collections.singletonList(subObject1), RiskType.THEFT));
        assertEquals(new BigDecimal("0.55"),
                premiumCalculator.calculateBasedOnType(Collections.singletonList(subObject2), RiskType.THEFT));

        List<PolicySubObject> policySubObjects = new ArrayList<>();
        policySubObjects.add(subObject1);
        policySubObjects.add(subObject2);

        assertEquals(new BigDecimal("50.25"),
                premiumCalculator.calculateBasedOnType(policySubObjects, RiskType.THEFT));
    }

    @Test
    void getCoefficient() {
        PremiumCalculator premiumCalculator = new PremiumCalculator();
        BigDecimal fireUnderCoefficient = premiumCalculator.getCoefficient(RiskType.FIRE, new BigDecimal("100"));
        BigDecimal fireOverCoefficient = premiumCalculator.getCoefficient(RiskType.FIRE, new BigDecimal("101"));
        BigDecimal theftUnderCoefficient = premiumCalculator.getCoefficient(RiskType.THEFT, new BigDecimal("14"));
        BigDecimal theftGtOrEqCoefficient = premiumCalculator.getCoefficient(RiskType.THEFT, new BigDecimal("15"));

        assertEquals(DEFAULT_COEFFICIENT_FIRE, fireUnderCoefficient);
        assertEquals(COEFFICIENT_FIRE, fireOverCoefficient);
        assertEquals(DEFAULT_COEFFICIENT_THEFT, theftUnderCoefficient);
        assertEquals(COEFFICIENT_THEFT, theftGtOrEqCoefficient);

    }

    protected Policy populatePolicyWithOnePolicyObjectAndOneFireSubAndOneTheftSub(String insuredSumForFire,
                                                                                  String insuredSumForTheft) {
        final Policy policy;
        final PolicyObject policyObject;
        PolicySubObject policySubObjectA = new PolicySubObject();
        policySubObjectA.setInsuredSum(new BigDecimal(insuredSumForFire));
        policySubObjectA.setRiskType(RiskType.FIRE);
        PolicySubObject policySubObjectB = new PolicySubObject();
        policySubObjectB.setInsuredSum(new BigDecimal(insuredSumForTheft));
        policySubObjectB.setRiskType(RiskType.THEFT);

        List<PolicySubObject> policySubObjects = new ArrayList<>();
        policySubObjects.add(policySubObjectA);
        policySubObjects.add(policySubObjectB);

        policy = new Policy();
        policyObject = new PolicyObject();
        policy.setPolicyObjects(Collections.singletonList(policyObject));
        policy.getPolicyObjects().get(0).setSubObjects(policySubObjects);

        return policy;
    }

    protected PolicyObject populatePolicyObjectWithMultipleSubObjectsOfSameRiskType(PolicyObject policyObject,
                                                                                    RiskType riskType,
                                                                                    String... subObjectInsuredSums) {
        List<PolicySubObject> subObjectList = CollectionUtils.isEmpty(policyObject.getSubObjects())
                ? new ArrayList<>() : policyObject.getSubObjects();
        for (String subObjectInsuredSum : subObjectInsuredSums) {
            subObjectList.add(createPolicySubObject(riskType, subObjectInsuredSum));
        }
        policyObject.setSubObjects(subObjectList);
        return policyObject;
    }

    protected PolicySubObject createPolicySubObject(RiskType riskType, String insuredSum) {
        PolicySubObject policySubObject = new PolicySubObject();
        policySubObject.setInsuredSum(new BigDecimal(insuredSum));
        policySubObject.setRiskType(riskType);
        return policySubObject;
    }
}