import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import titan.*;
import java.lang.Math;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 500;


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
       // primaryStage.show();

        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();

        //starting position and velocity vector for probe launch
        //generate a random vector
        double a = (Math.random()*1000)-500;
        double b = (Math.random()*1000)-500;
        double c = (Math.random()*1000)-500;

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


        double dist = spaceObjects.get(0).getPosition().dist(spaceObjects.get(3).getPosition());
        Vector3d start = spaceObjects.get(0).getPosition().sub(spaceObjects.get(3).getPosition());
        Vector3d startingPoint = (start.mul(1/dist)).mul((dist+6371000));

        for(int i=0;i<50;i++){
          //  Vector3d vel = new Vector3d(5000, -35000, -400);
            //vel.mul((1/vel.norm()) *60);
            sim.trajectory(startingPoint, initial_probe_velocity,31556926, 1000);
            System.out.println(i);
        }
        System.out.println("END");

        Renderer renderer = new Renderer(canvas, sim.getStates());

        renderer.start();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
