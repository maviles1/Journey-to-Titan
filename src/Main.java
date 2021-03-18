import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(1700, 1000);
        Canvas canvas = new Canvas(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
        View view = new View(canvas);
        anchorPane.getChildren().add(view);

        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        double[] ts = new double[]{0, 57869, 31556926};
       // System.out.println(Arrays.toString(sim.trajectory(new Vector3d(1, 1, 1), new Vector3d(60, 0, 0),31556926, 1000)));
     //   sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000);
        //sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),ts);

        //System.out.println(sim.simulation()[sim.simulation().length-1]);



//        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000000);
        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(1, 1, 0),31556926, 500);

        Renderer renderer = new Renderer(canvas, sim.getStates());

        renderer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
