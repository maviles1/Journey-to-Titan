import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import titan.*;
import titan.Vector3d;
import titan.interfaces.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application implements EventHandler<KeyEvent> {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double STEP_SIZE_TRAJECTORY = 24*60*60;
    public static final double YEAR_IN_SECONDS = 365.25*STEP_SIZE_TRAJECTORY;
    public static final int CANVAS_WIDTH = 1600;
    public static final int CANVAS_HEIGHT = 1000;
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
    Vector3d sunPos;
    Vector3d probePos;
    double anchorX;
    double anchorY;
    double anchorAngleX = 0;
    double anchorAngleY = 0;
    final DoubleProperty angleX = new SimpleDoubleProperty(0);
    final DoubleProperty angleY = new SimpleDoubleProperty(0);
    Point zoomAxis = new Point(0,0);
    Group superGroup;
    double speed;
    Button camLockToggle;
    Media song;
    Text probeMass;
    Vector3d camLock;
    ProbeSimulator sim;


    private void initMouseControl(Group group, Scene scene){
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                anchorX = mouseEvent.getX();
                anchorY = mouseEvent.getY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();

            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                angleX.set(anchorAngleX - (anchorY - mouseEvent.getSceneY()));
                angleY.set(anchorAngleY + anchorX - mouseEvent.getSceneX());
            }
        });
    }

    public void drawState(Group group){
        shapes = new ArrayList<>();
        names = new ArrayList<>();
        String url = getClass().getResource("solar_system_data-2020_04_01.txt").getPath();
        SpaceObjectBuilder builder = new SpaceObjectBuilder(url);
        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();
        planets = spaceObjects;
        double[] radius = new double[]{700000, 2439.7, 6051.8, 6371, 1737.1, 3389.5, 69911, 58232, 2575.7, 25362, 2462.2, 10000};
        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        this.sim = sim;
        Vector3d vel = new Vector3d(40289.2995, -41570.9400, -587.3099);
        Vector3d pos = new Vector3d(6371000.0, 1.0, 1.0);
        sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);
        states = sim.getStates();
        CSVReader r = new CSVReader("src/trajectory.csv");
        int counter = 0;
        Vector3d [] probePositions = r.getProbePositions();
        for (StateInterface s: states){
            s.getPositions()[11] = probePositions[counter];
            System.out.println("PROBE" + " "  + probePositions[counter]);
            System.out.println("EARTH: "  + s.getPositions()[3]);
            counter++;
        }
        initLight(group);
        probePos = spaceObjects.get(0).getPosition();
        for (int j = 0; j < spaceObjects.size(); j++) {
            SpaceObject o = spaceObjects.get(j);
            if (State.names.get(j).equals("Probe")){
                Box b = new Box();
                b.setDepth(10);
                b.setWidth(10);
                b.setHeight(10);
                group.getChildren().add(b);
                shapes.add(b);
                b.translateXProperty().set(Renderer.toScreenCoordinates(o.getPosition().getX()) - Renderer.toScreenCoordinates(probePos.getX()));
                b.translateYProperty().set(Renderer.toScreenCoordinates(o.getPosition().getY()) - Renderer.toScreenCoordinates(probePos.getY()));
                b.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ())- Renderer.toScreenCoordinates(probePos.getZ()));
                Text name = new Text(State.names.get(j));
                name.setStroke(Color.WHITE);
                name.setFill(Color.WHITE);
                name.translateXProperty().set(Renderer.toScreenCoordinates(o.getPosition().getX()));
                name.translateYProperty().set(Renderer.toScreenCoordinates(o.getPosition().getY()));
                name.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ()));
                names.add(name);
                group.getChildren().add(name);
            }
            else {
                spaceObjects.get(j).setRadius(radius[j]);
                Sphere sphere = new Sphere(20);
                sphere.translateXProperty().set(Renderer.toScreenCoordinates(o.getPosition().getX()) - Renderer.toScreenCoordinates(probePos.getX()));
                sphere.translateYProperty().set(Renderer.toScreenCoordinates(o.getPosition().getY()) - Renderer.toScreenCoordinates(probePos.getY()));
                sphere.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ()) -  Renderer.toScreenCoordinates(probePos.getZ()));
                group.getChildren().add(sphere);
                Text name = new Text(State.names.get(j));
                name.setStroke(Color.WHITE);
                name.setFill(Color.WHITE);
                name.translateXProperty().set(Renderer.toScreenCoordinates(o.getPosition().getX()) - Renderer.toScreenCoordinates(probePos.getX()));
                name.translateYProperty().set(Renderer.toScreenCoordinates(o.getPosition().getY()) - Renderer.toScreenCoordinates(probePos.getY()));
                name.translateZProperty().set(Renderer.toScreenCoordinates(o.getPosition().getZ())   - Renderer.toScreenCoordinates(probePos.getZ()));
                group.getChildren().add(name);
                names.add(name);
                shapes.add(sphere);
            }
        }
        initTitanPoints(states);
        initMaterials();
    }

    public void initProbeFuelCounter(Group superGroup){
        Text probeFuel = new Text();
        probeFuel.setFill(Color.WHITE);
        superGroup.getChildren().add(probeFuel);
        probeFuel.setX(CANVAS_WIDTH/2 - 100);
        probeFuel.setY(CANVAS_HEIGHT - 30);
        this.probeMass = probeFuel;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        String url = getClass().getResource("solar_system_data-2020_04_01.txt").getPath();
//        SpaceObjectBuilder builder = new SpaceObjectBuilder(url);
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("window.fxml"));
        Group superGroup = new Group();
        this.superGroup = superGroup;
        Group group = new Group();
        group.translateXProperty().set(CANVAS_WIDTH/2);
        group.translateYProperty().set(CANVAS_HEIGHT/2);
        this.group = group;
        superGroup.getChildren().add(new ImageView(new Image("textures/2k_stars_milky_way.jpeg")));
        superGroup.getChildren().add(group);
        initProbeFuelCounter(superGroup);
        initSlider(superGroup);
        drawState(group);
        Sphere dot = new Sphere(10);
        dot.translateXProperty().set(Renderer.toScreenCoordinates(-1.471954341272398E11));
        dot.translateYProperty().set(Renderer.toScreenCoordinates(-2.8604463103590034E10));
        dot.translateZProperty().set(Renderer.toScreenCoordinates(8284741.402211096));
        group.getChildren().add(dot);
        Camera cam = new PerspectiveCamera();
        this.cam = cam;
        cam.setFarClip(1e100);
        Scene scene = new Scene(superGroup,CANVAS_WIDTH,CANVAS_HEIGHT);
        Image background = new Image("textures/2k_stars_milky_way.jpeg");
        BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        initMouseControl(group, scene);
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                group.setTranslateZ(group.getTranslateZ() + scrollEvent.getDeltaY()*(-1));
            }
        });
        Button camLockToggle = new Button("CAM");
        superGroup.getChildren().add(camLockToggle);
        this.camLockToggle = camLockToggle;
        scene.setOnKeyPressed(this);
        scene.setCamera(cam);
