import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import titan.*;
import titan.interfaces.Vector3dInterface;
import titan.ui.View;
import titan.ui.Window;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 60*60*24;
    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    public static final Vector3d LAUNCH_VELOCITY = new Vector3d(40289.2995, -41570.9400, -587.3099);        //40289.2995, -41570.9400, -587.3099
    public static final Vector3d LAUNCH_POSITION = new Vector3d(6371e3, 1, 1);          //6371000.0, 1.0, 1.0


    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = getClass().getResource("solar_system_data-2020_04_01.txt").getPath();
        SpaceObjectBuilder builder = new SpaceObjectBuilder(url);
        System.out.println(url);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/window.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("../main/fxml/window.fxml"));

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        View view = new View(canvas);
        Window window = new Window(view);

        loader.setController(window);
        loader.setRoot(window);
        Parent root = loader.load();
        window.init();

        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("titan.png"));
        primaryStage.show();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

        //starting position and velocity vector for probe launch

        //generate the position and velocity vector
        double[] radius = new double[]{700000, 2439.7, 6051.8, 6371, 1737.1, 3389.5, 69911, 58232, 2575.7, 25362, 2462.2, 10};
        for (int j = 0; j < spaceObjects.size(); j++) {
            spaceObjects.get(j).setRadius(radius[j]);
        }

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);

       //the probe trajectory
        sim.trajectory(LAUNCH_POSITION, LAUNCH_VELOCITY, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);


        Renderer renderer = new Renderer(view, sim.getStates());
        window.attachRenderer(renderer);

        renderer.start();
    }


    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
