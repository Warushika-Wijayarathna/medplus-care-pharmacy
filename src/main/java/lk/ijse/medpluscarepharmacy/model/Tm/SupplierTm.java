package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class SupplierTm {
        private String supplierId;
        private String name;
        private int contact;
        private String email;
        private JFXButton update;
        private JFXButton delete;

    public SupplierTm(String selectedSupplierId, String selectedOption, JFXButton remove) {
        this.supplierId=selectedSupplierId;
        this.name=selectedOption;
        this.delete=remove;
    }
}
