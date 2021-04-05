import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import titan.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;

public class Main extends Application implements EventHandler<KeyEvent> {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 1000;
    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 1200;
    public Camera cam;
    public StateInterface[] states;
    int counter = 0;
    Group group;
    ArrayList<SpaceObject> planets;
    ArrayList<Shape3D> shapes;
    ArrayList<Node> names;
    PointLight l;
    double prevY;
    double prevX;
    Point zoomAxis = new Point(0,0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        //SpaceObjectBuilder builder = new SpaceObjectBuilder("src/solar_system_data-2020_04_01.txt");
        SpaceObjectBuilder builder = new SpaceObjectBuilder("solar_system_data-2020_04_01.txt");
        planets = new ArrayList<>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        Group group = new Group();
        this.group = group;
        shapes = new ArrayList<>();
        names = new ArrayList<>();
        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();
        double[] radius = new double[]{7000, 2439.7, 6051.8, 6371, 1737.1, 3389.5, 69911, 58232, 2575.7, 25362, 2462.2, 10000};
        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        Vector3d vel = new Vector3d(40289.2995, -41570.9400, -587.3099);
        Vector3d pos = new Vector3d(6371000.0, 1.0, 1.0);
        sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);
        states = sim.getStates();
        PointLight light = new PointLight();
        light.setColor(Color.YELLOW);
        light.getTransforms().add(new Translate(spaceObjects.get(0).getPosition().getX(),spaceObjects.get(0).getPosition().getY(),spaceObjects.get(0).getPosition().getZ()));
//        group.getChildren().add(light);
        l = light;
        for (int j = 0; j < spaceObjects.size(); j++) {
            SpaceObject o = spaceObjects.get(j);
            spaceObjects.get(j).setRadius(radius[j]);
            Sphere sphere = new Sphere(spaceObjects.get(j).getRadius()/1000);
            sphere.translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(o.getPosition().getX()));
            sphere.translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(o.getPosition().getY()));
            sphere.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ()));
//            sphere.translateZProperty().set(CANVAS_WIDTH/2);
            group.getChildren().add(sphere);
            Text name = new Text(State.names.get(j));
            name.translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(o.getPosition().getX()));
            name.translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(o.getPosition().getY()));
            name.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ()));
            group.getChildren().add(name);
            names.add(name);
            planets.add(o);
            shapes.add(sphere);
        }

        for (int i = 0; i < spaceObjects.size(); i++){
            String imgSrc = "textures/8k_sun.jpeg";
            switch (State.names.get(i)){
                case "Sun":
                    imgSrc = "textures/2k_sun.jpeg";
                    break;
                case "Mercury":
                    imgSrc = "textures/2k_mercury.jpeg";
                    break;
                case "Venus":
                    imgSrc = "textures/2k_venus_atmosphere.jpeg";
                    break;
                case "Earth":
                    imgSrc = "textures/2k_earth_daymap.jpeg";
                    break;
                case "Moon":
//                    imgSrc = "textures/8k_moon.jpeg";
                    break;
                case "Mars":
                    imgSrc = "textures/2k_mars.jpeg";
                    break;
                case "Jupiter":
                    imgSrc = "textures/2k_jupiter.jpeg";
                    break;
                case "Saturn":
                    imgSrc = "textures/2k_saturn.jpeg";
                    break;
                case "Uranus":
                    imgSrc = "textures/2k_uranus.jpeg";
                    break;
                case "Neptune":
                    imgSrc = "textures/2k_neptune.jpeg";
                    break;
                case "Probe":
                    imgSrc = "textures/2k_uranus.jpeg";
                    break;
                case "Titan":
                    imgSrc = "textures/2k_neptune.jpeg";
                    break;
            }
            PhongMaterial m = new PhongMaterial();
            m.setDiffuseMap(new Image(imgSrc));
            shapes.get(i).setMaterial(m);
            PhongMaterial buzz = new PhongMaterial();
            buzz.setDiffuseMap(new Image("textures/Buzz_Lightyear_ts4.png.png"));
            shapes.get(shapes.size() - 1).setMaterial(buzz);
        }
        Camera cam = new PerspectiveCamera();
        this.cam = cam;
