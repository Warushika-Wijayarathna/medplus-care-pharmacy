package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Supplier {
    private String supplierId;
    private String name;
    private int contact;
    private String email;

    public Supplier(String name, int contact, String email) {
        this.name=name;
        this.contact=contact;
        this.email=email;
    }


    public Supplier(String id, String name) {
        this.supplierId=id;
        this.name=name;
    }
}
