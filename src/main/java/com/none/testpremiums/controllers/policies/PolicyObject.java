package com.none.testpremiums.controllers.policies;

import java.util.List;

public class PolicyObject {
    private String name;
    private List<PolicySubObject> subObjects;

    public PolicyObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PolicySubObject> getSubObjects() {
        return subObjects;
    }

    public void setSubObjects(List<PolicySubObject> subObjects) {
        this.subObjects = subObjects;
    }
}
