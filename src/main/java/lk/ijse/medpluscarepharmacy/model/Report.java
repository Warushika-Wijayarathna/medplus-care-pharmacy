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

public class Report {
    private int reportId;
    private String type;
    private Date issueDate;
    private Date pickupDate;
    private String result;
    private int testId;
}
