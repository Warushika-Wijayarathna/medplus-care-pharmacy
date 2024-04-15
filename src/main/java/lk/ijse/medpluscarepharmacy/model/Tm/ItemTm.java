package lk.ijse.medpluscarepharmacy.model.Tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemTm {
    private int itemId;
    private String description;
    private int qty;
    private double wholeSalePrice;
    private double retailPrice;
    private double discount;

    public ItemTm(int itemId, String description, double retailPrice, double discount) {
        this.itemId = itemId;
        this.description = description;
        this.retailPrice = retailPrice;
        this.discount = discount;
    }
}
