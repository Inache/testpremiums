# testpremiums
Premiumsapp
App for calculating premiums.
Builded on gradle
openjdk version "11.0.13"

To check PremiumCalculator#calculate
It's possible to make Post request at http://localhost:8080/calculatePremium
with RequestBody.

{
    "number": "LV20-02-100000-5",
    "status": "APPROVED",
    "policyObjects": [
        {
            "name": "House",
            "subObjects": [
                {
                    "name": "TV",
                    "insuredSum": "500.00",
                    "riskType": "FIRE"
                },
                {
                    "name": "Other",
                    "insuredSum": "102.51",
                    "riskType": "THEFT"
                }
            ]
        }
    ]
}

Or with ApplicationContextProvider.getApplicationContext().getBean("premiumCalculator", PremiumCalculator.class);

Tests are in com/none/testpremiums/calculator/PremiumCalculatorTest.java


