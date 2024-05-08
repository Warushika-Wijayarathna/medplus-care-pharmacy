package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class ItemCartTm {
    private String itemId;
    private String description;
    private int qty;
    private double retailPrice;
    private double discount;
    private double total;
    private JFXButton remove;
}
