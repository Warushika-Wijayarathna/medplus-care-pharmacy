package lk.ijse.medpluscarepharmacy.model;

import java.time.LocalDate;
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
    private String custId;
    private String reportId;
    private String testId;
    private String result;
    private LocalDate issueDate;
    private LocalDate pickupDate;

    public Report(String custId,String testId, String result, LocalDate issueDate, LocalDate pickUpDate) {
        this.custId=custId;
        this.testId=testId;
        this.result=result;
        this.issueDate=issueDate;
        this.pickupDate=pickUpDate;
    }
}

