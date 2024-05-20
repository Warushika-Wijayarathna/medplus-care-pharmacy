package lk.ijse.medpluscarepharmacy.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.jfoenix.controls.JFXButton;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.medpluscarepharmacy.dbConnection.DbConnection;
import lk.ijse.medpluscarepharmacy.model.Item;
import lk.ijse.medpluscarepharmacy.model.TemperatureReading;
import lk.ijse.medpluscarepharmacy.model.Tm.ItemTm;
import lk.ijse.medpluscarepharmacy.repository.ItemRepo;
import lk.ijse.medpluscarepharmacy.repository.TemperatureRepo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import lk.ijse.medpluscarepharmacy.repository.OrderRepo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TempFormController {

    static SerialPort serialPort;
    static int i = 0;
    static double lastTemp = 0;
    public static String currentTemp = "Current Temperature: ";
    public Label lblDaily;
    public Label lblMonthly;
    public Label lblAnnual;
    public static AnchorPane itemPane;
    private AnchorPane temperatureChartPane;
    public TableView<ItemTm> itemTable;
    public TableColumn<?,?> colItemId;
    public AnchorPane rootPane;
    public TableColumn<?,?> colDesc;
    public TableColumn<?,?> colQty;
    public TableColumn<?,?> colWholePrice;
    public TableColumn<?,?> colRetailPrice;
    public TableColumn<?,?> colDiscount;
    public TableColumn<ItemTm, String> colExpDate;
    ObservableList<ItemTm> observableList = FXCollections.observableArrayList();
    private Thread thread;
    private Scanner scanner;

    public static final String ACCOUNT_SID = "ACcc72bee81dd443677a8c0a5747450545";
    public static final String AUTH_TOKEN = "972624e3be9fe4fe13ffb956f5f34d3b";
    private int alertCount = 0;
    public void initialize() {

        setCellValueFactories();
        loadAllItems();
        setSales();
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stopReading();
            }));
            temperatureChartPane = new AnchorPane();

            itemTable.setRowFactory(tv -> new TableRow<ItemTm>() {
                @Override
                protected void updateItem(ItemTm item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null|| empty) {
                        setStyle("");
                        getStyleClass().removeAll("expiring", "understock", "understock-expiring");

                    } else {
                        LocalDate expDate = item.getExpDate();
                        LocalDate now = LocalDate.now();
                        boolean underStock = item.getQty() <= 50;

                        if (expDate.isBefore(now.plusMonths(6)) && underStock) {
                            getStyleClass().removeAll("expiring", "understock");
                            getStyleClass().add("understock-expiring");
                        } else if (expDate.isBefore(now.plusMonths(6))) {
                            getStyleClass().removeAll("understock", "understock-expiring");
                            getStyleClass().add("expiring");
                        } else if (underStock) {
                            getStyleClass().removeAll("expiring", "understock-expiring");
                            getStyleClass().add("understock");
                        } else {
                            getStyleClass().removeAll("expiring", "understock", "understock-expiring");
                        }
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String portName = "/dev/ttyACM0";

        Label label1 = new Label(String.valueOf(currentTemp));
        JFXButton connectBtn = new JFXButton("Connect");
        connectBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        connectBtn.setPrefWidth(107);
        connectBtn.setPrefHeight(44);
        connectBtn.setLayoutX(583);
        connectBtn.setLayoutY(771);


        AnchorPane.setTopAnchor(label1, 10.0);
        AnchorPane.setLeftAnchor(label1, 10.0);

        AnchorPane.setBottomAnchor(connectBtn, 10.0);
        AnchorPane.setLeftAnchor(connectBtn, 10.0);
        rootPane.getChildren().addAll(label1, connectBtn);

        XYSeries series = new XYSeries("Temperature");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Temperature",
                "Time",
                "Temperature",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800,700));

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(chartPanel);

        temperatureChartPane.getChildren().add(swingNode);


        rootPane.getChildren().add(swingNode);

        connectBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (connectBtn.getText().equals("Connect")) {
                    try {
                        serialPort = SerialPort.getCommPort(portName);
                    } catch (SerialPortInvalidPortException e) {
                        new Alert(Alert.AlertType.WARNING,"Device not found").showAndWait();
                    }
                    serialPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 1000, 0);
                    if (serialPort.openPort()) {
                        connectBtn.setText("Disconnect");
                    }


                    thread = new Thread(() -> {
                        scanner = new Scanner(serialPort.getInputStream());
                        while (scanner.hasNextLine()) {
                            try {
                                String line = scanner.nextLine();
                                double number = Double.parseDouble(line);

                                LocalTime currentTime = LocalTime.now();
                                LocalDate currentDate = LocalDate.now();
                                Platform.runLater(() -> {
                                    series.add(i++, number);
                                    lastTemp = number;
                                    label1.setText(currentTemp + String.valueOf(lastTemp));

                                    LocalTime lowerBound8 = LocalTime.of(8, 29, 30);
                                    LocalTime upperBound8 = LocalTime.of(8, 31, 0);

                                    LocalTime lowerBound14 = LocalTime.of(12, 43, 30);
                                    LocalTime upperBound14 = LocalTime.of(12, 44, 0);

                                    LocalTime lowerBound20 = LocalTime.of(22, 59, 30);
                                    LocalTime upperBound20 = LocalTime.of(23, 01, 0);


                                    if ((currentTime.isAfter(lowerBound8) && currentTime.isBefore(upperBound8)) ||
                                            (currentTime.isAfter(lowerBound14) && currentTime.isBefore(upperBound14)) ||
                                            (currentTime.isAfter(lowerBound20) && currentTime.isBefore(upperBound20))) {

                                        try {
                                            TemperatureRepo.addTemperature(new TemperatureReading(currentDate,currentTime,lastTemp));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    System.out.println((currentTemp + String.valueOf(lastTemp)));

                                    VonageClient client = VonageClient.builder().apiKey("26607713").apiSecret("IBs3Pp0FUc4ryErx").build();


                                    if (lastTemp > 25 && alertCount < 1) {
                                        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
                                        Message message = Message.creator(
                                                new com.twilio.type.PhoneNumber("+94701969102"),
                                                new com.twilio.type.PhoneNumber("+18782060917"),
                                                "Temperature Alert!!!Temperature is above 30 degrees"
                                        ).create();
                                        System.out.println(message.getSid());
                                        alertCount++;
                                    }

//                                    if (lastTemp > 30 && alertCount < 3) {
//                                        TextMessage message = new TextMessage("Vonage APIs",
//                                                "94716930518",
//                                                "Temperature Alert!!!Temperature is above 30 degrees"
//                                        );
//
//                                        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);
//
//                                        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
//                                            System.out.println("Message sent successfully.");
//                                            alertCount++;
//                                        } else {
//                                            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
//                                        }
//                                    }

                                });
                            } catch (Exception e) {
                                System.out.println("Corrupt input detected");
                            }
                        }
                        scanner.close();
                    });
                    thread.start();

                } else {
                    serialPort.closePort();
                    connectBtn.setText("Connect");
                    if (scanner != null) {
                        scanner.close();
                    }
                }
            }
        });

    }

    private void setSales() {
        LocalDate today= LocalDate.now();
        Month Month = today.getMonth();
        int year = today.getYear();

        String daily = OrderRepo.getDailySales(today);
        lblDaily.setText(daily);

        String monthly = OrderRepo.getMonthlySales(Month, year);
        lblMonthly.setText(monthly);

        String annual = OrderRepo.getAnnualSales(year);
        lblAnnual.setText(annual);
    }


    public void stopReading() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }

    public void loadAllItems() {
        try {
            itemTable.getItems().clear();
            List<Item> itemList = ItemRepo.getAllItem();
            observableList.clear();
            itemTable.getItems().clear();

            for (Item item : itemList) {
                if (item != null && item.getItemId() != null && !item.getItemId().isEmpty()) {
                    observableList.add(new ItemTm(item.getItemId(), item.getDescription(), item.getQty(), item.getWholeSalePrice(), item.getRetailPrice(), item.getDiscount(), item.getExpDate()));
                }
            }
            itemTable.setItems(observableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactories() {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colWholePrice.setCellValueFactory(new PropertyValueFactory<>("wholeSalePrice"));
        colRetailPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
    }

    public void reportBtnOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/Temperature.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void onRefresh(ActionEvent actionEvent) {
        loadAllItems();
        setSales();
    }

    public void onRKey(KeyEvent keyEvent) {
        if (keyEvent.getCode()== KeyCode.F11){
            onRefresh(new ActionEvent());
        }
    }
}
