import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(800, 600);
        Canvas canvas = new Canvas(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
        View view = new View(canvas);
        anchorPane.getChildren().add(view);

        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
        //initialiseCanvas(canvas);
        ArrayList<SpaceObject> planets = new ArrayList<>();
        Planet earth = new Planet("earth", 5.97219e24, new Vector3d(-1.471922101663588e+11, -2.860995816266412e+10, 8.278183193596080e+06), new Vector3d(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01), 70);
//        Planet mars = new Planet("mars", 400, new Vector3d(400, 75, 87), new Vector3d(0.0, 0.0, 0.0), 80);
//        Planet saturn = new Planet("saturn", 3000, new Vector3d(50, 400, 640), new Vector3d(0.0, 0.0, 0.0), 1000);
//        Planet uranus = new Planet("uranus", 2700, new Vector3d(97, 280, 90), new Vector3d(0.0, 0.0, 0.0), 800);
        Planet sun = new Planet("sun", 1.988500e30, new Vector3d(-6.806783239281648e+08, 1.080005533878725e+09, 6.564012751690170e+06), new Vector3d(-1.420511669610689e+01, -4.954714716629277e+00, 3.994237625449041e-01), 800);
        planets.add(earth);
//        planets.add(mars);
//        planets.add(saturn);
//        planets.add(uranus);
        planets.add(sun);

        //StateInterface state = new State();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

        //create positions and velocities arrays to represent the state
//        Vector3d[] positions = new Vector3d[spaceObjects.size()];
//        Vector3d[] velocities = new Vector3d[spaceObjects.size()];
//        double[] mass = new double[spaceObjects.size()];
//        int i = 0;
//        for (SpaceObject spaceObject : spaceObjects) {
//            positions[i] = spaceObject.getPosition();
//            velocities[i] = spaceObject.getVelocity();
//            mass[i++] = spaceObject.getMass();
//        }
//        for (int i = 0; i < planets.size(); i++){
//            positions[i] = new Vector3d(planets.get(i).getPosition().getX(),planets.get(i).getPosition().getY(),planets.get(i).getPosition().getZ());
//            velocities[i] = new Vector3d(planets.get(i).getPosition().getX(),planets.get(i).getPosition().getY(),planets.get(i).getPosition().getZ());
//
//        }

//        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
//        System.out.println(Arrays.toString(sim.trajectory(new Vector3d(0, 0, 0), new Vector3d(60, 0, 0),31556926, 1000)));
//
//        //create the initial state
//        State state = new State(positions, velocities, 0);
//        State.setMass(mass);
//        State.setNames();

//TODO where did the minuses go????
        //System.out.println(state.toString());
        ODEFunction f = new ODEFunction();

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
       // System.out.println(Arrays.toString(sim.trajectory(new Vector3d(1, 1, 1), new Vector3d(60, 0, 0),31556926, 1000)));
        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000);






        //    State state1 = (State) state.addMul(1, f.call(0 + 1, state));
        // System.out.println(state1.toString());

//
//        double[] ts = new double[]{0, 31556926};
//        Solver x = new Solver();
//     //   StateInterface[] s = x.solve(f, state, ts);
//     //   System.out.println(s[1]);
//     //   System.out.println();
//
//        double tf = 31556926;
//        StateInterface[] s1 = x.solve(f, state, tf, 10000);
//
//        System.out.println(s1[s1.length - 1]);


        Renderer renderer = new Renderer(canvas, planets);

        renderer.start();


    }

    private void initialiseCanvas(Canvas canvas) {
        canvas.widthProperty().bind(((AnchorPane)canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane)canvas.getParent()).heightProperty());
    }


    public static void main(String[] args) {
//        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");
//        Solver solver = new Solver();
//        ODEFunction func = new ODEFunction();
//        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();
//
//        //create positions and velocities arrays to represent the state
//        Vector3d[] positions = new Vector3d[spaceObjects.size()];
//        Vector3d[] velocities = new Vector3d[spaceObjects.size()];
//        double[] mass = new double[spaceObjects.size()];
//        int i = 0;
//        for (SpaceObject spaceObject : spaceObjects) {
//            positions[i] = spaceObject.getPosition();
//            velocities[i] = spaceObject.getVelocity();
//            mass[i++] = spaceObject.getMass();
//        }
//        State state = new State(positions,velocities,0);
//
//        int count = 0;
//        for (int in = 0; in < 3155; in++){
//            StateInterface[] s1 = solver.solve(func, state, 31556926, 100000);
//            System.out.println(s1[count]);
//            state = (State) s1[count];
//            count++;
//        }
//
//
//
//
//




        launch(args);
    }
}
