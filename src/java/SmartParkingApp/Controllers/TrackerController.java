package SmartParkingApp.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class TrackerController {
    @FXML
    private ImageView imgFont;

    @FXML
    private ImageView imgBack;

    @FXML
    private ImageView imgPlate;

    @FXML
    private ImageView imgCamFont;

    @FXML
    private ImageView imgCamBack;

    @FXML
    private ImageView imgCamPlate;

    @FXML
    private Label lbl_checkInTime;

    @FXML
    private Label lbl_checkOutTime;

    @FXML
    private Label lbl_parkingDuration;

    @FXML
    private Label lbl_emotionVal;

    @FXML
    private Label lbl_parkingFee;

    @FXML
    private JFXTextField txtPlateNumber;

    @FXML
    private JFXTextField txtRFID;

    @FXML
    private JFXButton enterOutBtn;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    void cancelSession(ActionEvent event) {

    }

    @FXML
    void enterOutBtn_onAction(ActionEvent event) {

    }

    @FXML
    void onConfig(ActionEvent event) {

    }

    @FXML
    void onEnterRFID(ActionEvent event) {

    }

    @FXML
    void onTest(ActionEvent event) {

    }

    @FXML
    void onTestAll(ActionEvent event) {

    }
}
