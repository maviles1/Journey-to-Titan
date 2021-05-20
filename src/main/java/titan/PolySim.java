package titan;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import titan.interfaces.StateInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class PolySim extends ScrollPane {

    private final double WIDTH = 1000 * 5.5;
    private final double HEIGHT = 1000 * 5.5;
    private final int AMOUNT_BODIES = 12;

    private double res = 7e8;
    private int speedOffset = 1; //default render speed, 1 state/frame

    private final Canvas canvas;
    private ArrayList<Double[]> nasaPaths = new ArrayList<>();
    private ArrayList<Vector3d>[] nasaPos = new ArrayList[AMOUNT_BODIES - 1]; //no probe in nasa data

    private int count = 0;
    private int minSim;
    boolean probeFocus = false;

    private final AnimationTimer renderer;
    private final AnchorPane pane;
    private VBox console;

    ArrayList<StateInterface[]> polyStates;
    List<List<Double[]>> polyPaths = new ArrayList<>();

    String[] probeNames = {"Probe"};
    String[] names = new String[]{"Sun", "Mercury", "Venus", "Earth", "Moon", "Mars", "Jupiter", "Saturn", "Titan", "Uranus", "Neptune", "Probe"};

    public PolySim(StateInterface[] states) {
        this(new ArrayList<>(Collections.singletonList(states)));
    }

    public PolySim(ArrayList<StateInterface[]> polyStates) {
        this.canvas = new Canvas(WIDTH, HEIGHT);
        pane = new AnchorPane();
        this.renderer = setUpRenderer();
        this.polyStates = polyStates;   //list containing the different simulations to display
        setUpScrollPane();
        initHorizons();                 //gets the planet positions data from nasa

        for (int i = 0; i < polyStates.size()+1; i++) {
            polyPaths.add(new ArrayList<>());   //add arraylists to contain the previous position
        }

        minSim = polyStates.get(0).length;      //min amount of states to prevent array overflow in animation timer loop
        for (int i = 1; i < polyStates.size(); i++) {
            minSim = Math.min(minSim, polyStates.get(i).length);
        }
    }

    public void start() {
        renderer.start();
    }

    public void stop() {
        renderer.stop();
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        console.getChildren().clear();
        console.getChildren().add(new Label("Distance to Titan"));
        ((Label)console.getChildren().get(0)).setTextFill(Color.WHITE);

        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //green, yellow, blue
        Color[] simColors = {Color.valueOf("#2ecc71"), Color.valueOf("#f1c40f"), Color.valueOf("#3498db"), Color.valueOf("#f8a5c2"), Color.valueOf("#F79F1F"), Color.valueOf("#ff5252")};

        //First add the current pos in the paths with the actual nasa position data
        //drawNasaPaths(gc);

        //Now begin drawing the different simulations
        for (int i = 0; i < polyStates.size(); i++) {
            gc.setFill(simColors[i].darker().desaturate());
            for (Double[] pos : polyPaths.get(i)) {         //begin drawing the path history
                gc.fillOval(calcX(pos[0])-1, calcY(pos[1])-1, 2, 2);
            }

            gc.setFill(simColors[i]);           //select color for simulation from colors array
            for (int j = 0; j < AMOUNT_BODIES; j++) {
                polyPaths.get(i).add(polyPaint(i, gc, j));  //draw celestial body and add position to path history
            }

            Label distance = new Label(((State)polyStates.get(i)[count]).getPosition()[11].dist(((State)polyStates.get(i)[count]).getPosition()[8]) / 1000.0 + " - " + probeNames[i]);
            distance.setTextFill(gc.getFill());
            console.getChildren().add(distance);
        }

        gc.setFill(Color.valueOf("#ecf0f1"));
        Vector3d testPoint = new Vector3d(-2.4951517995514418E13, -1.794349344879982E12,2.901591968932223E7); //test point given by probesimulatortest
        gc.fillOval(calcX(testPoint.getX())-2.5, calcY(testPoint.getX())-2.5, 5, 5);    //not really sure why test point is rendering weirdly
        gc.fillText("Test Point", calcX(testPoint.getX()) + 10, calcY(testPoint.getY()) + 10);

        count += speedOffset;
        if (count > polyStates.get(0).length || count >= minSim) {
            //this.stop();
            count -= speedOffset;
        }
    }

    public Double[] polyPaint(int i, GraphicsContext gc, int body) {
        State state = (State) polyStates.get(i)[count];
        //scale and position x and y coordinate
        double x = calcX(state.getPosition()[body].getX());
        double y = calcY(state.getPosition()[body].getY());
        gc.fillOval(x - 2.5, y - 2.5, 5, 5);

        if (names[body].equals("Saturn") || names[body].equals("Earth"))    //make sure their moons don't cover the planet names
            gc.fillText(names[body], x - 20, y - 10);
        else if (probeNames.length >= i && names[body].equals("Probe")) //select name for simulation run for probe if applicable
            gc.fillText(probeNames[i], x - 20, y - 10);
        else
            gc.fillText(names[body], x + 10, y + 10);

        //return to be added to path history
        return new Double[]{state.getPosition()[body].getX(), state.getPosition()[body].getY()};
    }

    public void drawNasaPaths(GraphicsContext gc) {
        for (int i = 0; i < 9; i++)
            nasaPaths.add(new Double[]{calcX(nasaPos[i].get(count).getX()), calcY(nasaPos[i].get(count).getY())});

        //Draw all the nasa position history paths
        gc.setFill(Color.valueOf("#ecf0f1"));
        for (Double[] pos : nasaPaths)
            gc.fillOval(calcX(pos[0])-0.5, calcY(pos[1])-0.5, 1, 1);
    }

    public void setProbeNames(String[] probeNames) {
        this.probeNames = probeNames;
    }

    public void scale(double factor) {
        res *= factor;
    }

    public void simSpeed(double factor) {
        speedOffset += factor;
    }

    public double toCoord(double d) {
        return (d / res);
    }

    public double calcX(double d) {

        if (solverFocus != 0) {
            if (probeFocus) {
                double y = WIDTH / 2 - toCoord(((State) polyStates.get(solverFocus - 1)[count]).getPosition()[11].getX()); //if probe
                return y + toCoord(d);
            }
            double x = WIDTH / 2 - toCoord(((State) polyStates.get(solverFocus - 1)[count]).getPosition()[8].getX()); //if titan
            return x + toCoord(d);
        }

        return WIDTH / 2 + toCoord(d);
    }

    public double calcY(double d) {
        if (solverFocus != 0) {
            if (probeFocus) {
                double y = HEIGHT / 2 - toCoord(((State) polyStates.get(solverFocus - 1)[count]).getPosition()[11].getY()); //if probe
                return y + toCoord(d);
            }
            double y = HEIGHT / 2 - toCoord(((State) polyStates.get(solverFocus - 1)[count]).getPosition()[8].getY()); //if titan
            return y + toCoord(d);
        }

        return (HEIGHT / 2.0) + toCoord(d);
    }

    public ArrayList<Vector3d> parseHorizons(String filename) {
        try {
            String url = getClass().getResource("/" + filename).getPath();
            File file = new File(url);
            Scanner scanner = new Scanner(file);
            int size = (int) Math.round((31556926 / 3600.0) + 1.5);
            ArrayList<Vector3d> pos = new ArrayList<>();
            pos.ensureCapacity(size);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                Vector3d posV = new Vector3d(Double.parseDouble(data[2].trim()), Double.parseDouble(data[3].trim()), Double.parseDouble(data[4].trim()));
                pos.add(posV.mul(1000)); //nasa horizons data comes in km, so we need to convert to meters
            }
            return pos;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private AnimationTimer setUpRenderer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
    }

    private void setUpScrollPane() {
        pane.getChildren().add(this);

        setPannable(true);
        setHvalue(0.5);
        setVvalue(0.5);

        this.setPrefSize(1200, 800);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);

        console = new VBox();
        AnchorPane.setLeftAnchor(console, 20.0);
        AnchorPane.setTopAnchor(console, 20.0);

        pane.getChildren().add(console);
        setContent(canvas);

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.Z) {
                scale(1.05);
            } else if (event.getCode() == KeyCode.X) {
                scale(0.95);
            } else if (event.getCode() == KeyCode.EQUALS) {
                simSpeed(1);
            } else if (event.getCode() == KeyCode.MINUS) {
                simSpeed(-1);
            } else if (event.getCode() == KeyCode.C) {
                console.setVisible(!console.isVisible());
            } else if (event.getCode() == KeyCode.P) {
                if (solverFocus == polyStates.size()) {
                    solverFocus = 0;
                } else {
                    if (!probeFocus)
                        probeFocus = true;
                    else
                        solverFocus++;
                }
            } else if (event.getCode() == KeyCode.T) {
                if (solverFocus == polyStates.size())
                    solverFocus = 0;
                else {
                    if (probeFocus)
                        probeFocus = false;
                    else
                        solverFocus++;
                }
            }
        });
    }

    int solverFocus = 0;

    public void initHorizons() {
        try {
            nasaPos[0] = parseHorizons("sunTraj.txt");
            nasaPos[1] = parseHorizons("mercuryTraj.txt");
            nasaPos[2] = parseHorizons("venusTraj.txt");
            nasaPos[3] = parseHorizons("earthTraj.txt");
            nasaPos[4] = parseHorizons("lunaTraj.txt");
            nasaPos[5] = parseHorizons("marsTraj.txt");
            nasaPos[6] = parseHorizons("jupiterTraj.txt");
            nasaPos[7] = parseHorizons("saturnTraj.txt");
            nasaPos[8] = parseHorizons("titan2.txt");;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

