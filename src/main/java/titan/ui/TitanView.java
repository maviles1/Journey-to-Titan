package titan.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import titan.flight.State;
import titan.interfaces.StateInterface;

public class TitanView extends Renderer2D {

//    private double res = 150; // for height 1000
    private double res = 50; // for height 1000

    private final double RADIUS = 10;

    private double toCoord(double val) {
        return val / res;
    }

    public TitanView(StateInterface[] states) {
        this.WIDTH = 1200 * 3;
        this.HEIGHT = 3000;
//        this.HEIGHT = 1000;
        this.canvas = new Canvas(WIDTH, HEIGHT);
        this.states = states;

        setUpRenderer();
        setUpScrollPane();
    }

    @Override
    protected void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        prepareConsole();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //this is where we draw the probe
        State state = (State) states[count];
        gc.setFill(Color.WHITE);

        double landerX = state.getPositions()[0].getX();
        double landerY = state.getPositions()[0].getY();
        setVvalue(1 - (toCoord(landerY) / HEIGHT));

        //check collision
        if (HEIGHT - toCoord(landerY) >= HEIGHT - 50)
            gc.fillOval(landerX, HEIGHT - 70, RADIUS * 2, RADIUS * 2);
        else {
            //double rotationCenterX = landerX + (20 / 2.0);
            double rotationCenterX = landerX + RADIUS;
            double rotationCenterY = HEIGHT - toCoord(landerY) - 50 + RADIUS;

            gc.save();
            gc.transform(new Affine(new Rotate(45, rotationCenterX, rotationCenterY)));
            gc.fillRect(landerX, HEIGHT - toCoord(landerY) - 50, RADIUS*2, RADIUS*2);
            gc.restore();
        }

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, HEIGHT - 50, WIDTH, 50);

        if (count < states.length - 1)
            count++;
    }

    @Override
    protected void prepareConsole() {
        console.getChildren().clear();
        console.getChildren().add(new Label("Distance to Titan: " + Math.max(states[count].getPositions()[0].getY(), 0)));
        console.getChildren().add(new Label("Distance to Titan: " + states[count].getPositions()[0].getY()));
        console.getChildren().add(new Label("VY:  " + states[count].getVelocities()[0].getY()));
        ((Label)console.getChildren().get(0)).setTextFill(Color.WHITE);
        ((Label)console.getChildren().get(1)).setTextFill(Color.WHITE);
        ((Label)console.getChildren().get(2)).setTextFill(Color.WHITE);
    }

    @Override
    public void addKeyHandler() {
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {

            }
        });
    }
}
