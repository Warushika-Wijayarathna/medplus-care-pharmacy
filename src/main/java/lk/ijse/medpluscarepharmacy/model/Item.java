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

public class Item {
    private int itemId;
    private String description;
    private int qty;
    private double wholeSalePrice;
    private double retailPrice;
    private double discount;

    public Item(int id, String desc, Double price, Double discount) {
        this.itemId=id;
        this.description=desc;
        this.retailPrice=price;
        this.discount=discount;
    }
}