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

public class PrescTm {
    private String prescriptionId;
    private String customerId;
    private String patientName;
    private int age;
    private String medicalOfficerName;
    private String context;
    private String duration;
    private LocalDate date;
    private List<JFXButton> action;
}
