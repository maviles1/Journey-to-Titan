package titan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

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

    View view;
    Renderer renderer;

    public Window(View view) {
        this.view = view;
    }

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

    int oldOffset = 1;

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
