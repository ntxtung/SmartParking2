package SmartParkingApp.Controllers.Helper;

import com.jfoenix.controls.JFXButton;

public class EnterOutBtn extends JFXButton {
    public void changeToYellow(){
        this.setText("Check");
        this.setStyle("-fx-background-color:  #F57C00;");
    }

    public void changeToGreen(){
        this.setText("Enter");
        this.setStyle("-fx-background-color:  #4CAF50;");
    }

    public void changeToRed() {
        this.setText("Out");
        this.setStyle("-fx-background-color:  #D32F2F;");
    }
}