//        cam.translateZProperty().set(-160000);
//        Line line = new Line((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(spaceObjects.get(0).getPosition().getX()), (CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(spaceObjects.get(0).getPosition().getY()),(CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(spaceObjects.get(1).getPosition().getX()),(CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(spaceObjects.get(1).getPosition().getY()));
//        line.setStrokeWidth(900);
//        group.getChildren().add(line);
        Scene scene = new Scene(group,CANVAS_WIDTH,CANVAS_HEIGHT);
        Image background = new Image("textures/2k_stars_milky_way.jpeg");
        BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

//        v.setPreserveRatio(true);
//        group.getChildren().add(v);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                prevY = mouseEvent.getY();
                prevX = mouseEvent.getX();
            }
        });
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                zoomAxis.setLocation((int) mouseEvent.getX(),(int) mouseEvent.getY());
;            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Transform r = new Rotate();
                cam.setRotationAxis(Rotate.X_AXIS);
                cam.rotateProperty().set(cam.getRotate() + 0.5);
            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                cam.getTransforms().add(new Translate(0,0,scrollEvent.getDeltaY()));
            }
        });
        scene.setOnKeyPressed(this);
        scene.setCamera(cam);
        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("titan.png"));
        primaryStage.show();


        //starting position and velocity vector for probe launch

        //generate the position andG velocity vector



//        Renderer renderer = new Renderer(view, sim.getStates());
//        window.attachRenderer(renderer);

//        renderer.start();
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString() == "D"){
//            this.cam.translateXProperty().set(this.cam.getTranslateX() + 10);
            cam.getTransforms().add(new Translate(10,0,0));
        }
        if (keyEvent.getCode().toString() == "W"){
            cam.getTransforms().add(new Translate(0,-10,0));
        }
        if (keyEvent.getCode().toString() == "S"){
            cam.getTransforms().add(new Translate(0,10,0));
        }
        if (keyEvent.getCode().toString() == "A"){
            cam.getTransforms().add(new Translate(-10,0,0));
        }
        if (keyEvent.getCode().toString() == "UP"){
            cam.getTransforms().add(new Translate(zoomAxis.getX() - cam.getLayoutX(),zoomAxis.getY() - cam.getLayoutY(),10));
        }
        if (keyEvent.getCode().toString() == "DOWN"){
            cam.getTransforms().add(new Translate(0,0,-10));
        }
        if (keyEvent.getCode().toString() == "Q"){
            Transform trans = new Rotate(1, Rotate.X_AXIS);
            cam.getTransforms().add(trans);
        }
        if (keyEvent.getCode().toString() == "E"){
            Transform trans = new Rotate(-1, Rotate.X_AXIS);
            cam.getTransforms().add(trans);
        }
        if (keyEvent.getCode().toString() == "R"){
            Transform trans = new Rotate(1, Rotate.Y_AXIS);
            cam.getTransforms().add(trans);
        }
        if (keyEvent.getCode().toString() == "T"){
            Transform trans = new Rotate(-1, Rotate.Y_AXIS);
            cam.getTransforms().add(trans);
        }
        if (keyEvent.getCode().toString() == "SPACE"){
            Vector3d probe = states[counter].getPositions()[states[counter].getPositions().length - 1];
            for(int i = 0; i < shapes.size(); i++){
                shapes.get(i).setRotationAxis(Rotate.X_AXIS);
                shapes.get(i).rotateProperty().set(shapes.get(i).getRotate() + 0.5);
                shapes.get(i).translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()));
                shapes.get(i).translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()));
                shapes.get(i).translateZProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()));
                names.get(i).translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()));
                names.get(i).translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()));
                names.get(i).translateZProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()));
            }


//            cam.translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getX()));
//            cam.translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getY()));
//            cam.translateZProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getZ()));

            counter+= 100;
        }
    }
}
