package titan.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import titan.flight.State;
import titan.interfaces.StateInterface;
import titan.landing.LandingRate;
import titan.landing.LandingState;

public class TitanView extends Renderer2D {

//    private double res = 150; // for height 1000
    private double res = 50; // for height 1000

    private double landingPadX = 125000;

    private final double RADIUS = 10;

    private double toCoord(double val) {
        return val / res;
    }

    public TitanView(StateInterface[] states) {
        this.WIDTH = 3000;
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

        gc.setFill(Paint.valueOf("#1c253c"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        //draw ground
        gc.setFill(Paint.valueOf("#e79c61"));
        gc.fillRect(0, HEIGHT - 50, WIDTH, 50);

        //draw landing pad
        gc.setFill(Color.AQUA);
        gc.fillRect(toCoord(landingPadX) - 2, HEIGHT - 50, 4, 4);

        //this is where we draw the probe
        LandingState state = (LandingState) states[count];
        gc.setFill(Color.WHITE);

        double landerX = state.getPosition().getX();
        double landerY = state.getPosition().getY();
        setVvalue(1 - (toCoord(landerY) / HEIGHT));

        //check collision
        if (checkCollision(landerY)) {
            if (!landed) {
                errorX = Math.abs(landerX - landingPadX);
//                errorX = landerX - landingPadX;
                errorVX = Math.abs(state.getVelocity().getX());
                errorVY = Math.abs(state.getVelocity().getY());
                errorAngle = Math.abs(state.getAngle());
                errorAngleVel = Math.abs(state.getAngularVelocity());
                landedAt = landerX;
                landed = true;
            }

//            gc.fillOval(toCoord(landerX) - RADIUS, HEIGHT - 50 - RADIUS * 2, RADIUS * 2, RADIUS * 2); //I dunno why the height has to be subtracted by radius * 2
            gc.drawImage(new Image("/TransparentUFO.png"), toCoord(landedAt) - RADIUS, HEIGHT - 50 - RADIUS*2, 529/10.0, 254 / 10.0);
        } else {
            errorX = Math.abs(landerX - landingPadX);
//            errorX = landerX - landingPadX;
            errorVX = Math.abs(state.getVelocity().getX());
            errorVY = Math.abs(state.getVelocity().getY());
            errorAngle = Math.abs(state.getAngle());
            errorAngleVel = Math.abs(state.getAngularVelocity());

            double rotationCenterX = toCoord(landerX);
            double rotationCenterY = HEIGHT - toCoord(landerY) - 50;

            gc.save();
            gc.transform(new Affine(new Rotate(Math.toDegrees(state.getAngle()), rotationCenterX, rotationCenterY)));
//            gc.fillRect(toCoord(landerX) - RADIUS, HEIGHT - toCoord(landerY) - 50 - RADIUS, RADIUS*2, RADIUS*2);
            gc.drawImage(new Image("/TransparentUFO.png"), toCoord(landerX) - RADIUS, HEIGHT - toCoord(landerY) - 50 - RADIUS, 529/10.0, 254 / 10.0);
            gc.restore();
        }

        if (count < states.length - 1)
            count++;
    }

    public boolean checkCollision(double landerY) {
        return toCoord(landerY) - RADIUS <= 0;
    }

    double errorX = 0;
    double errorVX = 0;
    double errorVY = 0;
    double errorAngleVel = 0;
    double errorAngle = 0;
    boolean landed = false;
    double landedAt = 0;

    @Override
    protected void prepareConsole() {
        LandingState state = (LandingState) states[count];
        console.getChildren().clear();
        console.getChildren().add(new Label("Distance to Titan: " + Math.max(states[count].getPositions()[0].getY(), 0)));
//        console.getChildren().add(new Label("VY:  " + states[count].getVelocities()[0].getY()));
        console.getChildren().add(new Label("ErrorX:  " + errorX));
//        console.getChildren().add(new Label("ErrorX:  " + state.getPosition().getX()));
        console.getChildren().add(new Label("ErrorVX:  " + errorVX));
//        console.getChildren().add(new Label("ErrorVX:  " + state.getVelocity().getX()));
        console.getChildren().add(new Label("ErrorVY:  " + errorVY));
//        console.getChildren().add(new Label("Angle:  " + Math.toDegrees(state.getAngle() % (2*Math.PI))));
        console.getChildren().add(new Label("Angle: " + errorAngle % (2*Math.PI)));
        console.getChildren().add(new Label("AngularVel:  " + errorAngleVel));

        ((Label)console.getChildren().get(0)).setTextFill(Color.WHITE);
        ((Label)console.getChildren().get(1)).setTextFill(Color.WHITE);
        ((Label)console.getChildren().get(2)).setTextFill(Color.WHITE);

        if (errorX < 0.1)
            ((Label)console.getChildren().get(1)).setTextFill(Color.GREEN);
        else
            ((Label)console.getChildren().get(1)).setTextFill(Color.RED);

        if (errorVX < 0.1)
            ((Label)console.getChildren().get(2)).setTextFill(Color.GREEN);
        else
            ((Label)console.getChildren().get(2)).setTextFill(Color.RED);

        if (errorVY < 0.1)
            ((Label)console.getChildren().get(3)).setTextFill(Color.GREEN);
        else
            ((Label)console.getChildren().get(3)).setTextFill(Color.RED);

        if (errorAngle < 0.02)
            ((Label)console.getChildren().get(4)).setTextFill(Color.GREEN);
        else
            ((Label)console.getChildren().get(4)).setTextFill(Color.RED);

        if (errorAngleVel < 0.01)
            ((Label)console.getChildren().get(5)).setTextFill(Color.GREEN);
        else
            ((Label)console.getChildren().get(5)).setTextFill(Color.RED);

    }

    @Override
    public void addKeyHandler() {
        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.P) {

            }
        });
    }
}
