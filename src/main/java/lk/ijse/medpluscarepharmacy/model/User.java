package lk.ijse.medpluscarepharmacy.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class User {
    private Integer userId;
    private String userName;
    private String password;

    public User(String userName, String password){
        this.userName=userName;
        this.password=password;
    }
}
