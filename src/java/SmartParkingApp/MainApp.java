package SmartParkingApp;

import SmartParkingApp.Application.UseCase.UserManagement.IUserManagementServices;
import SmartParkingApp.Application.UseCase.UserManagement.UserManagementServices;
import SmartParkingApp.Application.UseCase.VehicleManagement.IVehicleManagementServices;
import SmartParkingApp.Application.UseCase.VehicleManagement.VehicleManagementServices;
import SmartParkingApp.Domain.User;
import SmartParkingApp.Utilities.Constants;
import SmartParkingApp.Utilities.HibernateUtils;
import com.gluonhq.ignite.spring.SpringContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class MainApp extends Application {
//    private SpringContext context = new SpringContext(this, () -> Arrays.asList(MainApp.class.getPackage().getName()));
    private SpringContext context = new SpringContext(this, () -> Arrays.asList(MainApp.class.getPackage().getName()));
    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Database
        Session session = HibernateUtils.getSessionFactory().openSession();
//        Session session = SessionSingleton.getInstance();
        session.getTransaction().begin();
        // Test
        Query query = session.createQuery("from User");
//        query.setParameter("id", 1);
        List<User> list = query.list();
        for (User user : list) {
            System.out.println(user.getFullname());
        }
        // JavaFX
        context.init();
        fxmlLoader.setLocation(getClass().getResource(Constants.FXML_ROOT));
        Parent root = fxmlLoader.load();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.FXML_ROOT));
//        Parent root = (Parent)fxmlLoader.load();
        // Show stage
        primaryStage.getIcons().add(new Image(Constants.LOGO_DIR));
        primaryStage.setTitle(Constants.APPLICATION_TITLE);
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));

        // Do somethings when exit
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application Closed!!!");
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }
}

@Configuration
class SpringConfig {
    @Bean
    public IUserManagementServices provideUserManagementService() {
        return new UserManagementServices();
    }
    @Bean
    public IVehicleManagementServices provideVehicleManagementService() {
        return new VehicleManagementServices();
    }
}