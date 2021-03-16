import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
//        AnchorPane pane = new AnchorPane();
//        Canvas canvas = new Canvas();
//        pane.getChildren().add(canvas);
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(pane));
//        primaryStage.show();
//        initialiseCanvas(canvas);
//
//

        Planet.planets[0] = new Planet();

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800, 600);
        Canvas canvas = new Canvas(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
        View view = new View(canvas);
        anchorPane.getChildren().add(view);

        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

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
