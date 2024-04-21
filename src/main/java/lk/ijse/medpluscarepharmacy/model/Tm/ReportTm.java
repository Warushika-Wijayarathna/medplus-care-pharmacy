package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class ReportTm {
    private int reportId;
    private int testId;
    private String result;
    private Date issueDate;
    private Date pickupDate;
    private JFXButton update;
    private JFXButton delete;
}
