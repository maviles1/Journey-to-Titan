import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.ProbeSimulator;
import titan.SpaceObject;
import titan.SpaceObjectBuilder;
import titan.Vector3d;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new GUI3D().start(primaryStage);
        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("titan.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}