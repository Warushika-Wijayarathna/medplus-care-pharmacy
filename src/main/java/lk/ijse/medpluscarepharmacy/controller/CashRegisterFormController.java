package lk.ijse.medpluscarepharmacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Customer;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemCartTm;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.model.Tm.SmallSupplierTm;
import lk.ijse.medpluscarepharmacy.repository.ItemRepo;
import lk.ijse.medpluscarepharmacy.repository.CustomerRepo;
import lk.ijse.medpluscarepharmacy.repository.OrderRepo;
import lk.ijse.medpluscarepharmacy.repository.PlaceOrderRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ijse.medpluscarepharmacy.util.Regex;
import lk.ijse.medpluscarepharmacy.util.TextField;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import javax.print.attribute.standard.PrintQuality;




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
    public JFXTextField searchCustomer;
    public Label custLbl;
    public Label idLbl;
    public Label descLbl;
    public Label priceLbl;
    public Label discLbl;
    public JFXTextField qtyTxt;

    public JFXButton addBtn;
    public TableView<ItemCartTm> cartTable;
    public TableColumn<?,?> itemIdColumn;
    public TableColumn<?,?> descriptionColumn;
    public TableColumn<?,?> unitPriceColumn;
    public TableColumn<?,?> discountColumn;
    public TableColumn<?,?> qtyColumn;
    public TableColumn<?,?> totalColumn;
    public TableColumn<?,?> removeColumn;
    public Label totalLbl;
    public Label balanceLbl;
    public JFXTextField cashTxt;
    public JFXButton checkOutBtn;
    public Label dateLbl;
    public Label usrLbl;
    public JFXButton addNewCustBtn;

    @FXML
    private TableView<ItemTm> itemTable;


    ObservableList<ItemTm> observableList = FXCollections.observableArrayList();
    ObservableList<ItemCartTm> obList = FXCollections.observableArrayList();

    public void initialize() throws IOException {
        usrLbl.setVisible(false);
        setCellValueFactory();
        loadAllItems();

        Platform.runLater(() -> {
            dateLbl.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            searchCustomer.requestFocus();
        });

    }

    public void onSearchCustomer(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            searchCustomerByContactNumber();
            itemSearchBar.requestFocus();
        }
    }

    private void searchCustomerByContactNumber() {
        String contactNumber = searchCustomer.getText().trim();
        if (!Regex.isTextFieldValid(TextField.CONTACT, contactNumber)) {
            new Alert(Alert.AlertType.ERROR, "Invalid contact number").showAndWait();
            searchCustomer.requestFocus();
            return;
        }
        if (!contactNumber.isEmpty()) {
            try {
                Customer customer = CustomerRepo.searchCustomerByContact(contactNumber);
                if (customer != null) {
                    custLbl.setText(customer.getName());
                } else {
                    new Alert(Alert.AlertType.ERROR, "Customer not found").showAndWait();
                    Platform.runLater(() -> {
                        searchCustomer.requestFocus();
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            custLbl.setText("Please enter a contact number");
        }
    }

    public void onSearchKeyPressed(KeyEvent keyEvent) {
        itemSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                itemTable.requestFocus();
            }
        });
        itemSearchBar.requestFocus();
        searchItem();
    }

    private void searchItem() {
        FilteredList<ItemTm> filteredData = new FilteredList<>(observableList, b -> true);
        itemSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(itemTm -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();


                if (itemTm.getItemId().toLowerCase().contains(searchKey)) {
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

        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        removeColumn.setCellValueFactory(new PropertyValueFactory<>("remove"));
    }

    public void addBtnClickOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode()==KeyCode.ENTER) {
            addOnAction(new ActionEvent());
        }
    }

    private void removeRow() {
        int selectedIndex = cartTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cartTable.getItems().remove(selectedIndex);
            double netTotal = 0;
            for (ItemCartTm cartItem : cartTable.getItems()) {
                netTotal += cartItem.getTotal();
            }
            totalLbl.setText(String.valueOf(netTotal));
        } else {
            new Alert(Alert.AlertType.ERROR, "No item selected to remove").showAndWait();
        }
    }

    public void checkOutBtnClickOnAction(KeyEvent keyEvent) {
        checkoutAction(new ActionEvent());
    }


    public void onItemMouseClick(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String itemId = itemTable.getSelectionModel().getSelectedItem().getItemId();
            String description = itemTable.getSelectionModel().getSelectedItem().getDescription();
            double retailPrice = itemTable.getSelectionModel().getSelectedItem().getRetailPrice();
            double discount = itemTable.getSelectionModel().getSelectedItem().getDiscount();

            idLbl.setText(itemId);
            descLbl.setText(description);
            priceLbl.setText(String.valueOf(retailPrice));
            discLbl.setText(String.valueOf(discount));

            qtyTxt.requestFocus();
            qtyTxt.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    addBtn.requestFocus();
                }
            });
        }
    }

    public void clear(){
        idLbl.setText("");
        descLbl.setText("");
        priceLbl.setText("");
        discLbl.setText("");
        qtyTxt.setText("");
    }

    public void onFPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.F12) {
            cashTxt.requestFocus();

        }

        if (keyEvent.getCode() == KeyCode.F11) {
            addNewCustBtnOnAction(new ActionEvent());
        }
    }

    public void onCashKeyPressed(KeyEvent keyEvent) {
        cashTxt.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                double cash = Double.parseDouble(cashTxt.getText());
                double total = Double.parseDouble(totalLbl.getText());
                double balance = cash - total;
                balanceLbl.setText(String.valueOf(balance));

                checkOutBtn.requestFocus();
            }
        });
    }


    public void onItemsTableClick(MouseEvent mouseEvent) {
        String itemId = itemTable.getSelectionModel().getSelectedItem().getItemId();
        String description = itemTable.getSelectionModel().getSelectedItem().getDescription();
        double retailPrice = itemTable.getSelectionModel().getSelectedItem().getRetailPrice();
        double discount = itemTable.getSelectionModel().getSelectedItem().getDiscount();

        idLbl.setText(itemId);
        descLbl.setText(description);
        priceLbl.setText(String.valueOf(retailPrice));
        discLbl.setText(String.valueOf(discount));

        qtyTxt.requestFocus();
        qtyTxt.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                addBtn.requestFocus();
            }
        });
    }

    public void addNewCustBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer_form.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("New Customer");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                searchCustomer.requestFocus();
            });

            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.DELETE) {
                    stage.close();
                }
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkoutAction(ActionEvent actionEvent) {
        try {
            double total = Double.parseDouble(totalLbl.getText());

            double cash = Double.parseDouble(cashTxt.getText());

            if (!Regex.isTextFieldValid(TextField.PRICE, cashTxt.getText())) {
                new Alert(Alert.AlertType.ERROR, "Invalid input for cash").showAndWait();
                cashTxt.requestFocus();
                return;
            }

            if (cash >= total) {
                List<ItemCartTm> cartItems = new ArrayList<>();

                for (ItemCartTm cartItem : cartTable.getItems()) {
                    cartItems.add(cartItem);
                }

                String admin = usrLbl.getText();
                int contact = Integer.parseInt(searchCustomer.getText());

                PlaceOrderRepo.checkOut(cartItems, contact, totalLbl.getText(), admin, dateLbl.getText());
                //get orderId
                String orderId = OrderRepo.getOrderId();

                cartTable.getItems().clear();


                JasperDesign jasperDesign = JRXmlLoader.load("/report/Bill.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                Map<String, Object> data = new HashMap<>();
                data.put("Date", dateLbl.getText());
                data.put("OrderId", orderId);
                data.put("UserName", usrLbl.getText());
                data.put("Time", time);
                data.put("SubTotal", totalLbl.getText());
                data.put("Balance", balanceLbl.getText());
                data.put("Cash", cashTxt.getText());

                JasperPrint jasperPrint =
                        JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());

                PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();


                PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
                PrintService specificPrinter = null;
                for (PrintService service : services) {
                    if (service.getName().equals("PRT80")) {
                        specificPrinter = service;
                        break;
                    }
                }


                // Create print attributes
                PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
                attributes.add(Sides.DUPLEX);
                attributes.add(PrintQuality.HIGH);

                JRPrintServiceExporter exporter = new JRPrintServiceExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

                SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
                configuration.setPrintRequestAttributeSet(attributes);
                configuration.setPrintService(specificPrinter);
                configuration.setDisplayPageDialog(false);
                configuration.setDisplayPrintDialog(false);

                exporter.setConfiguration(configuration);

                try {
                    exporter.exportReport();
                } catch (JRException e) {
                    e.printStackTrace();
                }

                new Alert(Alert.AlertType.CONFIRMATION, "Check Out Successful").showAndWait();
                searchCustomer.setText("");
                itemSearchBar.setText("");
                custLbl.setText("");
                totalLbl.setText("");
                balanceLbl.setText("");
                searchCustomer.requestFocus();
                cashTxt.clear();

            } else {
                new Alert(Alert.AlertType.ERROR, "Cash amount is less than total").showAndWait();
                cashTxt.requestFocus();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input for cash").showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOnAction(ActionEvent actionEvent) {
        try {
            String itemId = idLbl.getText();
            String description = descLbl.getText();
            double retailPrice = Double.parseDouble(priceLbl.getText());
            double discount = Double.parseDouble(discLbl.getText());
            int qty = Integer.parseInt(qtyTxt.getText());

            if (!Regex.isTextFieldValid(TextField.QTY, qtyTxt.getText())) {
                new Alert(Alert.AlertType.ERROR, "Invalid input for quantity").showAndWait();
                qtyTxt.requestFocus();
                return;
            }

            for (ItemCartTm itemCartTm : cartTable.getItems()) {
                if (itemCartTm.getItemId().equals(itemId)) {
                    itemCartTm.setQty(itemCartTm.getQty() + qty);
                    itemCartTm.setTotal(itemCartTm.getQty() * itemCartTm.getRetailPrice());
                    cartTable.refresh();
                    return;
                }
            }

            double discountAmount = (retailPrice * discount) / 100;
            double discountedPrice = retailPrice - discountAmount;
            double total = discountedPrice * qty;
            ImageView removeIcon = new ImageView(new Image(getClass().getResourceAsStream("/icon/Untitled design (47).png")));
            removeIcon.setFitWidth(20);
            removeIcon.setFitHeight(20);

            JFXButton remove = new JFXButton();
            remove.setGraphic(removeIcon);
            remove.setOnAction(e -> {
                removeRow();
            });

            ItemCartTm itemCartTm = new ItemCartTm(itemId, description, qty, retailPrice, discount, total, remove);
            cartTable.getItems().add(itemCartTm);
            cartTable.refresh();

            double netTotal = 0;
            for (ItemCartTm cartItem : cartTable.getItems()) {
                netTotal += cartItem.getTotal();
            }
            totalLbl.setText(String.valueOf(netTotal));
            clear();

            itemSearchBar.clear();

            itemSearchBar.requestFocus();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onQty(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.QTY, qtyTxt);
    }

    public void onCustomer(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.CONTACT, searchCustomer);
    }

    public void onCash(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PRICE, cashTxt);
    }
}
