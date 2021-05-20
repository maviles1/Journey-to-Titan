import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Scene scene = new titan.ui.GUI3D().start(primaryStage);

        Parent startMenu = FXMLLoader.load(getClass().getResource("fxml/launcher.fxml"));
        primaryStage.setTitle("Mission Titan");
//        primaryStage.setScene(scene);
        primaryStage.setScene(new Scene(startMenu));
        primaryStage.getIcons().add(new Image("titan.png"));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}