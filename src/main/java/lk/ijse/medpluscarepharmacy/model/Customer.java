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
    private int customerId;
    private String name;
    private String email;
    private String contactNo;
}
