package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TestTm {
    private String testId;
    private String description;
    private String lab;
    private String sampleType;
    private String testType;
    private double price;
    private JFXButton update;
    private JFXButton delete;
}
