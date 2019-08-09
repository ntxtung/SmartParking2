package SmartParkingApp.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import SmartParkingApp.Application.UseCase.UserManagement.IUserManagementServices;
import SmartParkingApp.Application.UseCase.VehicleManagement.IVehicleManagementServices;
import SmartParkingApp.Infrastructure.SoftwareSerial.JSerial;
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

import javax.inject.Inject;

public class RootController implements Initializable {
    @Inject
    IUserManagementServices userManagementServices;
    @Inject
    IVehicleManagementServices vehicleManagementServices;

    @FXML
    private SplitPane splitPane;

    @FXML
    private JFXComboBox<Label> choosePortComboBox;

    @FXML
    private JFXButton btn_rfidConnect;

    @FXML
    private JFXButton btn_rfidStop;

    @FXML
    private JFXButton btn_rfidRefresh;

    @FXML
    private Label lblTime;

    private ArrayList<SerialPort> portNames = new ArrayList<>();
    private JSerial mySerial = new JSerial();
    private TrackerController trackingControllerLeft, trackerControllerRight;
    private Parent trackingFormLeft, trackingFormRight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader leftLoader = new FXMLLoader(MainApp.class.getResource(Constants.FXML_TRACKER));
        FXMLLoader rightLoader = new FXMLLoader(MainApp.class.getResource(Constants.FXML_TRACKER));
        try {
            trackingFormLeft = leftLoader.load();
            trackingControllerLeft = leftLoader.getController();

            trackingFormRight = rightLoader.load();
            trackerControllerRight = rightLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        splitPane.setDividerPosition(0, splitPane.getWidth() / 2);
        splitPane.getItems().add(trackingFormLeft);
        splitPane.getItems().add(trackingFormRight);

//        trackingControllerLeft.setName("Left Tracking");
//        trackerControllerRight.setName("Right Tracking");
    }

    @FXML
    void onRFIDConnectAction(ActionEvent event) {

    }

    @FXML
    void onRFIDRefreshAction(ActionEvent event) {

    }

    @FXML
    void onRFIDStopAction(ActionEvent event) {

    }
    private volatile Boolean portStatus;
    private volatile Boolean cancelled = false;
    private void refreshPortList() {
        choosePortComboBox.getItems().clear();
        portNames = new ArrayList<>(Arrays.asList(SerialPort.getCommPorts()));
        if (portNames.size() > 0) {
            System.out.println("All available ports: ");
            choosePortComboBox.setPromptText("---Choose Port---");
            for (int i = 0; i < portNames.size(); i++) {
                choosePortComboBox.getItems().add(new Label(portNames.get(i).getSystemPortName()));
                System.out.println(portNames.get(i).getSystemPortName());
            }
        } else {
            System.out.println("No available serial port");
            choosePortComboBox.setPromptText("---No Port Available---");
        }
    }

    private Boolean connectPort() {
        mySerial = new JSerial(choosePortComboBox.getValue().getText(), 9600);
        if (mySerial.openConnection()) {
            this.portStatus = true;
        } else {
            this.portStatus = false;
        }
        return portStatus;
    }

    public void closePort() {
        mySerial.closeConnection();
        portStatus = false;
    }

    private void start() {
        Runnable RFIDrunner =
                () -> {
                    System.out.println(cancelled);
                    System.out.println(portStatus);
                    while (!cancelled && portStatus) {
                        String s = "";
                        try {
                            s = mySerial.serialRead();
                        } catch (Exception e) {
                            btn_rfidStop.fire();
                            System.err.println("RFID Disconnected");
                        }
                        if (s.length() > 0) {
//                            System.out.println(s);
                            String arr[] = s.split(" ");
                            if (arr[0].equals("R0")) {
                                if (trackingControllerLeft.getState() == 0) {
                                    Platform.runLater(() -> {
                                        trackingControllerLeft.setTextRFID(arr[1].replace(" ", ""));
                                    });
                                }
                            } else if (arr[0].equals("R1")) {
                                if (trackerControllerRight.getState() == 0) {
                                    Platform.runLater(() -> {
                                        trackerControllerRight.setTextRFID(arr[1].replace(" ", ""));
                                    });
                                }
                            }
                        }
                    }
                };
        Thread RFIDThread = new Thread(RFIDrunner);
        RFIDThread.start();
    }

    private void cancel() {
        cancelled = true;
    }

    private boolean isCancelled() {
        return cancelled;
    }

}
