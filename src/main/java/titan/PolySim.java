package titan;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import titan.interfaces.StateInterface;
import titan.ui.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;

@SuppressWarnings("unchecked")
public class PolySim extends AnimationTimer {

    private final int WIDTH = 700 * 4;
    private final int HEIGHT = 1000 * 4;
    private final int AMOUNT_BODIES = 12;

    private int speedOffset = 5; //default render speed, 1 state/frame

    private Canvas canvas;
    private ArrayList<Double[]> paths = new ArrayList<>();
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
        this.polyStates = polyStates;
        initHorizons();
        for (int i = 0; i < polyStates.size()+1; i++) {
            polyPaths.add(new ArrayList<>());
        }
    }

    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Color[] simColors = {Color.valueOf("#2ecc71"), Color.valueOf("#f1c40f"), Color.valueOf("#3498db")};

        gc.setFill(Color.valueOf("#ecf0f1"));
        for (Double[] pos : nasaPaths) {
            gc.fillOval(pos[0]-0.5, pos[1]-0.5, 1, 1);
        }

        for (int i = 0; i < polyStates.size(); i++) {
            gc.setFill(simColors[i]);
            for (int j = 0; j < AMOUNT_BODIES; j++) {
                polyPaths.get(i).add(polyPaint((State) polyStates.get(i)[count], gc, j));
            }

            gc.setFill(((Color)gc.getFill()).darker().desaturate());
            for (Double[] pos : polyPaths.get(i)) {
                gc.fillOval(pos[0]-1, pos[1]-1, 2, 2);
            }
        }

        for (int i = 0; i < 9; i++) {
            double xNasa = (WIDTH / 2.0) + toCoord(nasaPos[i].get(count).getX());   //for some reason get error here that nasas how run out of index here at 8761/8767 and idk why
            double yNasa = (HEIGHT / 2.0) + toCoord(nasaPos[i].get(count).getY());
            nasaPaths.add(new Double[]{xNasa, yNasa});
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
        return new Double[]{x, y};
    }

//    public void paint(GraphicsContext g, int i) {
//        if (poly)
//            paintOtherSimulations(g, i);
//
//        g.setFill(Color.RED);
//        for (Double[] pos : nasaPaths) {
//            g.fillOval(pos[0]-1, pos[1]-1, 2, 2);
//        }
//
//        g.setFill(Color.LIGHTGRAY);
//        for (Double[] pos : paths) {
//            g.fillOval(pos[0]-1, pos[1]-1, 2, 2);
//        }
//
//        if (i < 9) {
//            double xNasa = (WIDTH / 2.0) + toCoord(nasaPos[i].get(count).getX());
//            double yNasa = (HEIGHT / 2.0) + toCoord(nasaPos[i].get(count).getY());
//            nasaPaths.add(new Double[]{xNasa, yNasa});
//        }
//
//        g.setFill(Color.WHITE);
//
//        double x = (WIDTH / 2.0) + toCoord(si.getPosition()[i].getX());
//        double y = (HEIGHT / 2.0) + toCoord(si.getPosition()[i].getY());
//        g.fillOval(x-2.5, y-2.5, 5, 5);
//        paths.add(new Double[]{x, y});
//
//        if (State.names.get(i).equals("Saturn") || State.names.get(i).equals("Earth"))
//            g.fillText(State.names.get(i), x - 20, y - 10);
//        else
//            g.fillText(State.names.get(i), x + 10, y + 10);
//
//
//
//    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public ArrayList<Vector3d> parseHorizons(String filename) {
        try {
            String url = getClass().getResource("/" + filename).getPath();
            File file = new File(url);
            Scanner scanner = new Scanner(file);
            int size = (int) Math.round((31556926 / 3600.0) + 1.5);
            ArrayList<Vector3d> pos = new ArrayList<>();
            pos.ensureCapacity(size);
            //titanPos.ensureCapacity(size);
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                //2459305.041666667, A.D. 2021-Mar-31 13:00:00.0000,  8.787375012365239E+08, -1.204414917090462E+09, -1.425353973159307E+07,
                Vector3d posV = new Vector3d(Double.parseDouble(data[2].trim()), Double.parseDouble(data[3].trim()), Double.parseDouble(data[4].trim()));
                pos.add(posV.mul(1000));
                //titanPos.add(posV.mul(1000));
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


    public static double toCoord(double d) {
        return d / 7e8;
    }
}