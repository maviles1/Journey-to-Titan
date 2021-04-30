package titan;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Sphere;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Renderer extends AnimationTimer {

    private long startNanoTime = 0;

    private int speedOffset = 1;

    private Canvas canvas;
    ArrayList<Double[]> paths = new ArrayList<>();
    State state;
    int count = 0;
    StateInterface[] s1;
    View view;

    public Renderer(View view, StateInterface[] states) {
        this.view = view;
        this.canvas = view.getCanvas();
        this.state = (State) states[0];
        this.s1 = states;
    }

    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (startNanoTime == 0)
            startNanoTime = now;

        double t = (now - startNanoTime) / 1000000000.0;

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