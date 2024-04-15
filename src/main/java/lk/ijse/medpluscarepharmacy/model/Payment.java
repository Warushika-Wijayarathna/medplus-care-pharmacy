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

public class Payment {
    private int paymentId;
    private double cash;
    private double balance;
    private Date date;
    private int userId;
    private int orderId;
    private int supplierId;
}
