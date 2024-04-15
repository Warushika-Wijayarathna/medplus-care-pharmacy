package lk.ijse.medpluscarepharmacy.model.Tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTm {

    private int userId;
    private String userName;
    private String password;
    private JFXButton update;


    public UserTm(int userId, String userName, String password){
        this.userId=userId;
        this.userName=userName;
        this.password=password;
    }
}
