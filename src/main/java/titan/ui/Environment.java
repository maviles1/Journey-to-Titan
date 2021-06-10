package titan.ui;


import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import titan.State;
import titan.interfaces.StateInterface;

public class Environment extends ScrollPane {

    private final double WIDTH = 1200;
    private final double HEIGHT = 800;
    private final Canvas canvas;

    private final AnimationTimer renderer;
    private final AnchorPane pane;
    private VBox console;
    private StateInterface[] states;

    private int count;

    public Environment() {
        this.canvas = new Canvas(WIDTH, HEIGHT);
        pane = new AnchorPane();
        this.renderer = setUpRenderer();
        setUpScrollPane();
    }

    public Environment(StateInterface[] states) {
        this.canvas = new Canvas(WIDTH, HEIGHT);
        pane = new AnchorPane();
        this.renderer = setUpRenderer();
        setUpScrollPane();
        this.states = states;
    }

    public void start() {
        renderer.start();
    }

    public void stop() {
        renderer.stop();
    }

    private AnimationTimer setUpRenderer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        prepareConsole();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //this is where we draw the probe
        State state = (State) states[count];
        gc.setFill(Color.WHITE);
        //check collision
        if (state.getPositions()[0].getY() >= HEIGHT - 50)
            gc.fillOval(state.getPositions()[0].getX(), HEIGHT - 60, 10, 10);
        else
            gc.fillOval(state.getPositions()[0].getX(), state.getPositions()[0].getY(), 10, 10);

        gc.setFill(Color.BEIGE);
        gc.fillRect(0, HEIGHT - 50, WIDTH, 50);

        if (count < states.length - 1)
            count++;
    }

    public void prepareConsole() {
        console.getChildren().clear();
        console.getChildren().add(new Label("Distance to Titan"));
        ((Label)console.getChildren().get(0)).setTextFill(Color.WHITE);
        //((Label)console.getChildren().get(1)).setTextFill(Color.WHITE);
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

        });
    }
}
