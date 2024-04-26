package lk.ijse.medpluscarepharmacy.model;

import java.util.Date;
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

public class Prescription {
    private String prescriptionId;
    private String patientName;
    private int age;
    private String medicalOfficerName;
    private String context;
    private String duration;
    private Date date;
    private int customerId;
}
