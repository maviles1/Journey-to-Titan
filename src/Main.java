import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        AnchorPane pane = new AnchorPane();
        Canvas canvas = new Canvas();
        pane.getChildren().add(canvas);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
        initialiseCanvas(canvas);

        Renderer renderer = new Renderer(canvas);
        renderer.start();


    }

    private void initialiseCanvas(Canvas canvas) {
        canvas.widthProperty().bind(((AnchorPane)canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane)canvas.getParent()).heightProperty());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
