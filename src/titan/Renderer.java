package titan;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import javafx.scene.text.TextAlignment;
import titan.ui.View;

import java.util.ArrayList;

/**
 * Class responsible for actually drawing the results of the simulation onto the canvas
 * extends the AnimationTimer class in order to handle the updates
 */
public class Renderer extends AnimationTimer {

    private long startNanoTime = 0;

    private int speedOffset = 1; //default render speed, 1 state/frame

    private Canvas canvas;
    private ArrayList<Double[]> paths = new ArrayList<>();
    private State state;
    private int count = 0;
    private StateInterface[] s1;
    private View view;

    /**
     * Constructor for creating the Renderer
     * @param view the View class which contains the Canvas
     * @param states The list of states calculated by the Solver class
     */
    public Renderer(View view, StateInterface[] states) {
        this.view = view;
        this.canvas = view.getCanvas();
        this.state = (State) states[0];
        this.s1 = states;
    }

    /**
     * Method inherited from AnimationTimer class, gets called ~60 times per second
     * @param now current time
     */
    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (startNanoTime == 0)
            startNanoTime = now;

        gc.setFill(Paint.valueOf("#469FCB"));
        for (int i = 0; i < state.positions.length; i++) {
            drawSpaceObject(gc, state.positions[i], i);
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);

        state = (State) s1[count];
        count += speedOffset;
        if (count > s1.length) {
            this.stop();
        }
    }

    public void drawSpaceObject(GraphicsContext gc, Vector3d vec, int index) {
        Sphere sphere = new Sphere();
        gc.fillOval(gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()), 5, 5);
        gc.setFill(Paint.valueOf("#CC52D7"));
        gc.strokeOval(gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()), 5, 5);
        for (Double[] path : paths) {
            gc.fillOval(path[0], path[1], path[2], path[3]);
        }
        paths.add(new Double[]{gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()), 1.0, 1.0});        gc.setFill(Paint.valueOf("#DBF188"));
        gc.fillText(State.names.get(index), gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX() + 6), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY() + 30));
        gc.setFill(Paint.valueOf("#ffffff"));
    }


    /**
     * Updates the speed offset to a new offset. The speed offset determines how many intermediate states are skipped
     * when drawing the simulation
     * @param offset
     */
    public void setSpeedOffset(int offset) {
        this.speedOffset = offset;
    }

    public int getSpeedOffset() {
        return speedOffset;
    }


    public static double toScreenCoordinates(double d) {
        return ((d / 1e11)) * 280;
    }

}