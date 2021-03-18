package titan;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;

import java.io.File;
import java.net.MalformedURLException;

public class View extends ScrollPane {
    private double scaleValue = 1.0;
    private double delta = 1.05;
    private Canvas canvas;
    private Group zoomGroup;
    private Scale scaleTransform;

    public View(Canvas canvas) throws MalformedURLException {
        AnchorPane.setBottomAnchor(this, 0.0);
        AnchorPane.setTopAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);
        AnchorPane.setRightAnchor(this, 0.0);
        this.setStyle("-fx-background-color: #000000;");

        this.canvas = canvas;

//        this.setFitToWidth(true);
//        this.setFitToHeight(true);
        this.setPannable(true);

        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(canvas);

        this.setContent(contentGroup);
//        canvas.widthProperty().bind(this.widthProperty());
//        canvas.heightProperty().bind(this.heightProperty());
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);

        setVvalue(0.5);
        setHvalue(0.5);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        class ZoomHandler implements EventHandler<ScrollEvent> {

            /**
             * Handles zooming by mouse wheel/scrolling. Finds the new scaleValue and applies it.
             * @param scrollEvent Event object fired on Scroll
             */
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() < 0)
                    scaleValue /= delta;    //smooth zoom out
                else
                    scaleValue *= delta;    //smooth zoom in

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

        Image img = new Image(new File("src/space.png").toURI().toURL().toExternalForm());

        canvas.getGraphicsContext2D().drawImage(img, 0, 0);

    }

    public void zoomIn() {
        scaleValue *= delta;
        double centerPosX = (zoomGroup.getLayoutBounds().getWidth() - getViewportBounds().getWidth())  * getHvalue() + getViewportBounds().getWidth()  / 2;
        double centerPosY = (zoomGroup.getLayoutBounds().getHeight() - getViewportBounds().getHeight())  * getVvalue() + getViewportBounds().getHeight()  / 2;
        double nscale = Math.max(scaleValue, Math.min(getViewportBounds().getWidth() / zoomGroup.getLayoutBounds().getWidth(),
                getViewportBounds().getHeight() / zoomGroup.getLayoutBounds().getHeight()));


        double newCenterX = centerPosX * nscale;
        double newCenterY = centerPosY * nscale;
        setHvalue((newCenterX - getViewportBounds().getWidth()/2) / (zoomGroup.getLayoutBounds().getWidth() * scaleValue - getViewportBounds().getWidth()));
        setVvalue((newCenterY - getViewportBounds().getHeight()/2) / (zoomGroup.getLayoutBounds().getHeight() * scaleValue  -getViewportBounds().getHeight()));
        scaleTransform.setX(nscale);
        scaleTransform.setY(nscale);
    }

    public void zoomOut() {
        scaleValue /= delta;
        double centerPosX = (zoomGroup.getLayoutBounds().getWidth() - getViewportBounds().getWidth())  * getHvalue() + getViewportBounds().getWidth()  / 2;
        double centerPosY = (zoomGroup.getLayoutBounds().getHeight() - getViewportBounds().getHeight())  * getVvalue() + getViewportBounds().getHeight()  / 2;
        double nscale = Math.max(scaleValue, Math.min(getViewportBounds().getWidth() / zoomGroup.getLayoutBounds().getWidth(),
                getViewportBounds().getHeight() / zoomGroup.getLayoutBounds().getHeight()));


        double newCenterX = centerPosX * nscale;
        double newCenterY = centerPosY * nscale;
        setHvalue((newCenterX - getViewportBounds().getWidth()/2) / (zoomGroup.getLayoutBounds().getWidth() * scaleValue - getViewportBounds().getWidth()));
        setVvalue((newCenterY - getViewportBounds().getHeight()/2) / (zoomGroup.getLayoutBounds().getHeight() * scaleValue  -getViewportBounds().getHeight()));
        scaleTransform.setX(nscale);
        scaleTransform.setY(nscale);
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

//package titan;
//
//import javafx.event.EventHandler;
//import javafx.geometry.Bounds;
//import javafx.scene.Group;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.image.Image;
//import javafx.scene.input.ScrollEvent;
//import javafx.scene.input.ZoomEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.transform.Scale;
//
//import java.io.File;
//import java.net.MalformedURLException;
//
//public class View extends ScrollPane {
//    private double scaleValue = 1.0;
//    private double delta = 1.05;
//    private Canvas canvas;
//
//    public View(Canvas canvas) throws MalformedURLException {
//        AnchorPane.setBottomAnchor(this, 0.0);
//        AnchorPane.setTopAnchor(this, 0.0);
//        AnchorPane.setLeftAnchor(this, 0.0);
//        AnchorPane.setRightAnchor(this, 0.0);
//        this.setStyle("-fx-background-color: #000000;");
//
//        this.canvas = canvas;
//
////        this.setFitToWidth(true);
////        this.setFitToHeight(true);
//        this.setPannable(true);
//
//        Group contentGroup = new Group();
//        Group zoomGroup = new Group();
//        contentGroup.getChildren().add(zoomGroup);
//        zoomGroup.getChildren().add(canvas);
//
//        this.setContent(contentGroup);
//        canvas.widthProperty().bind(this.widthProperty());
//        canvas.heightProperty().bind(this.heightProperty());
//        this.setHbarPolicy(ScrollBarPolicy.NEVER);
//        this.setVbarPolicy(ScrollBarPolicy.NEVER);
//
//        Scale scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
//        zoomGroup.getTransforms().add(scaleTransform);
//
//        class ZoomHandler implements EventHandler<ScrollEvent> {
//
//            /**
//             * Handles zooming by mouse wheel/scrolling. Finds the new scaleValue and applies it.
//             * @param scrollEvent Event object fired on Scroll
//             */
//            @Override
//            public void handle(ScrollEvent scrollEvent) {
//                double oldScale = scaleValue;
//                if (scrollEvent.getDeltaY() < 0)
//                    scaleValue /= delta;    //smooth zoom out
//                else
//                    scaleValue *= delta;    //smooth zoom in
//
//                //get old center positions
//                double centerPosX = (zoomGroup.getLayoutBounds().getWidth() - getViewportBounds().getWidth())  * getHvalue() + getViewportBounds().getWidth()  / 2;
//                double centerPosY = (zoomGroup.getLayoutBounds().getHeight() - getViewportBounds().getHeight())  * getVvalue() + getViewportBounds().getHeight()  / 2;
//
//                //When zooming out, zoomGroup should only be zoomed until the entire content fits in the Viewport.
//                //This prevents the content from being zoomed out of existence
//                double nscale = Math.max(scaleValue, Math.min(getViewportBounds().getWidth() / zoomGroup.getLayoutBounds().getWidth(),
//                        getViewportBounds().getHeight() / zoomGroup.getLayoutBounds().getHeight()));
//
//                scaleTransform.setX(nscale);
//                scaleTransform.setY(nscale);
//
//                //calculate new center positions
//                double newCenterX = centerPosX * nscale;
//                double newCenterY = centerPosY * nscale;
//
//                //recenter canvas
//                //setHvalue((newCenterX - getViewportBounds().getWidth()/2) / (zoomGroup.getLayoutBounds().getWidth() * scaleValue - getViewportBounds().getWidth()));
//                //setVvalue((newCenterY - getViewportBounds().getHeight()/2) / (zoomGroup.getLayoutBounds().getHeight() * scaleValue  -getViewportBounds().getHeight()));
//
//                double  f = (oldScale / nscale)-1;
//                Bounds bounds = zoomGroup.localToScene(zoomGroup.getBoundsInLocal());
//                double dx = (scrollEvent.getSceneX() - (bounds.getWidth()/2 + bounds.getMinX()));
//                double dy = (scrollEvent.getSceneY() - (bounds.getHeight()/2 + bounds.getMinY()));
//
//                setHvalue(zoomGroup.getTranslateX()-f*dx);
//                setVvalue(zoomGroup.getTranslateY()-f*dy);
//
//                //apply new scale
//
////                scaleTransform.setX(zoomGroup.getTranslateX()-f*dx);
////                scaleTransform.setY(zoomGroup.getTranslateY()-f*dy);
//                scrollEvent.consume();
//            }
//        }
//
//        //make sure that ScrollPane doesn't use ScrollEvent to pan
//        addEventFilter(ScrollEvent.ANY, new ZoomHandler());
//        //addEventFilter(ZoomEvent.ANY, handler);
//
//        Image img = new Image(new File("src/space.png").toURI().toURL().toExternalForm());
//
//        canvas.getGraphicsContext2D().drawImage(img, 0, 0);
//
//    }
//
//    public Canvas getCanvas() {
//        return this.canvas;
//    }
//}
