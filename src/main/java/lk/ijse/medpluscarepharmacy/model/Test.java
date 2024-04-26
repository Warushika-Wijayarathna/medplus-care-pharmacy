package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Test {
    private String  testId;
    private String description;
    private String lab;
    private String sampleType;
    private String testType;
    private double price;

    public Test(String desc, String lab, String sampleType, String testType, double priceOf) {
        this.description=desc;
        this.lab=lab;
        this.sampleType=sampleType;
        this.testType=testType;
        this.price=priceOf;
    }
}
