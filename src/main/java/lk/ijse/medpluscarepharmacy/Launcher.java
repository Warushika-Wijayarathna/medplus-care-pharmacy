package lk.ijse.medpluscarepharmacy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Launcher extends Application {
    public static  void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane rootPane = FXMLLoader.load(this.getClass().getResource("/view/member_identifier_form.fxml"));


        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Member Identifier");
        primaryStage.centerOnScreen();

        primaryStage.show();
    }
}
