package titan.ui;

import javafx.animation.Animation;
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
import java.net.URL;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import titan.*;
import titan.interfaces.StateInterface;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Random;

import java.awt.*;
import java.util.ArrayList;

public class GUI3D {

    public static final double PROBE_SPEED = 600000; //initial probe speed(scalar) relative to earth
    public static final double YEAR_IN_SECONDS = 31556926;
    public static final double STEP_SIZE_TRAJECTORY = 3600;
    public static final int CANVAS_WIDTH = 1400;
    public static final int CANVAS_HEIGHT = 1000;
    public static double scale = 1e11;
    public Camera cam;
    public StateInterface[] states;
    int counter = 4500;
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
    Group superGroup = new Group();
    double speed;
    Button camLockToggle;
    Media song;
    Text probeMass;
    Vector3d camLock;
    ProbeSimulator sim;
    Boolean probeLock;
    ArrayList<Shape3D> planetPath = new ArrayList<>();
    MediaPlayer player;
    Group paths = new Group();
    Scene scene;

    @FXML
    private Button camButton;

    @FXML
    private Label sliderLabel;

    @FXML
    private Slider slider;

    @FXML
    private Label fuelCounter;

    @FXML
    private Group simulation;

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
        SpaceObjectBuilder builder = new SpaceObjectBuilder(getClass().getResource("/solar_system_data-2020_04_01.txt").getFile());
        ArrayList<SpaceObject> spaceObjects = builder.getSpaceObjects();
        planets = spaceObjects;
        double[] radius = new double[]{700000, 2439.7, 6051.8, 6371, 1737.1, 3389.5, 69911, 58232, 2575.7, 25362, 2462.2, 10000};

