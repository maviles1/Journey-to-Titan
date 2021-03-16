package titan;

import javafx.animation.AnimationTimer;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Renderer extends AnimationTimer {

    private long startNanoTime = 0;

    private Canvas canvas;
    ArrayList <SpaceObject> system;
    ArrayList <Double []> paths = new ArrayList<>();


    public Renderer(Canvas canvas, ArrayList<SpaceObject> system) {
        this.canvas = canvas;
        this.system = system;
    }

    @Override
    public void handle(long now) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#003366"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (startNanoTime == 0)
            startNanoTime = now;

        double t = (now - startNanoTime) / 1000000000.0;

        double x = 500 + 20 * Math.cos(t);
        double y = 500 + 20 * Math.sin(t);
        gc.setFill(Paint.valueOf("#ffffff"));

        for (int i = 0; i < system.size(); i++){
            drawSpaceObject(gc, system.get(i));
            system.get(i).update();
            for (int j = 0; j < system.size(); j++){
                if (!system.get(i).getName().equals(system.get(j).getName())){
                    system.get(i).attract(system.get(j));
                }
            }

        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(
                "t: " + t,
                50, 10
        );

//        gc.fillOval(0 + x, 0 + y, 50, 50);

    }

    public void drawSpaceObject(GraphicsContext gc, SpaceObject object){
        gc.fillOval(  gc.getCanvas().getLayoutBounds().getCenterX() + object.getPosition().getX(), gc.getCanvas().getLayoutBounds().getCenterY() + object.getPosition().getY(), 50, 50);
        gc.setFill(Paint.valueOf("#000000"));
        gc.strokeOval(gc.getCanvas().getLayoutBounds().getCenterX() + object.getPosition().getX(), gc.getCanvas().getLayoutBounds().getCenterY() + object.getPosition().getY(), 50, 50);
        for (int i = 0; i < paths.size(); i++){
            gc.fillOval(paths.get(i)[0],paths.get(i)[1],paths.get(i)[2],paths.get(i)[3]);
        }
        paths.add(new Double[]{gc.getCanvas().getLayoutBounds().getCenterX() + object.getPosition().getX(), gc.getCanvas().getLayoutBounds().getCenterY() + object.getPosition().getY(), 10.0, 10.0});
        gc.fillText(object.getName(), gc.getCanvas().getLayoutBounds().getCenterX() + object.getPosition().getX() + 6, gc.getCanvas().getLayoutBounds().getCenterY() + object.getPosition().getY() + 30);
        gc.setFill(Paint.valueOf("#ffffff"));

    }

    private static double standardize(double d){
        return d/1000000000/1000000000/7000000;
    }

    private static double unstandardize(double d){
        return d*1000000000*1000000000*7000000;
    }

}
