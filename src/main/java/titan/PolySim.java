package titan;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import titan.interfaces.StateInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@SuppressWarnings("unchecked")
public class PolySim extends AnimationTimer {

    private final double WIDTH = 1000 * 5.5;
    private final double HEIGHT = 1000 * 5.5;
    private final int AMOUNT_BODIES = 12;

    private double res = 7e8;
    private int speedOffset = 1; //default render speed, 1 state/frame

    private Canvas canvas;
    private ArrayList<Double[]> nasaPaths = new ArrayList<>();
    private ArrayList<Vector3d>[] nasaPos = new ArrayList[11];

    private int count = 0;
    private int minSim;

    ArrayList<StateInterface[]> polyStates;
    List<List<Double[]>> polyPaths = new ArrayList<>();

    String[] probeNames = {"Probe"};
    String[] names = new String[]{"Sun", "Mercury", "Venus", "Earth", "Moon", "Mars", "Jupiter", "Saturn", "Titan", "Uranus", "Neptune", "Probe"};

    public PolySim(StateInterface[] states) {
        this(new ArrayList<>(Collections.singletonList(states)));
    }

    public PolySim(ArrayList<StateInterface[]> polyStates) {
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.polyStates = polyStates;   //list containing the different simulations to display
        initHorizons();                 //gets the planet positions data from nasa
        for (int i = 0; i < polyStates.size()+1; i++) {
            polyPaths.add(new ArrayList<>());   //add arraylists to contain the previous position
        }
        minSim = polyStates.get(0).length;
        for (int i = 1; i < polyStates.size(); i++) {
            minSim = Math.min(minSim, polyStates.get(i).length);
        }
    }



    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

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
        }

        gc.setFill(Color.valueOf("#ecf0f1"));
        Vector3d testPoint = new Vector3d(-2.4951517995514418E13, -1.794349344879982E12,2.901591968932223E7); //test point given by probesimulatortest
        gc.fillOval(calcX(testPoint.getX())-1, calcY(testPoint.getX())-1, 2, 2);
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
        double x = (WIDTH / 2.0) + toCoord(state.getPosition()[body].getX());
        double y = (HEIGHT / 2.0) + toCoord(state.getPosition()[body].getY());
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

    public double calcX(double val) {
        return (WIDTH / 2.0) + toCoord(val);
    }
    public double calcY(double val) {
        return (HEIGHT / 2.0) + toCoord(val);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void scale(double factor) {
        res *= factor;
    }

    public void simSpeed(double factor) {
        speedOffset += factor;
    }

    public double toCoord(double d) {
        return d / res;
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