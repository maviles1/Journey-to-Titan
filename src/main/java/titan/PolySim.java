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

    private final int WIDTH = 1000 * 5;
    private final int HEIGHT = 1000 * 5;
    private final int AMOUNT_BODIES = 12;

//    private static double res = 7e8;
    private double res = 5e9;
    private int speedOffset = 5; //default render speed, 1 state/frame

    private Canvas canvas;
    private ArrayList<Double[]> nasaPaths = new ArrayList<>();

    ArrayList<Vector3d> titanPos = new ArrayList<>();
    ArrayList<Vector3d>[] nasaPos = new ArrayList[11];

    private int count = 0;

    ArrayList<StateInterface[]> polyStates;
    List<List<Double[]>> polyPaths = new ArrayList<>();

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

    }

    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //green, yellow, blue
        Color[] simColors = {Color.valueOf("#2ecc71"), Color.valueOf("#f1c40f"), Color.valueOf("#3498db")};

        for (int i = 0; i < 9; i++) {
            nasaPaths.add(new Double[]{calcX(nasaPos[i].get(count).getX()), calcY(nasaPos[i].get(count).getY())});
        }

        gc.setFill(Color.valueOf("#ecf0f1"));
        for (Double[] pos : nasaPaths) {
            gc.fillOval(calcX(pos[0])-0.5, calcY(pos[1])-0.5, 1, 1);
        }

        for (int i = 0; i < polyStates.size(); i++) {
            gc.setFill(simColors[i]);
            for (int j = 0; j < AMOUNT_BODIES; j++) {
                polyPaths.get(i).add(polyPaint((State) polyStates.get(i)[count], gc, j));
            }

            gc.setFill(((Color)gc.getFill()).darker().desaturate());
            for (Double[] pos : polyPaths.get(i)) {
                gc.fillOval(calcX(pos[0])-1, calcY(pos[1])-1, 2, 2);
            }
        }

        count += speedOffset;
        if (count > 8767) {
            this.stop();
        }
    }

    public Double[] polyPaint(State state, GraphicsContext gc, int body) {
        double x = (WIDTH / 2.0) + toCoord(state.getPosition()[body].getX());
        double y = (HEIGHT / 2.0) + toCoord(state.getPosition()[body].getY());
        gc.fillOval(x-2.5, y-2.5, 5, 5);

        if (State.names.get(body).equals("Saturn") || State.names.get(body).equals("Earth"))
            gc.fillText(State.names.get(body), x - 20, y - 10);
        else
            gc.fillText(State.names.get(body), x + 10, y + 10);

        return new Double[]{state.getPosition()[body].getX(), state.getPosition()[body].getY()};
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
                pos.add(posV.mul(1000));
            }
            return pos;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initHorizons() {
        try {
            titanPos = parseHorizons("titan2.txt");
            nasaPos[0] = parseHorizons("sunTraj.txt");
            nasaPos[1] = parseHorizons("mercuryTraj.txt");
            nasaPos[2] = parseHorizons("venusTraj.txt");
            nasaPos[3] = parseHorizons("earthTraj.txt");
            nasaPos[4] = parseHorizons("lunaTraj.txt");
            nasaPos[5] = parseHorizons("marsTraj.txt");
            nasaPos[6] = parseHorizons("jupiterTraj.txt");
            nasaPos[7] = parseHorizons("saturnTraj.txt");
            nasaPos[8] = titanPos;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public double toCoord(double d) {
        return d / res;
    }
}