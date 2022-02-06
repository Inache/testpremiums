package com.none.testpremiums.controllers.policies;

import java.math.BigDecimal;

public class PolicySubObject {
    private String name;
    private BigDecimal insuredSum;
    private RiskType riskType;

    public PolicySubObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getInsuredSum() {
        return insuredSum;
    }

    public void setInsuredSum(BigDecimal insuredSum) {
        this.insuredSum = insuredSum;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }
}
