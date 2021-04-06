package titan.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import titan.Renderer;
import titan.ui.View;

/**
 * This class represents the application window which contains the simulation controls as well as the renderer.
 * This class is also the controller for the window.resources.fxml file, which specifies the layout
 */
public class Window extends AnchorPane {

    @FXML
    private Button speedPlus;

    @FXML
    private Button resetSpeed;

    @FXML
    private Button speedMinus;

    @FXML
    private Button zoomPlus;

    @FXML
    private Button resetZoom;

    @FXML
    private Button zoomMinus;

    @FXML
    private AnchorPane viewPane;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button playBtn;

    @FXML
    private Button rewindBtn;

    private View view;
    private Renderer renderer;

    private int oldOffset = 1;  //variable used to track the speed offset used for play/pausing simulation


    /**
     * Constructor for the Window object
     * @param view View class to be used
     */
    public Window(View view) {
        this.view = view;
    }

    /**
     * Initialize the javafx nodes. Can't be done in the constructor because JavaFX is annoying that way
     */
    public void init() {
        viewPane.getChildren().add(view);
    }

    public void attachRenderer(Renderer r) {
        this.renderer = r;
    }

    @FXML
    void decreaseSpeed(ActionEvent event) {
        renderer.setSpeedOffset(renderer.getSpeedOffset() - 10);
    }

    @FXML
    void increaseSpeed(ActionEvent event) {
        renderer.setSpeedOffset(renderer.getSpeedOffset() + 10);
    }

    @FXML
    void resetSpeed(ActionEvent event) {
        renderer.setSpeedOffset(1);
    }

    @FXML
    void resetZoom(ActionEvent event) {
    }

    @FXML
    void zoomIn(ActionEvent event) {
        view.zoomIn();
    }

    @FXML
    void zoomOut(ActionEvent event) {
        view.zoomOut();
    }


    @FXML
    void pauseSimulation(ActionEvent event) {
        oldOffset = renderer.getSpeedOffset();
        renderer.setSpeedOffset(0);
    }

    @FXML
    void playSimulation(ActionEvent event) {
        renderer.setSpeedOffset(oldOffset);
    }

    @FXML
    void rewindSimulation(ActionEvent event) {
        renderer.setSpeedOffset(-10);
    }

}
