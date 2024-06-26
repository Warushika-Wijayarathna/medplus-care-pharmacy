package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class EmployeeTm {
    private String employeeId;
    private String name;
    private String position;
    private String address;
    private String contactNo;
    private double salary;
    private String userId;
    private JFXButton update;

}
