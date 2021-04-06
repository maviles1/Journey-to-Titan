package titan.ui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;

import java.io.File;
import java.net.MalformedURLException;

public class View extends ScrollPane {
    private double scaleValue = 1.0;    //initial scale
    private final double DELTA = 1.05;  //how much to change the scale by
    private Canvas canvas;
    private Group zoomGroup;
    private Scale scaleTransform;

    public View(Canvas canvas) throws MalformedURLException {
        //some basic styling
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        this.setStyle("-fx-background-color: #000000;");

        this.canvas = canvas;

//        this.setFitToWidth(true);
//        this.setFitToHeight(true);
        this.setPannable(true);

        //wrap content in Group that can be scaled
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(canvas);

        //Hide the scroll bars
        this.setContent(contentGroup);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);

        //position scoll bars in center
        setVvalue(0.5);
        setHvalue(0.5);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        //Zoomhandler class will handle the zooming with mouse scroll wheel
        class ZoomHandler implements EventHandler<ScrollEvent> {

            /**
             * Handles zooming by mouse wheel/scrolling. Finds the new scaleValue and applies it.
             * @param scrollEvent Event object fired on Scroll
             */
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() < 0)
                    scaleValue /= DELTA;    //smooth zoom out
                else
                    scaleValue *= DELTA;    //smooth zoom in

                double centerPosX = (zoomGroup.getLayoutBounds().getWidth() - getViewportBounds().getWidth())  * getHvalue() /*+ getViewportBounds().getWidth()*/  / 2;
                double centerPosY = (zoomGroup.getLayoutBounds().getHeight() - getViewportBounds().getHeight())  * getVvalue() /*+ getViewportBounds().getHeight()*/  / 2;

                //When zooming out, zoomGroup should only be zoomed until the entire content fits in the Viewport.
                //This prevents the content from being zoomed out of existence
                double nscale = Math.max(scaleValue, Math.min(getViewportBounds().getWidth() / zoomGroup.getLayoutBounds().getWidth(),
                        getViewportBounds().getHeight() / zoomGroup.getLayoutBounds().getHeight()));


                double newCenterX = centerPosX * nscale;
                double newCenterY = centerPosY * nscale;

                setHvalue((newCenterX - getViewportBounds().getWidth()/2) / (zoomGroup.getLayoutBounds().getWidth() * scaleValue - getViewportBounds().getWidth()));
                setVvalue((newCenterY - getViewportBounds().getHeight()/2) / (zoomGroup.getLayoutBounds().getHeight() * scaleValue  -getViewportBounds().getHeight()));

                //apply new scale
                scaleTransform.setX(nscale);
                scaleTransform.setY(nscale);
                scrollEvent.consume();
            }
        }

        //make sure that ScrollPane doesn't use ScrollEvent to pan
        addEventFilter(ScrollEvent.ANY, new ZoomHandler());

    }

    /**
     * Method used by the zoom in button in order to scale the canvas
     */
    public void zoomIn() {
        scaleValue *= DELTA;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.scale(scaleValue, scaleValue);
    }

    /**
     * Method used by zoom out button in order to scale the canvas
     */
    public void zoomOut() {
        scaleValue /= DELTA;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.scale(scaleValue, scaleValue);
    }

    public void centerViewOn(double x, double y){
        double viewportWidth    = getViewportBounds().getWidth();
        double maxHscrollPixels = zoomGroup.getLayoutBounds().getWidth() - viewportWidth;
        double hscrollPixels    =  (x + 0.5) * zoomGroup.getLayoutBounds().getWidth() / zoomGroup.getLayoutBounds().getWidth() - viewportWidth / 2;
        setHvalue(hscrollPixels / maxHscrollPixels);

        double viewportHeight   = getViewportBounds().getHeight();
        double maxVscrollPixels = zoomGroup.getLayoutBounds().getHeight() - viewportHeight;
        double vscrollPixels    =  (y + 0.5) * zoomGroup.getLayoutBounds().getHeight() / zoomGroup.getLayoutBounds().getHeight() - viewportHeight / 2;
        setVvalue(vscrollPixels / maxVscrollPixels);
    }

    public Canvas getCanvas() {
        return this.canvas;
    }
}