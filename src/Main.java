import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import titan.*;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 500;


    @Override
    public void start(Stage primaryStage) throws Exception {
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");

        FXMLLoader loader = new FXMLLoader(new File("src/window.fxml").toURI().toURL());

        Canvas canvas = new Canvas(1600, 1200);
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

        Vector3d vel = new Vector3d(40188.42496797729, -41575.62743325709, -16011.17428935992);
        Vector3d pos = new Vector3d(6371000.0, 1.0, 1.0);
        sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        Renderer renderer = new Renderer(view, sim.getStates());
        window.attachRenderer(renderer);

        renderer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
