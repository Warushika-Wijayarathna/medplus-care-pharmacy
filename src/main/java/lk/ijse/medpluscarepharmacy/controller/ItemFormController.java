package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SupplierTm;
import lk.ijse.medpluscarepharmacy.repository.ItemRepo;
import lk.ijse.medpluscarepharmacy.repository.SupplierRepo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ItemFormController {
    public TableView<ItemTm> itemTable;
    public TableColumn<?,?> colItemId;
    public TableColumn<?,?> colDesc;
    public TableColumn<?,?> colQty;
    public TableColumn<?,?> colWholePrice;
    public TableColumn<?, ?> colRetailPrice;
    public TableColumn<?,?> colDiscount;
    public TableColumn<?,?> colExpDate;
    public TableColumn<?,?> colSupp;
    public TableColumn<?,?> colUpdate;
    public TableColumn<?,?> colDelete;
    public JFXTextField descTxt;
    public JFXTextField qtyTxt;
    public JFXTextField wholeSalePriceTxt;
    public JFXButton addBtn;
    public JFXTextField searchBar;
    public JFXTextField retailTxt;
    public JFXTextField discountTxt;
    public DatePicker expDatePicker;
    public TableView<SupplierTm> supplierCart;
    public TableColumn<?,?> colSuppId;
    public TableColumn<?,?> colSuppName;
    public TableColumn<?,?> colRemove;
    @FXML
    private JFXTextField searchTextField;

    @FXML
    private JFXListView<String> optionsListView;

    private ObservableList<String> allSupplierNames;

    ObservableList<ItemTm> observableList = FXCollections.observableArrayList();
    public ItemTm selectedItem;
    public void initialize(){
        setCellValueFactory();
        loadAllItems();
        searchItem();

        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                itemTable.requestFocus();
            }
        });
        searchBar.requestFocus();

        try {
            allSupplierNames = FXCollections.observableArrayList(SupplierRepo.getAllSupplierNames());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateDropdownOptions(newValue.toLowerCase());
        });

        optionsListView.setPrefWidth(200);
        optionsListView.setVisible(false);

        searchTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                optionsListView.setVisible(false);
            }
        });
    }

    private void updateDropdownOptions(String lowerCase) {
        optionsListView.setVisible(true);
        FilteredList<String> filteredOptions = allSupplierNames.filtered(name -> name.toLowerCase().contains(lowerCase));

        optionsListView.setItems(filteredOptions);
    }


    @FXML
    void onSelectOption(MouseEvent event) {
        String selectedOption = optionsListView.getSelectionModel().getSelectedItem();
        if (selectedOption != null) {
            searchTextField.setText(selectedOption);
            optionsListView.setVisible(false);

            try {
                String selectedSupplierId = SupplierRepo.getSupplierIdByName(selectedOption);
                if (selectedSupplierId != null) {
                    SupplierTm selectedSupplier = new SupplierTm(selectedSupplierId, selectedOption, new JFXButton("Remove"));
                    supplierCart.getItems().add(selectedSupplier);
                } else {
                    System.out.println("Supplier ID not found for " + selectedOption);
                }
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }


    private void searchItem() {
        FilteredList<ItemTm> filteredData = new FilteredList<>(observableList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(itemTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if ((itemTm.getItemId()).contains(searchKey)) {
                    return true;
                } else if (itemTm.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getWholeSalePrice()).contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getRetailPrice()).contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getDiscount()).contains(searchKey)) {
                    return true;
                } else if (new SimpleDateFormat("dd-MM-yyyy").format(itemTm.getExpDate()).contains(searchKey)) {
                    return true;

                } else if (itemTm.getSuppId().toLowerCase().contains(searchKey)) {
                    return true;
                }
                return false;
            });
        });

        observableList.clear();
        observableList.addAll(filteredData);

        SortedList<ItemTm> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(itemTable.comparatorProperty());
        itemTable.setItems(sortedData);
    }

    private void loadAllItems() {
        observableList.clear();
        try {
            List<Item> itemList = ItemRepo.getAllItem();
            for (Item item : itemList) {
                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateItem(item));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteItem(item));

                ItemTm itemTm = new ItemTm(
                        item.getItemId(),
                        item.getDescription(),
                        item.getQty(),
                        item.getWholeSalePrice(),
                        item.getRetailPrice(),
                        item.getDiscount(),
                        item.getExpDate(),
                        item.getSuppId(),
                        updateButton,
                        deleteButton
                );
                observableList.add(itemTm);
            }
            itemTable.setItems(observableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleDeleteItem(Item item) {
        if (item != null) {
            try {
                ItemRepo.delete(item);
                observableList.remove(selectedItem);
                new Alert(Alert.AlertType.CONFIRMATION, "Item deleted successfully!").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Item to delete customer!").showAndWait();
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a item to delete!").showAndWait();
        }
    }

    private void handleUpdateItem(Item item) {
        if (selectedItem != null) {
            String itemId = selectedItem.getItemId();
            String description = selectedItem.getDescription();
            int qty = selectedItem.getQty();
            double wholeSalePrice = selectedItem.getWholeSalePrice();
            double retailPrice = selectedItem.getRetailPrice();
            double discount = selectedItem.getDiscount();
            LocalDate expDate = selectedItem.getExpDate();
            String suppId = selectedItem.getSuppId();

            Item updatedItem = new Item(
                    itemId,
                    descTxt.getText(),
                    Integer.parseInt(qtyTxt.getText()),
                    Double.parseDouble(wholeSalePriceTxt.getText()),
                    Double.parseDouble(retailTxt.getText()),
                    Double.parseDouble(discountTxt.getText()),
                    expDatePicker.getValue(),
                    suppId
            );

            try {
                ItemRepo.update(updatedItem);

                selectedItem.setDescription(description);
                selectedItem.setQty(qty);
                selectedItem.setWholeSalePrice(wholeSalePrice);
                selectedItem.setRetailPrice(retailPrice);
                selectedItem.setDiscount(discount);
                selectedItem.setExpDate(expDate);

                clear();

                new Alert(Alert.AlertType.CONFIRMATION, "Item Updated").showAndWait();

                loadAllItems();
                searchItem();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an item to update!").showAndWait();
        }
    }


    private void setCellValueFactory() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colWholePrice.setCellValueFactory(new PropertyValueFactory<>("wholeSalePrice"));
        colRetailPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("suppId"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
    }

    public void onMouseClickAction(MouseEvent mouseEvent) {
    }

    public void addBtnOnAction(ActionEvent actionEvent) {
    }

    public void clear() {
        descTxt.clear();
        qtyTxt.clear();
        wholeSalePriceTxt.clear();
        retailTxt.clear();
        discountTxt.clear();
        expDatePicker.setValue(null);
    }




}
