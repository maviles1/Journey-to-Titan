import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        Planet earth = new Planet("earth", 1000, new Vector3d(50, 100, 300), new Vector3d(0, 0.0, 0.0), 70);
        Planet mars = new Planet("mars", 400, new Vector3d(400, 75, 87), new Vector3d(0.0, 0.0, 0.0), 80);
        Planet saturn = new Planet("saturn", 3000, new Vector3d(50, 400, 640), new Vector3d(0.0, 0.0, 0.0), 1000);
        Planet uranus = new Planet("uranus", 2700, new Vector3d(97, 280, 90), new Vector3d(0.0, 0.0, 0.0), 800);
        Planet sun = new Planet("sun", 27000000, new Vector3d(100, 100, 20), new Vector3d(0.0, 0.0, 0.0), 800);
        planets.add(earth);
        planets.add(mars);
        planets.add(saturn);
        planets.add(uranus);
        planets.add(sun);

        //StateInterface state = new State();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

 //       Probe probe = new Probe("Probe", 15000, new Vector3d(0, 0, 0), new Vector3d(60, 0, 0));
  //      spaceObjects.add(probe);

   /*     for(int step=0; step<spaceObjects.size();step++){
            System.out.println(spaceObjects.get(step).toString());
        }
    */


        //create positions and velocities arrays to represent the state
        Vector3d[] positions = new Vector3d[spaceObjects.size()];
        Vector3d[] velocities = new Vector3d[spaceObjects.size()];
        double[] mass = new double[spaceObjects.size()];
        int i = 0;
        for (SpaceObject spaceObject : spaceObjects) {
            positions[i] = spaceObject.getPosition();
            velocities[i] = spaceObject.getVelocity();
            mass[i++] = spaceObject.getMass();
        }

        //create the initial state
        State state = new State(positions, velocities, 0);
        State.setMass(mass);
        State.setNames();

        System.out.println(state.toString());
        ODEFunction f = new ODEFunction();

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        double[] ts = new double[]{0, 31556926};
       // System.out.println(Arrays.toString(sim.trajectory(new Vector3d(1, 1, 1), new Vector3d(60, 0, 0),31556926, 1000)));
     //   sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000);
        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),ts);

        System.out.println(sim.simulation()[sim.simulation().length-1]);





        //    State state1 = (State) state.addMul(1, f.call(0 + 1, state));
        // System.out.println(state1.toString());


     //   double[] ts = new double[]{0, 31556926};
        Solver x = new Solver();
     //   StateInterface[] s = x.solve(f, state, ts);
     //   System.out.println(s[1]);
     //   System.out.println();

        double tf = 31556926;
        StateInterface[] s1 = x.solve(f, state, tf, 10000);

        System.out.println(s1[s1.length - 1]);


        Renderer renderer = new Renderer(canvas, planets, state);

        renderer.start();

        //starting position and velocity vector for probe launch
        //generate a random vector
        double a = (Math.random()*100)-50;
        double b = (Math.random()*100)-50;
        double c = (Math.random()*100)-50;
        Vector3d random_vector = new Vector3d(a,b,c);

        //normalize the vector to generate unit vector
        Vector3d random_unit= random_vector.mul(1/random_vector.norm());

        //generate the position and velocity vector
        Vector3d initial_probe_position = random_unit.mul(earth.getRadius());
        Vector3d initial_probe_velocity = random_unit.mul(PROBE_SPEED);
    }

    private void initialiseCanvas(Canvas canvas) {
        canvas.widthProperty().bind(((AnchorPane) canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane) canvas.getParent()).heightProperty());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
