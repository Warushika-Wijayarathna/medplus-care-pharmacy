package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.repository.ItemRepo;

import javax.swing.text.TabableView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CashRegisterFormController {
    public AnchorPane rootPane;
    @FXML
    public TableColumn<ItemTm, Integer> colId;
    @FXML
    public TableColumn<ItemTm, String> colDesc;
    @FXML
    public TableColumn<ItemTm, Double> colPrice;
    @FXML
    public TableColumn<ItemTm, Double> colDiscount;
    public JFXTextField itemSearchBar;

    @FXML
    private TableView<ItemTm> itemTable;

    ObservableList<ItemTm> observableList = FXCollections.observableArrayList();

    public void initialize(){
        setCellValueFactory();
        loadAllItems();
        searchItem();

        itemSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                itemTable.requestFocus();
            }
        });
        itemSearchBar.requestFocus();
    }

    private void searchItem() {
        FilteredList<ItemTm> filteredData = new FilteredList<>(observableList, b -> true);
        itemSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(itemTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (String.valueOf(itemTm.getItemId()).contains(searchKey)) {
                    return true;
                } else if (itemTm.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getRetailPrice()).contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getDiscount()).contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<ItemTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());
        itemTable.setItems(sortedData);
    }


    private void loadAllItems() {

        try {
            List<Item> itemList = ItemRepo.getAll();
            for(Item item : itemList) {
                ItemTm itemTm = new ItemTm(
                        item.getItemId(),
                        item.getDescription(),
                        item.getRetailPrice(),
                        item.getDiscount()
                );
                observableList.add(itemTm);
            }
            itemTable.setItems(observableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }

    public void addBtnClickOnAction(ActionEvent actionEvent) {
    }

    public void checkOutBtnClickOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payment_pop.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Checkout");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void itemSearchBarOnAction(ActionEvent actionEvent) {
    }
}
