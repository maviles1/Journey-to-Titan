package titan.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import titan.State;
import titan.interfaces.StateInterface;

public class TitanView extends Renderer2D {

    private double res = 50;

    private double toCoord(double val) {
        return val / res;
    }

    public TitanView(StateInterface[] states) {
        this.WIDTH = 1200;
        this.HEIGHT = 3000;
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

        //check collision
        if (landerY <= HEIGHT - 50)
            gc.fillOval(landerX, HEIGHT - 60, 10, 10);
        else
            gc.fillOval(landerX, HEIGHT - toCoord(landerY) - 50, 10, 10);

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, HEIGHT - 50, WIDTH, 50);

        if (count < states.length - 1)
            count++;
    }

    @Override
    protected void prepareConsole() {
        console.getChildren().clear();
        console.getChildren().add(new Label("Distance to Titan: " + Math.max(states[count].getPositions()[0].getY(), 0)));
        ((Label)console.getChildren().get(0)).setTextFill(Color.WHITE);
        //((Label)console.getChildren().get(1)).setTextFill(Color.WHITE);
    }

    @Override
    public void addKeyHandler() {
        setOnKeyPressed(event -> {

        });
    }
}
