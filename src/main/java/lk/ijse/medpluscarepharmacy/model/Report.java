package lk.ijse.medpluscarepharmacy.model;

import java.util.Date;

import lk.ijse.medpluscarepharmacy.model.Tm.ReportTm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Report {
    private Report reportId;
    private int testId;
    private String result;
    private Date issueDate;
    private Date pickupDate;
}