//        group.getChildren().add(cam);
        primaryStage.setTitle("Mission Titan");
        primaryStage.setScene(scene);
//        primaryStage.getIcons().add(new Image("titan.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString() == "SPACE"){
            String song = "song.mp3";
            System.out.println("song: " + Paths.get(song).toUri().toString());
//            Media media = new Media(Paths.get(song).toUri().toString());
//            MediaPlayer player = new MediaPlayer(media);
//            player.play();
            Vector3d [] prevPoints = new Vector3d[states[0].getPositions().length];
//            Media media = null;
//            try {
//                media = new Media(getClass().getResource("song.mp3").toURI().toString());
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//            MediaPlayer player = new MediaPlayer(media);
//            player.play();

            speed = 1;
            double [] probeFuel = sim.getProbeMass();

            AnimationTimer p = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    Vector3d probe = states[counter].getPositions()[states[counter].getPositions().length - 1];
                    probePos = probe;
                    camLock = sunPos;
                    camLockToggle.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            camLock = probe;
                            System.out.println("SSS");
                            System.out.println(camLock == probe);
                        }
                    });
                    probeMass.setText(String.valueOf(probeFuel[counter]));
                    for(int i = 0; i < shapes.size(); i++){
                        double screenX = Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()) - Renderer.toScreenCoordinates(camLock.getX());
                        double screenY = Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()) - Renderer.toScreenCoordinates(camLock.getY());
                        double screenZ = Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()) - Renderer.toScreenCoordinates(camLock.getZ());
                        shapes.get(i).setRotationAxis(Rotate.X_AXIS);
                        shapes.get(i).rotateProperty().set(shapes.get(i).getRotate() + 0.5);
                        shapes.get(i).translateXProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()) - Renderer.toScreenCoordinates(camLock.getX()));
                        shapes.get(i).translateYProperty().set( Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()) - Renderer.toScreenCoordinates(camLock.getY()));
                        shapes.get(i).translateZProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()) - Renderer.toScreenCoordinates(camLock.getZ()));
                        Sphere path = new Sphere(1);
                        group.getChildren().add(path);
                        path.translateXProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()) - Renderer.toScreenCoordinates(camLock.getX()));
                        path.translateYProperty().set( Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()) - Renderer.toScreenCoordinates(camLock.getY()));
                        path.translateZProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()) - Renderer.toScreenCoordinates(camLock.getZ()));
                        names.get(i).translateXProperty().set( Renderer.toScreenCoordinates(states[counter].getPositions()[i].getX()) - Renderer.toScreenCoordinates(camLock.getX()));
                        names.get(i).translateYProperty().set( Renderer.toScreenCoordinates(states[counter].getPositions()[i].getY()) - Renderer.toScreenCoordinates(camLock.getY()));
                        names.get(i).translateZProperty().set(Renderer.toScreenCoordinates(states[counter].getPositions()[i].getZ()) - Renderer.toScreenCoordinates(camLock.getZ()));
                    }
