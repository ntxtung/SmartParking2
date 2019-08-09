package SmartParkingApp.Controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class TrackerConfigController {
    @FXML
    private JFXComboBox<?> laneRole_combobox;

    @FXML
    private JFXComboBox<?> comboFontCamera;

    @FXML
    private JFXComboBox<?> comboBehindCamera;

    @FXML
    private JFXSlider focusWidth;

    @FXML
    private JFXSlider focusHeight;

    @FXML
    private JFXSlider focusX;

    @FXML
    private JFXSlider focusY;

    @FXML
    void onCancel(ActionEvent event) {

    }

    @FXML
    void onChangeFocusConfigs(MouseEvent event) {

    }

    @FXML
    void onOkay(ActionEvent event) {

    }
}
