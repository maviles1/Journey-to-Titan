import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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