package titan.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import titan.interfaces.StateInterface;

/**
 * This abstract class is extended by PolySim and TitanView in order to display the simulation results.
 */
public abstract class Renderer2D extends ScrollPane {

    protected double WIDTH;
    protected double HEIGHT;
    protected Canvas canvas;

    protected AnimationTimer renderer;
    protected AnchorPane pane = new AnchorPane();
    protected VBox console;
    protected StateInterface[] states;

    protected int count;

    protected void setUpRenderer() {
        renderer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                render();
            }
        };
    }

    public void start() {
        renderer.start();
    }

    public void stop() {
        renderer.stop();
    }

    protected void setUpScrollPane() {
        pane.getChildren().add(this);

        setPannable(true);
        setHvalue(0.5);
        setVvalue(0.5);

        this.setPrefSize(1200, 800);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);

        console = new VBox();
        AnchorPane.setLeftAnchor(console, 20.0);
        AnchorPane.setTopAnchor(console, 20.0);

        pane.getChildren().add(console);
        setContent(canvas);

        addKeyHandler();
    }

    public abstract void addKeyHandler();
    protected abstract void render();
    protected abstract void prepareConsole();
}
