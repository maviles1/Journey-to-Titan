import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;

import java.io.File;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 500;


    @Override
    public void start(Stage primaryStage) throws Exception{
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");

        FXMLLoader loader = new FXMLLoader(new File("src/window.fxml").toURI().toURL());


        //AnchorPane anchorPane = new AnchorPane();
        //anchorPane.setPrefSize(800, 600);
        Canvas canvas = new Canvas(1600, 1200);
        View view = new View(canvas);
        //anchorPane.getChildren().add(view);
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
        //generate a random vector
        double a = (Math.random()*100)-50;
        double b = (Math.random()*100)-50;
        double c = (Math.random()*100)-50;

        //random_vector is not null
        while (a==0 && b==0 && c==0)
        {
            a=(Math.random()*100)-50;
        }

        Vector3d random_vector = new Vector3d(a,b,c);

        //normalize the vector to generate unit vector
        Vector3d random_unit= random_vector.mul(1/random_vector.norm());

        //generate the position and velocity vector
        Planet earth = (Planet) spaceObjects.get(3);
        Vector3d initial_probe_position = random_unit.mul(6371000);
        Vector3d initial_probe_velocity = random_unit.mul(PROBE_SPEED);

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        double[] ts = new double[]{0, 57869, YEAR_IN_SECONDS};

       // System.out.println(Arrays.toString(sim.trajectory(new Vector3d(1, 1, 1), new Vector3d(60, 0, 0),31556926, 1000)));
     //   sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000);
        //sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),ts);

        //System.out.println(sim.simulation()[sim.simulation().length-1]);

//        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000000);
//        sim.trajectory(initial_probe_position, initial_probe_velocity, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);
//        sim.trajectory(new Vector3d(6371000, 1, 1), initial_probe_velocity, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);
//        sim.trajectory(initial_probe_position, new Vector3d(60, 0, 0), YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        //Vector3d vel = new Vector3d(5000, -35000, -400);
        //Vector3d vel = new Vector3d(31336.554258164942,-31485.0256115705,40332.53685876744);
//        Vector3d vel = new Vector3d(31161.479822313646,-31236.70426509767,40659.937062646415);
        Vector3d vel = new Vector3d(40188.42496797729,-41575.62743325709,-16011.17428935992);
        Vector3d pos = new Vector3d(6371000.0,1.0,1.0);
//        Vector3d pos = vel;
//        pos = pos.mul(1/pos.norm());
//        pos = pos.mul(6371000);
        //vel.mul((1/vel.norm()) *60);
        //new Vector3d(6371000, 1, 1)
        sim.trajectory(pos, vel,31556926, 1000);

        Renderer renderer = new Renderer(view, sim.getStates());
        window.attachRenderer(renderer);

        renderer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
