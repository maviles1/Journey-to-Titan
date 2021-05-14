import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;
import titan.interfaces.StateInterface;
import titan.ui.View;
import titan.ui.Window;

import java.util.ArrayList;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 8600;
    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    public static final double[] LAUNCH_VELOCITY = {40289.2995, -41570.9400, -587.3099};
    public static final double[] LAUNCH_POSITION = {6371000.0, 1.0, 1.0};
    Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        String url = getClass().getResource("solar_system_data-2020_04_01.txt").getPath();
        SpaceObjectBuilder builder = new SpaceObjectBuilder(url);

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

        Vector3d vel = new Vector3d(LAUNCH_VELOCITY[0], LAUNCH_VELOCITY[1], LAUNCH_VELOCITY[2]);
        Vector3d pos = new Vector3d(LAUNCH_POSITION[0], LAUNCH_POSITION[1], LAUNCH_POSITION[2]);
        sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        Renderer renderer = new Renderer(view, sim.getStates());
        window.attachRenderer(renderer);

        //renderer.start();

        runPolySim(pos, vel);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void runPolySim(Vector3d pos, Vector3d vel) {
        ScrollPane pane = new ScrollPane();
        pane.setPannable(true);
//        pane.setHvalue(1/3.0);
//        pane.setVvalue(1/1.2);
        pane.setHvalue(0.5);
        pane.setVvalue(0.5);

        ProbeSim ogSim = new ProbeSim();
        ogSim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ProbeSim eulerSim = new ProbeSim(new Solver(new EulerSolver()));
        eulerSim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ProbeSim verletSim = new ProbeSim(new Solver(new VerletSolver()));
        verletSim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ProbeSim v2Sim = new ProbeSim(new Solver(VerletSolver.getV2()));
        v2Sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ProbeSim rkSim = new ProbeSim(new Solver(new RKSolver()));
        rkSim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ProbeSim rk2 = new ProbeSim(new Solver(RKSolver.getRK2()));
        rk2.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);

        ArrayList<StateInterface[]> simStates = new ArrayList<>();
        //simStates.add(ogSim.getStates());
        simStates.add(eulerSim.getStates());
        simStates.add(verletSim.getStates());
        simStates.add(v2Sim.getStates());
        simStates.add((rkSim.getStates()));
        simStates.add((rk2.getStates()));

        PolySim polySim = new PolySim(simStates);
//        polySim.setProbeNames(new String[]{"Euler", "Verlet", "RK"});
        polySim.setProbeNames(new String[]{"Euler", "Verlet", "V2", "RK", "RK2"});

        pane.setContent(polySim.getCanvas());
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Z) {
                polySim.scale(1.05);
            } else if (event.getCode() == KeyCode.X) {
                polySim.scale(0.95);
            } else if (event.getCode() == KeyCode.EQUALS) {
                polySim.simSpeed(1);
            } else if (event.getCode() == KeyCode.MINUS) {
                polySim.simSpeed(-1);
            }
        });

        stage.setScene(new Scene(pane, 1200, 800));

        polySim.start();
    }
}
