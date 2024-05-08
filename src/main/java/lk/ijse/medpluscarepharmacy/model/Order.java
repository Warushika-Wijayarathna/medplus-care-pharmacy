package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data

public class Order {
    private String orderId;
    private int qty;
    private double total;
    private String cust_id;
    private String user_id;
    private LocalDate date;
}