//          cam.translateXProperty().set((CANVAS_WIDTH/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getX()));
//          cam.translateYProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getY()));
//            cam.translateZProperty().set((CANVAS_HEIGHT/2) + Renderer.toScreenCoordinates(states[counter].getPositions()[0].getZ()));
                    counter += speed;
//                    if (counter == 21886){
//                        states[counter].getVelocities()[states[counter].getVelocities().length - 1].add(new Vector3d(1000000,1000000,1000000));
//                    }
                }
            };
            p.start();
        }
    }

    public void initMaterials() {
        for (int i = 0; i < planets.size(); i++){
            String imgSrc = "textures/8k_sun.jpeg";
            switch (State.names.get(i)){
                case "Sun":
                    imgSrc = "textures/8k_sun.jpeg";
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
            buzz.setDiffuseMap(new Image("textures/550x755.jpeg"));
            shapes.get(shapes.size() - 1).setMaterial(buzz);
        }
    }

    public void initLight(Group group){
        PointLight light = new PointLight();
        light.setColor(Color.WHITE);
        sunPos = planets.get(0).getPosition();
        light.getTransforms().add(new Translate(planets.get(0).getPosition().getX(),planets.get(0).getPosition().getY(),planets.get(0).getPosition().getZ()));
        l = light;
//        group.getChildren().add(l);
    }

    public void initSlider(Group group){
        Slider slider = new Slider();
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                speed = (double) observableValue.getValue();
            }
        });
        slider.setLayoutX(CANVAS_WIDTH/2);
        slider.setLayoutY(CANVAS_HEIGHT - 30);
        group.getChildren().add(slider);
    }

    public void initTitanPoints(StateInterface [] states){
//        double toAdd = 2775.7;
//        StateInterface titanState = states[21886];
//        Vector3d titanPos = titanState.getPositions()[8];
//        for (int i = 0; i < 1000; i++){
//            Random rand = new Random();
//            Sphere point = new Sphere(10);
//            Vector3d newVec = new Vector3d(0,0,0);
//            newVec.setX(Renderer.toScreenCoordinates(rand.nextDouble()*(toAdd + titanPos.getX())));
//            newVec.setY(Renderer.toScreenCoordinates(rand.nextDouble()*(toAdd + titanPos.getY())));
//            newVec.setZ(Math.sqrt(toAdd*toAdd - newVec.getX()*newVec.getX() - newVec.getY()*newVec.getY()) + Renderer.toScreenCoordinates(titanPos.getZ()));
//            point.translateXProperty().set(newVec.getX());
//            point.translateYProperty().set(newVec.getY());
//            point.translateZProperty().set(newVec.getZ());
////            group.getChildren().add(point);
//        }


    }
}


