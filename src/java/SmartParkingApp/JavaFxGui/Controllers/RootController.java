package SmartParkingApp.JavaFxGui.Controllers;

import SmartParkingApp.JavaFxGui.Helper.DatetimeUpdater;
import SmartParkingApp.JavaFxGui.Helper.RfidClient;
import SmartParkingApp.MainApp;
import SmartParkingApp.Utilities.Constants;
import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    //region Class Properties
    private TrackingController _trackingControllerLeft, _trackingControllerRight;
    private RfidClient _rfidClient = new RfidClient();
    //endregion

    //region JavaFx Properties

    //region Components
    @FXML
    private Label lblTime;
    @FXML
    private SplitPane splitPane;
    //endregion

    //region RFID Client
    @FXML
    private JFXComboBox<Label> choosePortComboBox;
    @FXML
    private JFXButton btn_rfidConnect;
    @FXML
    private JFXButton btn_rfidStop;
    @FXML
    private JFXButton btn_rfidRefresh;
    //endregion
    //endregion

    //region Initialization
    private void _trackingInitialize() {
        FXMLLoader leftLoader = new FXMLLoader(MainApp.class.getResource(Constants.FXML_TRACKER));
        FXMLLoader rightLoader = new FXMLLoader(MainApp.class.getResource(Constants.FXML_TRACKER));
        try {
            // Load FXML
            Parent trackingFormLeft = leftLoader.load();
            Parent trackingFormRight = rightLoader.load();

            // Attach to split pane
            splitPane.setDividerPosition(0, splitPane.getWidth() / 2);
            splitPane.getItems().add(trackingFormLeft);
            splitPane.getItems().add(trackingFormRight);

            // Extract controller
            _trackingControllerLeft = leftLoader.getController();
            _trackingControllerRight = leftLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _dateTimeUpdaterInitialize() {
        DatetimeUpdater watcher = new DatetimeUpdater(lblTime);
        watcher.setDaemon(true);
        watcher.execute();
    }

    private void _rfidClientInitialize() {
        choosePortComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                    btn_rfidConnect.setDisable(false);
                }
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        _trackingInitialize();
        _dateTimeUpdaterInitialize();
        _rfidClientInitialize();
    }
    //endregion

    //region JavaFX Methods

    @FXML
    void onRFIDConnectAction(ActionEvent event) {
        System.out.println(this.getClass().getName() + " >> Go in to methods");
        _rfidClient.setPortDescription(this.getSelectedPort());
        if (_rfidClient.openConnection()) {
            this._fx_StartRfid();
            this.startRfidThread();
        }
    }

    @FXML
    void onRFIDRefreshAction(ActionEvent event) {
        this.refreshPortList();
    }

    @FXML
    void onRFIDStopAction(ActionEvent event) {
        this._rfidClient.closeConnection();
        this._fx_StopRfid();
    }
    //endregion

    //region RFID Client Methods
    public void refreshPortList() {
        choosePortComboBox.getItems().clear();
        SerialPort[] portList = SerialPort.getCommPorts();

        if (portList != null && portList.length > 0) {
            System.out.println(this.getClass().getName() + " >> All available ports: ");
            choosePortComboBox.setPromptText("---Choose Port---");
            for (int i = 0; i < portList.length; i++) {
                choosePortComboBox.getItems().add(new Label(portList[i].getSystemPortName()));
                System.out.println(portList[i].getSystemPortName());
            }
        } else {
            System.out.println(this.getClass().getName() + " >> No available serial port");
            choosePortComboBox.setPromptText("---No Port Available---");
        }
    }

    public boolean connectPort() {
        _rfidClient.setPortDescription(this.getSelectedPort());
        _rfidClient.setConnectionStatus(_rfidClient.openConnection());
        return _rfidClient.getConnectionStatus();
    }

    public void closePort() {
        _rfidClient.closeConnection();
    }

    private void startRfidThread() {
        Runnable RFIDrunner =
                () -> {
                    var cancelled = _rfidClient.getCancelled();
                    var connectionStatus = _rfidClient.getConnectionStatus();
                    System.out.println("RootController >> Cancelled: " + cancelled);
                    System.out.println("RootController >> ConnectionStatus: " + connectionStatus);

                    while (!_rfidClient.getCancelled() && _rfidClient.getConnectionStatus()) {
                        String s = "";
                        try {
                            s = _rfidClient.serialRead();
                        } catch (Exception e) {
                            btn_rfidStop.fire();
                            System.err.println(this.getClass().getName() + " >> RFID Disconnected");
                        }
                        if (s.length() > 0) {
                            System.out.println(this.getClass().getName() + " >> Input: " + s);
                            String arr[] = s.split(" ");

                            var router = arr[0];
                            var value = arr[1].replace(" ", "");
//                            System.out.println("RootController >> RFID Router: " + router);
//                            System.out.println("RootControlelr >> RFID Value: " + value);
//                            if (router.equals("R0")) {
//                                if (_trackingControllerLeft.getState() == 0) {
//                                    Platform.runLater(() -> {
//                                        _trackingControllerLeft.setTextRFID(value);
//                                    });
//                                }
//                            } else if (router.equals("R1")) {
//                                if (_trackingControllerRight.getState() == 0) {
//                                    Platform.runLater(() -> {
//                                        _trackingControllerRight.setTextRFID(value);
//                                    });
//                                }
//                            }
                        }
                    }
                };
        Thread RFIDThread = new Thread(RFIDrunner);
        RFIDThread.start();
    }
    //endregion

    //region Help Methods
    private String getSelectedPort() {
        return this.choosePortComboBox.getValue().getText();
    }

    private void _fx_StartRfid() {
        Platform.runLater(() -> {
            btn_rfidConnect.setDisable(true);
            btn_rfidStop.setDisable(false);
        });
    }

    private void _fx_StopRfid() {
        Platform.runLater(() -> {
            btn_rfidConnect.setDisable(false);
            btn_rfidStop.setDisable(true);
        });
    }
    //endregion


}
