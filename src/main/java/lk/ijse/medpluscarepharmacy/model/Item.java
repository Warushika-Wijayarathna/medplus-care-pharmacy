package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Item {
    private String  itemId;
    private String description;
    private int qty;
    private double wholeSalePrice;
    private double retailPrice;
    private double discount;
    private LocalDate expDate;


    public Item(String id, String desc, int qty, Double price, Double discount, double wholeSalePrice, LocalDate exp ){
        this.itemId=id;
        this.description=desc;
        this.qty=qty;
        this.wholeSalePrice=wholeSalePrice;
        this.retailPrice=price;
        this.discount=discount;
        this.expDate=exp;

    }

    public Item(String id, String desc, double price, double discount) {
        this.itemId=id;
        this.description=desc;
        this.retailPrice=price;
        this.discount=discount;
    }

    public Item(String desc, int qty, double wholeSalePrice, double retailPrice, double discount, LocalDate expDate) {
        this.description=desc;
        this.qty=qty;
        this.wholeSalePrice=wholeSalePrice;
        this.retailPrice=retailPrice;
        this.discount=discount;
        this.expDate=expDate;
    }
}