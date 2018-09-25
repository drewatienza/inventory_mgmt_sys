import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));
        primaryStage.setTitle("WGU C482 - Inventory Management System");
        primaryStage.setScene(new Scene(root, 1160, 550));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
