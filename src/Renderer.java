import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Renderer extends AnimationTimer {

    private long startNanoTime = 0;

    private Canvas canvas;

    public Renderer(Canvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void handle(long now) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Paint.valueOf("#000000"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (startNanoTime == 0)
            startNanoTime = now;

        double t = (now - startNanoTime) / 1000000000.0;

        double x = 232 + 128 * Math.cos(t);
        double y = 232 + 128 * Math.sin(t);
        gc.setFill(Paint.valueOf("#ffffff"));

        gc.fillOval(0 + x, 0 + y, 50, 50);
    }
}
