package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.Supplier;
import lk.ijse.medpluscarepharmacy.model.Tm.CustomerTm;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SmallSupplierTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SupplierTm;
import lk.ijse.medpluscarepharmacy.repository.ItemRepo;
import lk.ijse.medpluscarepharmacy.repository.ItemSupplierRepo;
import lk.ijse.medpluscarepharmacy.repository.SupplierRepo;
import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemFormController {
    public TableView<ItemTm> itemTable;
    public TableColumn<ItemTm, ?> colItemId;
    public TableColumn<ItemTm, ?> colDesc;
    public TableColumn<ItemTm, ?> colQty;
    public TableColumn<ItemTm, ?> colWholePrice;
    public TableColumn<ItemTm, ?> colRetailPrice;
    public TableColumn<ItemTm, ?> colDiscount;
    public TableColumn<ItemTm, ?> colExpDate;
    public TableColumn<ItemTm, List<JFXButton>> colAction;
    public JFXTextField descTxt;
    public JFXTextField qtyTxt;
    public JFXTextField wholeSalePriceTxt;
    public JFXButton addBtn;
    public JFXTextField searchBar;
    public JFXTextField retailTxt;
    public JFXTextField discountTxt;
    public DatePicker expDatePicker;
    public TableView<SmallSupplierTm> supplierCart;
    public TableColumn<?, ?> colSuppId;
    public TableColumn<?, ?> colSuppName;
    public TableColumn<?, ?> colRemove;
    @FXML
    public JFXComboBox suppComBox;
    private ObservableList<String> allSupplierNames;

    ObservableList<ItemTm> observableList = FXCollections.observableArrayList();
    ObservableList<SmallSupplierTm> obList = FXCollections.observableArrayList();
    public ItemTm selectedItem;
    public SmallSupplierTm selectedSupplierOfCart;

    public void initialize() throws SQLException {
        setCellValueFactoryForItemTable();
        setCellValueFactoryForSupplierTable();
        loadAllItems();


        try {
            allSupplierNames = FXCollections.observableArrayList(SupplierRepo.getAllSupplierNames());
            suppComBox.setItems(allSupplierNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        Platform.runLater(()->{
            loadAllItems();

            descTxt.requestFocus();
            descTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    qtyTxt.requestFocus();
                }
            });

            qtyTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    wholeSalePriceTxt.requestFocus();
                }
            });

            wholeSalePriceTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    retailTxt.requestFocus();
                }
            });

            retailTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    discountTxt.requestFocus();
                }
            });

            discountTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    expDatePicker.requestFocus();
                }
            });

            expDatePicker.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    suppComBox.requestFocus();
                }
            });

            suppComBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtn.requestFocus();
                }
            });

            addBtn.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtnOnAction(new ActionEvent());
                }
            });
        });


    }


    public void onKeyPressedAction(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            itemTable.requestFocus();
        }
        searchBar.requestFocus();
        searchItem();
    }

    private void searchItem() {
        FilteredList<ItemTm> filteredData = new FilteredList<>(observableList, b -> true);
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(itemTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (itemTm.getItemId().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (itemTm.getDescription().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (String.valueOf(itemTm.getWholeSalePrice()).contains(searchKey)) {
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
            List<Item> itemList = ItemRepo.getAllItem();
            observableList.clear();

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

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                ItemTm itemTm = new ItemTm(
                        item.getItemId(),
                        item.getDescription(),
                        item.getQty(),
                        item.getWholeSalePrice(),
                        item.getRetailPrice(),
                        item.getDiscount(),
                        item.getExpDate(),
                        actionBtns
                );

                observableList.add(itemTm);
            }
            itemTable.setItems(observableList);
        } catch (SQLException e) {
            System.out.println("SQL Exception occurred while loading items: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception occurred while loading items: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleDeleteItem(Item item) {
        if (item != null) {
            try {
                boolean isDeleted = false;

                isDeleted = ItemSupplierRepo.deleteItem(item);

                if (isDeleted){
                    obList.clear();
                    loadAllItems();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete item!").showAndWait();
                    return;
                }
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
            try {
                String itemId = selectedItem.getItemId();
                String description = selectedItem.getDescription();
                int qty = selectedItem.getQty();
                double wholeSalePrice = selectedItem.getWholeSalePrice();
                double retailPrice = selectedItem.getRetailPrice();
                double discount = selectedItem.getDiscount();
                LocalDate expDate = selectedItem.getExpDate();

                if (Regex.isTextFieldValid(TextField.DESCRIPTION,description)){
                    new Alert(Alert.AlertType.WARNING, "Invalid Description!").showAndWait();
                    descTxt.requestFocus();
                    return;
                }

                if (Regex.isTextFieldValid(TextField.QTY, String.valueOf(qty))){
                    new Alert(Alert.AlertType.WARNING, "Invalid Quantity!").showAndWait();
                    qtyTxt.requestFocus();
                    return;
                }

                if (Regex.isTextFieldValid(TextField.PRICE, String.valueOf(wholeSalePrice))){
                    new Alert(Alert.AlertType.WARNING, "Invalid Whole Sale Price!").showAndWait();
                    wholeSalePriceTxt.requestFocus();
                    return;
                }

                if (Regex.isTextFieldValid(TextField.PRICE, String.valueOf(retailPrice))){
                    new Alert(Alert.AlertType.WARNING, "Invalid Retail Price!").showAndWait();
                    retailTxt.requestFocus();
                    return;
                }

                if (Regex.isTextFieldValid(TextField.PRICE, String.valueOf(discount))){
                    new Alert(Alert.AlertType.WARNING, "Invalid Discount!").showAndWait();
                    discountTxt.requestFocus();
                    return;
                }

                Item updatedItem = new Item(
                        itemId,
                        descTxt.getText(),
                        Integer.parseInt(qtyTxt.getText()),
                        Double.parseDouble(wholeSalePriceTxt.getText()),
                        Double.parseDouble(retailTxt.getText()),
                        Double.parseDouble(discountTxt.getText()),
                        expDatePicker.getValue()
                );

                List<String> allSupplierIds = new ArrayList<>();
                for (SmallSupplierTm supplier : supplierCart.getItems()) {
                    allSupplierIds.add(supplier.getSupplierId());
                }

                boolean isUpdated = ItemSupplierRepo.updateItem(updatedItem, allSupplierIds);

                if (isUpdated) {
                    obList.clear();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update item!").showAndWait();
                    return;
                }

                selectedItem.setDescription(description);
                selectedItem.setQty(qty);
                selectedItem.setWholeSalePrice(wholeSalePrice);
                selectedItem.setRetailPrice(retailPrice);
                selectedItem.setDiscount(discount);
                selectedItem.setExpDate(expDate);

                clear();

                new Alert(Alert.AlertType.CONFIRMATION, "Item updated successfully!").showAndWait();

                loadAllItems();
                searchItem();
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid input format! Please enter valid numbers.").showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select an item to update!").showAndWait();
        }
    }



    private void setCellValueFactoryForItemTable() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colWholePrice.setCellValueFactory(new PropertyValueFactory<>("wholeSalePrice"));
        colRetailPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));

        colAction.setCellFactory((TableColumn<ItemTm, List<JFXButton>> column) -> {
            return new TableCell<ItemTm, List<JFXButton>>() {
                @Override
                protected void updateItem(List<JFXButton> buttons, boolean empty) {
                    super.updateItem(buttons, empty);
                    if (empty || buttons == null || buttons.isEmpty()) {
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox();
                        for (JFXButton button : buttons) {
                            hbox.getChildren().add(button);
                        }
                        setGraphic(hbox);
                    }
                }

            };
        });
    }

    private void setCellValueFactoryForSupplierTable() {
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSuppName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRemove.setCellValueFactory(new PropertyValueFactory<>("remove"));
    }

    public void onMouseClickAction(MouseEvent mouseEvent) {
        obList.clear();

        try {
            if (mouseEvent.getClickCount() == 1) {
                int selectedIndex = itemTable.getSelectionModel().getSelectedIndex();
                int selectedIndexOfSupplier = supplierCart.getSelectionModel().getSelectedIndex();

                if (selectedIndex >= 0) {
                    selectedItem = itemTable.getItems().get(selectedIndex);

                    String itemId = selectedItem.getItemId();
                    String desc = selectedItem.getDescription();
                    int qty = selectedItem.getQty();
                    double wholeSalePrice = selectedItem.getWholeSalePrice();
                    double retailPrice = selectedItem.getRetailPrice();
                    double discount = selectedItem.getDiscount();
                    LocalDate expDate = selectedItem.getExpDate();

                    descTxt.setText(desc);
                    qtyTxt.setText(String.valueOf(qty));
                    wholeSalePriceTxt.setText(String.valueOf(wholeSalePrice));
                    retailTxt.setText(String.valueOf(retailPrice));
                    discountTxt.setText(String.valueOf(discount));
                    expDatePicker.setValue(expDate);

                    if (selectedIndexOfSupplier >= 0) {
                        selectedSupplierOfCart = supplierCart.getItems().get(selectedIndexOfSupplier);
                    }
                    List<Supplier> supplierTm = null;
                    try {
                        List<String> suppliers = ItemSupplierRepo.getSupplierIdsByItemId(itemId);
                        System.out.println(suppliers);
                        supplierTm = SupplierRepo.getSupplierDetailsBySupplierId(suppliers);
                        System.out.println(supplierTm);
                        for(Supplier supplier : supplierTm){
                            ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (47).png")));
                            removeIcon.setFitWidth(20);
                            removeIcon.setFitHeight(20);

                            JFXButton remove = new JFXButton();
                            remove.setGraphic(removeIcon);
                            remove.setOnAction(event-> {
                                supplierCart.getItems().remove(new SmallSupplierTm(supplier.getSupplierId(), supplier.getName(), remove));
                            });

                            SmallSupplierTm smallSupplierTm = new SmallSupplierTm(
                                    supplier.getSupplierId(),
                                    supplier.getName(),
                                    remove
                            );
                            obList.add(smallSupplierTm);

                        }
                        supplierCart.setItems(obList);
                        System.out.println(obList);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }



    public void addBtnOnAction(ActionEvent actionEvent) {
        String desc = descTxt.getText().trim();
        String qtyText = qtyTxt.getText().trim();
        String wholeSalePriceText = wholeSalePriceTxt.getText().trim();
        String retailPriceText = retailTxt.getText().trim();
        String discountText = discountTxt.getText().trim();
        LocalDate expDate = expDatePicker.getValue();

        if (Regex.isTextFieldValid(TextField.DESCRIPTION,desc)){
            new Alert(Alert.AlertType.WARNING, "Invalid Description!").showAndWait();
            descTxt.requestFocus();
            return;
        }

        if (Regex.isTextFieldValid(TextField.QTY,qtyText)){
            new Alert(Alert.AlertType.WARNING, "Invalid Quantity!").showAndWait();
            qtyTxt.requestFocus();
            return;
        }

        if (Regex.isTextFieldValid(TextField.PRICE,wholeSalePriceText)){
            new Alert(Alert.AlertType.WARNING, "Invalid Whole Sale Price!").showAndWait();
            wholeSalePriceTxt.requestFocus();
            return;
        }

        if (Regex.isTextFieldValid(TextField.PRICE,retailPriceText)){
            new Alert(Alert.AlertType.WARNING, "Invalid Retail Price!").showAndWait();
            retailTxt.requestFocus();
            return;
        }

        if (Regex.isTextFieldValid(TextField.PRICE,discountText)){
            new Alert(Alert.AlertType.WARNING, "Invalid Discount!").showAndWait();
            discountTxt.requestFocus();
            return;
        }


        if (desc.isEmpty() || qtyText.isEmpty() || wholeSalePriceText.isEmpty() || retailPriceText.isEmpty() || discountText.isEmpty() || expDate == null || supplierCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields!").showAndWait();
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            double wholeSalePrice = Double.parseDouble(wholeSalePriceText);
            double retailPrice = Double.parseDouble(retailPriceText);
            double discount = Double.parseDouble(discountText);

            String itemId = ItemRepo.generateItemId(new Item(desc, qty, wholeSalePrice, retailPrice, discount, expDate));


            Item newItem = new Item(itemId,desc, qty, wholeSalePrice, retailPrice, discount, expDate);

            List<String> allSupplierIds = new ArrayList<>();
            for (SmallSupplierTm supplier : supplierCart.getItems()) {
                allSupplierIds.add(supplier.getSupplierId());
            }

            boolean isSaved = false;

            isSaved = ItemSupplierRepo.saveItem(newItem, allSupplierIds);

            if (isSaved) {
                Platform.runLater(()->{
                    new Alert(Alert.AlertType.CONFIRMATION, "Item added successfully!").showAndWait();
                });


                ImageView updateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (44).png")));
                updateIcon.setFitWidth(20);
                updateIcon.setFitHeight(20);

                JFXButton updateButton = new JFXButton();
                updateButton.setGraphic(updateIcon);
                updateButton.setOnAction(event -> handleUpdateItem(newItem));

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (43).png")));
                deleteIcon.setFitWidth(20);
                deleteIcon.setFitHeight(20);

                JFXButton deleteButton = new JFXButton();
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setOnAction(event -> handleDeleteItem(newItem));

                List<JFXButton> actionBtns = new ArrayList<>();
                actionBtns.add(updateButton);
                actionBtns.add(deleteButton);

                observableList.add(new ItemTm(itemId, desc, qty, wholeSalePrice, retailPrice, discount, expDate, actionBtns));
                obList.clear();
                clear();
                descTxt.requestFocus();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add item!").showAndWait();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format!").showAndWait();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add item!").showAndWait();
            e.printStackTrace();
        }
    }
    public void clear() {
        descTxt.clear();
        qtyTxt.clear();
        wholeSalePriceTxt.clear();
        retailTxt.clear();
        discountTxt.clear();
        expDatePicker.setValue(null);
    }

    @FXML

    void onSelectOption(ActionEvent event) {
        String selectedSupplierName = (String) suppComBox.getSelectionModel().getSelectedItem();
        if (selectedSupplierName != null) {
            try {
                SupplierTm selectedSupplier = SupplierRepo.getSupplierTmByName(selectedSupplierName);
                if (selectedSupplier != null) {
                    ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (47).png")));
                    removeIcon.setFitWidth(20);
                    removeIcon.setFitHeight(20);

                    JFXButton remove = new JFXButton();
                    remove.setGraphic(removeIcon);
                    remove.setOnAction(e -> {
                        supplierCart.getItems().remove(new SmallSupplierTm(selectedSupplier.getSupplierId(), selectedSupplier.getName(), remove));
                    });
                    SmallSupplierTm smallSupplierTm = new SmallSupplierTm(selectedSupplier.getSupplierId(), selectedSupplier.getName(), remove);
                    supplierCart.getItems().add(smallSupplierTm);
                } else {
                    System.out.println("Supplier not found for " + selectedSupplierName);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void onDescription(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.DESCRIPTION, descTxt);
    }

    public void onQty(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.QTY, qtyTxt);
    }

    public void onWholeSale(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PRICE, wholeSalePriceTxt);
    }

    public void onRetailPrice(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PRICE, retailTxt);
    }

    public void onDiscount(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PRICE, discountTxt);
    }

    public void onFKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.F11){
            searchBar.requestFocus();
        }
    }
}