        Vector3d vel = new Vector3d(0, 0, 0);
        Vector3d pos = new Vector3d(-3223960.8810019065,5495059.07408366,6558.208615016209);
        StateInterface [] min = new StateInterface[1];
        double minDist = 1e17;
        ProbeSimulator sim = new ProbeSimulator(spaceObjects);
        sim.trajectory(pos, vel, YEAR_IN_SECONDS, STEP_SIZE_TRAJECTORY);
        StateInterface [] states = sim.getStates();
        this.states = states;
        this.sim = sim;
        initLight(group);
        probePos = spaceObjects.get(spaceObjects.size() - 1).getPosition();
        for (int j = 0; j < spaceObjects.size(); j++) {
            SpaceObject o = spaceObjects.get(j);
            if (State.names.get(j).equals("Probe")){
                Box b = new Box();
                b.setDepth(10);
                b.setWidth(10);
                b.setHeight(10);
                group.getChildren().add(b);
                shapes.add(b);
                b.translateXProperty().set(toScreenCoordinates(o.getPosition().getX()) - toScreenCoordinates(probePos.getX()));
                b.translateYProperty().set(toScreenCoordinates(o.getPosition().getY()) - toScreenCoordinates(probePos.getY()));
                b.translateZProperty().set(toScreenCoordinates(o.getPosition().getZ()) - toScreenCoordinates(probePos.getY()));
                Text name = new Text(State.names.get(j));
                name.setStroke(Color.LIMEGREEN);
                name.setFill(Color.LIMEGREEN);
                name.translateXProperty().set(toScreenCoordinates(o.getPosition().getX())- toScreenCoordinates(probePos.getX()));
                name.translateYProperty().set(toScreenCoordinates(o.getPosition().getY())- toScreenCoordinates(probePos.getY()));
                name.translateZProperty().set(toScreenCoordinates(o.getPosition().getZ())- toScreenCoordinates(probePos.getZ()));
                names.add(name);
                group.getChildren().add(name);
            }
            else {
                spaceObjects.get(j).setRadius(radius[j]);
                Sphere sphere = new Sphere(20);
                sphere.translateXProperty().set(toScreenCoordinates(o.getPosition().getX()) - toScreenCoordinates(probePos.getX()));
                sphere.translateYProperty().set(toScreenCoordinates(o.getPosition().getY()) - toScreenCoordinates(probePos.getY()));
                sphere.translateZProperty().set(toScreenCoordinates(o.getPosition().getZ()) -  toScreenCoordinates(probePos.getZ()));
                group.getChildren().add(sphere);
                Text name = new Text(State.names.get(j));
                name.setStroke(Color.LIMEGREEN);
                name.setFill(Color.LIMEGREEN);
                name.translateXProperty().set(toScreenCoordinates(o.getPosition().getX()) - toScreenCoordinates(probePos.getX()));
                name.translateYProperty().set(toScreenCoordinates(o.getPosition().getY()) - toScreenCoordinates(probePos.getY()));
                name.translateZProperty().set(toScreenCoordinates(o.getPosition().getZ())   - toScreenCoordinates(probePos.getZ()));
                group.getChildren().add(name);
                names.add(name);
                shapes.add(sphere);
            }
        }
        try {
            initMaterials();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void initProbeFuelCounter(Group superGroup){
        Text probeFuel = new Text();
        probeFuel.setFill(Color.WHITE);
        superGroup.getChildren().add(probeFuel);
        probeFuel.setX(CANVAS_WIDTH/2 - 100);
        probeFuel.setY(CANVAS_HEIGHT - 30);
        this.probeMass = probeFuel;
    }

    public Scene start(Stage primaryStage) throws Exception {
        SpaceObjectBuilder builder = new SpaceObjectBuilder(getClass().getResource("solar_system_data-2020_04_01.txt").getFile());
        Group group = new Group();
        group.translateXProperty().set(CANVAS_WIDTH / 2);
        group.translateYProperty().set(CANVAS_HEIGHT / 2);
        this.group = group;
        superGroup.getChildren().add(new ImageView(new Image("2k_stars.jpeg")));
        superGroup.getChildren().add(group);
        this.group.getChildren().add(paths);
        initProbeFuelCounter(superGroup);
        initSlider(superGroup);
        drawState(group);
        Camera cam = new PerspectiveCamera();
        this.cam = cam;
        cam.setFarClip(1e100);
        Parent root = FXMLLoader.load(getClass().getResource("Panel.fxml"));
        Scene scene = new Scene(superGroup, CANVAS_WIDTH, CANVAS_HEIGHT);
        Image background = new Image("textures/2k_stars_milky_way.jpeg");
        BackgroundImage back = new BackgroundImage(background, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        initMouseControl(group, scene);
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                group.setTranslateZ(group.getTranslateZ() + scrollEvent.getDeltaY() * (-1));
            }
        });
        Button camLockToggle = new Button("CAM");
        superGroup.getChildren().add(camLockToggle);
        this.camLockToggle = camLockToggle;
        scene.setCamera(cam);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().toString() == "SPACE"){
                    String song = "song2.mp3";
                    Media media = new Media(Paths.get(song).toUri().toString());
                    player = new MediaPlayer(media);
//                    player.play();
                    speed = 1;
                    double [] probeFuel = sim.getProbeMass();
                    probeLock = true;
                    AnimationTimer p = new AnimationTimer() {
                        @Override
                        public void handle(long l) {
                            Vector3d probe = states[counter].getPositions()[states[counter].getPositions().length - 1];
                            probePos = probe;
                            sunPos = states[counter].getPositions()[8];
                            if (probeLock){
                                camLock = states[counter].getPositions()[states[counter].getPositions().length - 1];
                                double screenX = toScreenCoordinates(states[counter].getPositions()[0].getX()) - toScreenCoordinates(probePos.getX());
                                double screenY = toScreenCoordinates(states[counter].getPositions()[0].getY()) - toScreenCoordinates(probePos.getY());
                                double screenZ = toScreenCoordinates(states[counter].getPositions()[0].getZ()) - toScreenCoordinates(probePos.getZ());
                                paths.translateXProperty().set(screenX);
                                paths.translateYProperty().set(screenY);
                                paths.translateZProperty().set(screenZ);
                            }
                            else{
                                camLock = states[counter].getPositions()[8];
                                double screenX = toScreenCoordinates(states[counter].getPositions()[0].getX()) - toScreenCoordinates(sunPos.getX());
                                double screenY = toScreenCoordinates(states[counter].getPositions()[0].getY()) - toScreenCoordinates(sunPos.getY());
                                double screenZ = toScreenCoordinates(states[counter].getPositions()[0].getZ()) - toScreenCoordinates(sunPos.getZ());
                                paths.translateXProperty().set(screenX);
                                paths.translateYProperty().set(screenY);
                                paths.translateZProperty().set(screenZ);
                            }
                            camLockToggle.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    probeLock = !probeLock;
                                }
                            });
                            probeMass.setText(String.valueOf(probeFuel[counter]));
