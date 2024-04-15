package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data

public class Supplier {
    private int supplierId;
    private String name;
    private String originCountry;
}
