package SmartParkingApp.Utilities;
import org.opencv.core.Size;
public final class Constants {
    public final static String  APPLICATION_TITLE = "SMART PARKING V2.0 - HCM-IU - VNU";

    public final static String  LOGO_DIR = "/Images/hcmiulogo.png";
    public final static String  DEFAULT_IMG_DIR = "/Images/default-image.jpg";
    public final static String  DEFAULT_IMG_PLATE_DIR = "/Images/default-image_plate.jpg";

    public final static int     MAX_CAMERA_NUMBER = 10;

    public final static String  FXML_ROOT = "/Components/Root.fxml";
    public final static String  FXML_TRACKER = "/Components/Tracker.fxml";
    public final static String  FXML_TRACKER_CONFIG = "/Components/TrackerConfig.fxml";

    public static final Size    PREDICTION_CHAR_SIZE = new Size(10,10);
    public static final String  TRAIN_CHARS_DIR = ".data/demo/char";
    public static final String  TRAIN_OUTPUT_DIR = "./trainedData.txt";
    public static final Size    PREPROCESSING_RESIZE_SIZE = new Size(800, 600);
    public static final int     THRESHOLD_BLOCK_SIZE = 95;
    public static final int     THRESHOLD_C = 0;
    public static final int     MORPHOLOGY_SIZE = 2;
    public static final int     SZ = 20;

    public static final long    FEE_PER_HOUR = 2000;
}
