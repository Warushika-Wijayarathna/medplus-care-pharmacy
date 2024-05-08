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

public class ReportTm {
    private String reportId;
    private String testId;
    private String result;
    private LocalDate issueDate;
    private LocalDate pickupDate;
    private List<JFXButton> action;
}
