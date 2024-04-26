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

public class Customer {
    private String customerId;
    private String name;
    private int contactNo;
    private String email;

    public Customer(String name, int contact, String email) {
        this.name=name;
        this.contactNo=contact;
        this.email=email;
    }
}
