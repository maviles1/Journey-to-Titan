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
    public void start(Stage primaryStage) throws Exception {

        SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(1700, 1000);
        Canvas canvas = new Canvas(anchorPane.getPrefWidth(), anchorPane.getPrefHeight());
        View view = new View(canvas);
        anchorPane.getChildren().add(view);

        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(new Scene(anchorPane));
        primaryStage.show();
        //initialiseCanvas(canvas);
        ArrayList<SpaceObject> planets = new ArrayList<>();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

        //create positions and velocities arrays to represent the state
        Vector3d[] positions = new Vector3d[spaceObjects.size() + 1];
        Vector3d[] velocities = new Vector3d[spaceObjects.size() + 1];
        double[] mass = new double[spaceObjects.size() + 1];
        int i = 0;
        for (SpaceObject spaceObject : spaceObjects) {
            positions[i] = spaceObject.getPosition();
            velocities[i] = spaceObject.getVelocity();
            mass[i++] = spaceObject.getMass();
        }
        mass[spaceObjects.size() - 1] = 15000.0;
        positions[positions.length -1 ] = new Vector3d(-3.615638921529161e+10 + 10000,-2.167633037046744e+11 + 10000,3.687670305939779e+09 + 10000);
        velocities[velocities.length -1 ] = new Vector3d(1000,1000,1000);

        //create the initial state
        double [] radius = new double[]{700000,2439.7,6051.8,6371,1737.1,3389.5,69911,58232,2575.7,25362,2462.2,10};
        State state = new State(positions, velocities, 0);
        State.setMass(mass);
        State.setNames();
        State.setRadius(radius);

//        System.out.println(state.toString());
        ODEFunction f = new ODEFunction();

        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
//         System.out.println(Arrays.toString(sim.trajectory(new Vector3d(1, 1, 1), new Vector3d(60, 0, 0),31556926, 1000)));
        sim.trajectory(new Vector3d(1, 1, 5), new Vector3d(60, 60, 0),31556926, 1000);

        double[] ts = new double[]{0, 31556926};
        Solver x = new Solver();
        double tf = 31556926;
        StateInterface[] s1 = x.solve(f, state, tf, 100000);


        Renderer renderer = new Renderer(canvas, planets, state);
        int count = 0;

            ODEFunction func = new ODEFunction();
            double[] tss = new double[]{0, 31556926};
            Solver solver = new Solver();
//        StateInterface[] s = solver.solve(func, state, ts);
            //   System.out.println(s[1]);
            double d = 31556926;
            StateInterface[] s2 = solver.solve(func, state, d, 100000);
//            System.out.println("ASDASDASDASDASDASDASDASD: " + s2[count]);
//            state = (State) s2[count];
            count++;
//
//         GeneticAlgorithm ga = new GeneticAlgorithm(positions[0].add(new Vector3d(1000,1000,1000)),state);
//         ga.calculateTrajectory();

        renderer.start();
    }

    private void initialiseCanvas(Canvas canvas) {
        canvas.widthProperty().bind(((AnchorPane) canvas.getParent()).widthProperty());
        canvas.heightProperty().bind(((AnchorPane) canvas.getParent()).heightProperty());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
