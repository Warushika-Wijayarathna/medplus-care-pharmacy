package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemTm {
    private String itemId;
    private String description;
    private int qty;
    private double wholeSalePrice;
    private double retailPrice;
    private double discount;
    private LocalDate expDate;
    private List<JFXButton> action;


    public ItemTm(String itemId, String description, double retailPrice, double discount) {
        this.itemId = itemId;
        this.description = description;
        this.retailPrice = retailPrice;
        this.discount = discount;
    }

    public ItemTm(String itemId, String description, int qty, double wholeSalePrice, double retailPrice, double discount, LocalDate expDate) {
        this.itemId = itemId;
        this.description = description;
        this.qty = qty;
        this.wholeSalePrice = wholeSalePrice;
        this.retailPrice = retailPrice;
        this.discount = discount;
        this.expDate = expDate;
    }
}
