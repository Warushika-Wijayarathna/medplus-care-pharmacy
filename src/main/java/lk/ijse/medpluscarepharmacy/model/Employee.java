package lk.ijse.medpluscarepharmacy.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class Employee {
    private Integer employeeId;
    private String name;
    private String position;
    private String address;
    private String contactNo;
    private double salary;
    private Integer userId;

    public Employee(String name, String position, String address, String contactNo, double salary, Integer userId) {
        this.name=name;
        this.position=position;
        this.address=address;
        this.contactNo=contactNo;
        this.salary=salary;
        this.userId=userId;
    }

    public Employee(Integer id,String name, String position, String address, String contactNo, double salary) {
        this.employeeId=id;
        this.name=name;
        this.position=position;
        this.address=address;
        this.contactNo=contactNo;
        this.salary=salary;
    }
}
