package SmartParkingApp.Controllers;

import SmartParkingApp.Application.Helper.RFIDHandler;
import SmartParkingApp.Domain.Vehicle;
import SmartParkingApp.Utilities.Constants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;

public class TrackerController implements Initializable {
    private String name;
    private Image defaultImg = new Image("/" + Constants.DEFAULT_IMG_DIR);
    private Image defaultImg_plate = new Image("/" + Constants.DEFAULT_IMG_PLATE_DIR);
    private Date timeIn;
    private Date timeOut;
    private Vehicle currentVehicle;
//    private EnumEmotion detecedEmotion = EnumEmotion.UNKNOWN;

    /**
     * 0 - Both Enter and Out lane (Default)
     * 1 - Only Enter lane
     * 2 - Only Out lane
     */
    private byte role = 0;
    /**
     * 0 - Waiting
     * 1 - Entering
     * 2 - Outing
     */
    private byte state = 0;

    private ScheduledExecutorService timer;
    private int deviceIndex = 0;
    private VideoCapture capture;
    private Mat frame;
    private Mat m1;

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
    void onConfig(ActionEvent event) {
        FXMLLoader configLoader = new FXMLLoader(getClass().getResource("/" + Constants.FXML_TRACKER_CONFIG));
        try {
            AnchorPane configDialog = configLoader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(configDialog));
            TrackerConfigController configController = configLoader.getController();
            configController.setTrackingController(this);
            stage.setTitle(this.name + " configurate");
            stage.show();
//            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeToWaitingMode();

        txtRFID.textProperty().addListener((observable, oldValue, newValue) -> {
            // Real purpose
            if (RFIDHandler.checkValidRFID(newValue.toUpperCase())) {
                Platform.runLater(() -> {
                    enterOutBtn.setDisable(false);
                    enterOutBtn.fire();
                });
            } else {
                Platform.runLater(() -> {
                    enterOutBtn.setDisable(true);
                });
            }
//            System.out.println("textfield changed from " + oldValue + " to " + newValue);
        });
    }

    public void changeToWaitingMode() {
        state = 0;
        enterOutBtn_changeToYellow();

        resetImg();
        currentVehicle = null;
        Platform.runLater(() -> {
            txtPlateNumber.setDisable(true);
            txtRFID.setDisable(false);
            btn_cancel.setDisable(true);

            txtPlateNumber.setText("");
            txtRFID.setText("");

            lbl_checkInTime.setText("-");
            lbl_checkOutTime.setText("-");
            lbl_parkingDuration.setText("-");
            lbl_emotionVal.setText("-");
            lbl_parkingFee.setText("- VND");

            lbl_emotionVal.setStyle("-fx-text-fill: #212121");
            emotionDetectService.reset();
        });
    }

    public void changeToConfirm_EnterMode() {
        state = 1;
        enterOutBtn_changeToGreen();
        txtPlateNumber.setDisable(false);
        txtRFID.setDisable(true);
        btn_cancel.setDisable(false);

        currentVehicle = new Vehicle(txtRFID.getText(), null, null, null, new Date());

        timeIn = currentVehicle.getTimeIn();
        Platform.runLater(() -> {
//            imgFont.setImage(imgCamFont.getImage());
//            imgBack.setImage(imgCamBack.getImage());
//            imgPlate.setImage(imgCamPlate.getImage());

            lbl_checkInTime.setText(MainProgram.getSimpleDateFormat().format(timeIn));
            lbl_parkingDuration.setText("0");
            lbl_parkingFee.setText("0 VND");

            emotionDetectService.start();
        });
    }

    public void changeToConfirm_OutMode() {
        state = 2;
        enterOutBtn_changeToRed();
        txtPlateNumber.setDisable(true);
        txtRFID.setDisable(true);
        btn_cancel.setDisable(false);

        timeIn = currentVehicle.getTimeIn();
        timeOut = new Date();

        long duration = getDateDiff(currentVehicle.getTimeIn(), timeOut, TimeUnit.HOURS);

        Platform.runLater(() -> {
            imgFont.setImage(currentVehicle.getFrontImg());
            imgBack.setImage(currentVehicle.getBackImg());
            imgPlate.setImage(currentVehicle.getPlateImg());

            txtPlateNumber.setText(currentVehicle.getPlateNumber());
            lbl_checkInTime.setText(MainProgram.getSimpleDateFormat().format(timeIn));
            lbl_checkOutTime.setText(MainProgram.getSimpleDateFormat().format(timeOut));
            lbl_parkingDuration.setText(duration + " Hours");
            lbl_parkingFee.setText(VehicleManage.getInstance().calculateParkingFee(duration) + " VND");

            emotionDetectService.start();
        });
    }

    @FXML
    void enterOutBtn_onAction(ActionEvent event) {
        if (state == 0) {
            System.out.println(this.name + " input RFID: " + txtRFID.getText());
            enterOutBtn.setText("...");
            currentVehicle = VehicleManage.getInstance().getVehicleByRfidFromParkingList(txtRFID.getText());
            if (currentVehicle != null) {
                if (role == 0 || role == 2) {
                    changeToConfirm_OutMode();
                } else {
                    changeToWaitingMode();
                }
            } else {
                if (role == 0 || role == 1) {
                    changeToConfirm_EnterMode();
                } else {
                    changeToWaitingMode();
                }
            }
        } else if (state == 1) {
            currentVehicle.setPlateNumber(txtPlateNumber.getText());
            currentVehicle.setFrontImg(imgFont.getImage());
            currentVehicle.setBackImg(imgBack.getImage());
            currentVehicle.setPlateImg(imgPlate.getImage());
            currentVehicle.setEmotionIn(detecedEmotion);
            VehicleManage.addVehicle(currentVehicle);
            System.out.println(currentVehicle + " IN");
            enterOutBtn.setText("...");
            changeToWaitingMode();
        } else if (state == 2) {
            currentVehicle.changeStatusToLeft();
            currentVehicle.setEmotionOut(detecedEmotion);
            VehicleManage.getInstance().moveVehicleToOtherList(currentVehicle);
            System.out.println(currentVehicle + " OUT");
            enterOutBtn.setText("...");
            changeToWaitingMode();
        }
    }

    private void demoProcessAllImgs() {
        File folder = new File("./img");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());

//		        ImageProcessing.getLicensePlateNumber(listOfFiles[i]);

            }

//		      } else if (listOfFiles[i].isDirectory()) {
//		        System.out.println("Directory " + listOfFiles[i].getName());
//		      }

        }
    }
}