//                            fuelCounter.setText(String.valueOf(probeFuel[counter]));
                            for(int i = 0; i < shapes.size(); i++){
                                double screenX = toScreenCoordinates(states[counter].getPositions()[i].getX()) - toScreenCoordinates(camLock.getX());
                                double screenY = toScreenCoordinates(states[counter].getPositions()[i].getY()) - toScreenCoordinates(camLock.getY());
                                double screenZ = toScreenCoordinates(states[counter].getPositions()[i].getZ()) - toScreenCoordinates(camLock.getZ());
                                shapes.get(i).setRotationAxis(Rotate.X_AXIS);
                                shapes.get(i).rotateProperty().set(shapes.get(i).getRotate() + 0.5);
                                shapes.get(i).translateXProperty().set(screenX);
                                shapes.get(i).translateYProperty().set(screenY);
                                shapes.get(i).translateZProperty().set(screenZ);
                                Sphere path = new Sphere(1);
                                PhongMaterial redMaterial = new PhongMaterial();
                                redMaterial.setSpecularColor(Color.ORANGE);
                                redMaterial.setDiffuseColor(Color.RED);
                                path.setMaterial(redMaterial);
                                paths.getChildren().add(path);
                                path.translateXProperty().set(toScreenCoordinates(states[counter].getPositions()[i].getX()) - toScreenCoordinates(states[counter].getPositions()[0].getX()));
                                path.translateYProperty().set(toScreenCoordinates(states[counter].getPositions()[i].getY()) - toScreenCoordinates(states[counter].getPositions()[0].getY()));
                                path.translateZProperty().set(toScreenCoordinates(states[counter].getPositions()[i].getZ()) - toScreenCoordinates(states[counter].getPositions()[0].getZ()));
                                names.get(i).translateXProperty().set(screenX);
                                names.get(i).translateYProperty().set(screenY);
                                names.get(i).translateZProperty().set(screenZ);
                            }

                            counter += speed;
//                    if (counter == 21886){
//                        states[counter].getVelocities()[states[counter].getVelocities().length - 1].add(new Vector3d(1000000,1000000,1000000));
//                    }
                        }
                    };
                    p.start();
                }
            }
        });
//        group.getChildren().add(cam);
        return scene;
    }


    public void initMaterials() throws URISyntaxException {
        for (int i = 0; i < planets.size(); i++){
            String imgSrc = "textures/8k_sun.jpeg";
            PhongMaterial m = new PhongMaterial();
            switch (State.names.get(i)){
                case "Sun":
                    imgSrc = "textures/8k_sun.jpeg";
                    m.setDiffuseColor(Color.ORANGE);
                    break;
                case "Mercury":
                    imgSrc = "textures/2k_mercury.jpeg";
                    m.setDiffuseColor(Color.ORANGERED);
                    break;
                case "Venus":
                    imgSrc = "textures/2k_venus_atmosphere.jpeg";
                    m.setDiffuseColor(Color.LAVENDER);
                    break;
                case "Earth":
                    imgSrc = "textures/2k_earth_daymap.jpeg";
                    m.setDiffuseColor(Color.BLUE);
                    break;
                case "Mars":
                    imgSrc = "textures/2k_mars.jpeg";
                    m.setDiffuseColor(Color.INDIANRED);
                    break;
                case "Jupiter":
                    imgSrc = "textures/2k_jupiter.jpeg";
                    m.setDiffuseColor(Color.CORAL);
                    break;
                case "Saturn":
                    imgSrc = "textures/2k_saturn.jpeg";
                    m.setDiffuseColor(Color.SANDYBROWN);
                    break;
                case "Uranus":
                    imgSrc = "textures/2k_uranus.jpeg";
                    m.setDiffuseColor(Color.TURQUOISE);
                    break;
                case "Neptune":
                    imgSrc = "textures/2k_neptune.jpeg";
                    m.setDiffuseColor(Color.DARKBLUE);
                    break;
                case "Probe":
                    imgSrc = "textures/2k_uranus.jpeg";
                    m.setDiffuseColor(Color.LIMEGREEN);
                    break;
                case "Titan":
                    imgSrc = "textures/2k_neptune.jpeg";
                    m.setDiffuseColor(Color.ROSYBROWN);
                    break;
                case "Moon":
                    imgSrc = "textures/8k_moon.jpeg";
                    m.setDiffuseColor(Color.GRAY);
                    break;
            }

            shapes.get(i).setMaterial(m);
            PhongMaterial buzz = new PhongMaterial();
            buzz.setDiffuseMap(new Image("textures/550x755.png"));
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

    public static double toScreenCoordinates(double d){
        return ((d / scale)) * 280;
    }

    public void updateSpeed(MouseEvent mouseEvent) {
        System.out.println(String.valueOf(slider.getValue()));
        speed = (int) slider.getValue();
    }

    public void changeCamLock(MouseEvent mouseEvent) {
        probeLock = !probeLock;
    }

    @FXML
    void initialize() {
        assert camButton != null : "fx:id=\"camButton\" was not injected: check your FXML file 'Panel.fxml'.";
        assert sliderLabel != null : "fx:id=\"sliderLabel\" was not injected: check your FXML file 'Panel.fxml'.";
        assert slider != null : "fx:id=\"slider\" was not injected: check your FXML file 'Panel.fxml'.";
        assert fuelCounter != null : "fx:id=\"fuelCounter\" was not injected: check your FXML file 'Panel.fxml'.";
        assert simulation != null : "fx:id=\"simulation\" was not injected: check your FXML file 'Panel.fxml'.";
    }
}


