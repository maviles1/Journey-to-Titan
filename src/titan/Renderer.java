package titan;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.ArrayList;

public class Renderer extends AnimationTimer {

    private long startNanoTime = 0;

    private Canvas canvas;
    ArrayList <SpaceObject> system;
    ArrayList <Double []> paths = new ArrayList<>();
    State state;
    int count = 0;
    StateInterface [] s1;



    public Renderer(Canvas canvas, ArrayList<SpaceObject> system) {
        this.canvas = canvas;
        this.system = system;
    }

    public Renderer(Canvas canvas, ArrayList<SpaceObject> system, State state) {
        this.canvas = canvas;
        this.state = state;
        Solver solver = new Solver();
        ODEFunction func = new ODEFunction();
        this.s1 = solver.solve(func, state, 315569260, 10000);
    }

    @Override
    public void handle(long now) {
        Button button = new Button("PENIS");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (startNanoTime == 0)
            startNanoTime = now;

        double t = (now - startNanoTime) / 1000000000.0;
        double x = 500 + 20 * Math.cos(t);
        double y = 500 + 20 * Math.sin(t);
        gc.setFill(Paint.valueOf("#469FCB"));
        for (int i = 0; i < state.positions.length; i++){
            drawSpaceObject(gc, state.positions[i],i);
        }

        gc.setTextAlign(TextAlignment.CENTER );
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(
                "EARTH DISTANCE FROM SUN: " + state.positions[0].dist(state.positions[1]),
                200, 10
        );
        gc.fillText(
                "EARTH COORDINATES ON CANVAS: " + state.positions[1].mul((1/(1e11))*300).toString(),
                400, 30
        );
        gc.fillText(
                "SUN COORDINATES ON CANVAS: " + state.positions[0].mul((1/(1e11))*300).toString(),
                400, 45
        );


//        state.positions = func.call(t,state).getRatePosition();
//        for (int i = 0; i < state.positions.length; i++){
//            state.positions[i] = state.positions[i].add(func.call(t,state).getRatePosition()[i]);
//        }
//        state = (State) solver.step(func, t, state, 1);
//        ODEFunction func = new ODEFunction();
//        double[] ts = new double[]{0, 31556926};
//        Solver solver = new Solver();
////        StateInterface[] s = solver.solve(func, state, ts);
//        //   System.out.println(s[1]);
////        StateInterface[] s1 = solver.solve(func, state, tf, 100000);
////        System.out.println(s1[count]);
//        state = (State) s1[count];


        state = (State) s1[count];
        count++;
//        state = (State) solver.step(func, t, state, 1000000);
    }

    public void drawSpaceObject(GraphicsContext gc,Vector3d vec, int index){
        gc.fillOval(  gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()),toScreenRadius(State.radius[index]), toScreenRadius(State.radius[index]));
        gc.setFill(Paint.valueOf("#CC52D7"));
        gc.strokeOval(gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()), toScreenRadius(State.radius[index]), toScreenRadius(State.radius[index]));
        for (int i = 0; i < paths.size(); i++){
            gc.fillOval(paths.get(i)[0],paths.get(i)[1],paths.get(i)[2],paths.get(i)[3]);
        }
        paths.add(new Double[]{gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX()) + toScreenRadius(toScreenCoordinates(vec.getX())), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY()) + toScreenRadius(toScreenCoordinates(vec.getY())), 1.0, 1.0});
        gc.setFill(Paint.valueOf("#DBF188"));
        gc.fillText(state.names.get(index), gc.getCanvas().getLayoutBounds().getCenterX() + toScreenCoordinates(vec.getX() + 6), gc.getCanvas().getLayoutBounds().getCenterY() + toScreenCoordinates(vec.getY() + 30));
        gc.setFill(Paint.valueOf("#ffffff"));
    }


    public static double toScreenCoordinates(double d){
        return ((d/(1e12)) * 100);
    }

    public static double toScreenRadius(double d){
        System.out.println(d);
        return d/10000;
    }

}