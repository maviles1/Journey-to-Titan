package titan.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import titan.flight.*;
import titan.interfaces.StateInterface;

import java.util.ArrayList;

public class Launcher {

    public static final double DAY = 24 * 60 * 60;
    public static final double YEAR = 365.25 * DAY;

    public static final double DEFAULT_STEP_SIZE = 3600;

    public static final double[] DEFAULT_LAUNCH_VELOCITY = {0, 0, 0};
//    public static final double[] DEFAULT_LAUNCH_VELOCITY = {26775.57124009934, -57470.04719025249, -472.36369747524566};
//    public static final double[] DEFAULT_LAUNCH_VELOCITY = {40289.2995, -41570.9400, -587.3099};
    public static final double[] DEFAULT_LAUNCH_POSITION = {-3223960.8810019065, 5495059.07408366, 6558.208615016209};
//    public static final double[] DEFAULT_LAUNCH_POSITION = {6371000.0, 1.0, 1.0};

    @FXML
    private TextField xField;

    @FXML
    private TextField yField;

    @FXML
    private TextField zField;

    @FXML
    private TextField vxField;

    @FXML
    private TextField vyField;

    @FXML
    private TextField vzField;

    @FXML
    private TextField stepSizeField;

    @FXML
    private TextField durationField;

    @FXML
    private CheckBox eulerOption;

    @FXML
    private CheckBox verletOption;

    @FXML
    private CheckBox rkClassicOption;

    @FXML
    private CheckBox rkOriginalOption;

    @FXML
    private RadioButton twoD;

    @FXML
    private RadioButton threeD;

    @FXML
    private Label errorMsg;

    @FXML
    private Button launchButton;

    @FXML
    void initialize() {
        xField.setText(DEFAULT_LAUNCH_POSITION[0] + "");
        yField.setText(DEFAULT_LAUNCH_POSITION[1] + "");
        zField.setText(DEFAULT_LAUNCH_POSITION[2] + "");

        vxField.setText(DEFAULT_LAUNCH_VELOCITY[0] + "");
        vyField.setText(DEFAULT_LAUNCH_VELOCITY[1] + "");
        vzField.setText(DEFAULT_LAUNCH_VELOCITY[2] + "");

        stepSizeField.setText(DEFAULT_STEP_SIZE + "");
        durationField.setText(YEAR * 2 + ""); //TWO YEARS default duration

        ToggleGroup perspective = new ToggleGroup();
        twoD.setToggleGroup(perspective);
        threeD.setToggleGroup(perspective);

        threeD.setOnMouseClicked(event -> errorMsg.setText("Currently 3D GUI has hardcoded parameters"));
    }

    @FXML
    void launchSimulation(ActionEvent event) throws Exception {
        double posX = Double.parseDouble(xField.getText());
        double posY = Double.parseDouble(yField.getText());
        double posZ = Double.parseDouble(zField.getText());
        Vector3d pos = new Vector3d(posX, posY, posZ);

        double velX = Double.parseDouble(vxField.getText());
        double velY = Double.parseDouble(vyField.getText());
        double velZ = Double.parseDouble(vzField.getText());
        Vector3d vel = new Vector3d(velX, velY, velZ);

        double stepSize;

        if (stepSizeField.getText().equalsIgnoreCase("hour")) {
            stepSize = 3600;
        } else if (stepSizeField.getText().equalsIgnoreCase("day")) {
            stepSize = DAY;
        } else {
            stepSize = Double.parseDouble(stepSizeField.getText());
        }

        double duration = Double.parseDouble(durationField.getText());

        if (twoD.isSelected()) {
            errorMsg.setText("");
            runPolySim(pos, vel, stepSize, duration);
        } else if (threeD.isSelected()){
            //3d simulation
            //TODO: Be able to allow users to select the solver to use instead of hardcoding
            GUI3D gui3D = new GUI3D();
            Stage stage = new Stage();
            stage.setTitle("Mission Titan");
            stage.getIcons().add(new Image("titan.png"));
            Scene s = gui3D.start(stage);
            stage.setScene(s);
            stage.show();
            stage.setOnCloseRequest(event1 -> {
                //TODO: somehow call the stop() method on the AnimationTimers used in order to prevent memory leak
                errorMsg.setText("");
            });
        } else {
                //dummy states for demo reasons
                ArrayList<StateInterface> generatedStates = new ArrayList<>();
                double y = 150000;
                int x = 600;
                State y0 = new State(new Vector3d[]{new Vector3d(x, y, 0)}, new Vector3d[]{new Vector3d(0, 1, 0)}, 0);
                generatedStates.add(y0);
                for (int i = 1; y > 0; i++) {
                    State prevState = (State) generatedStates.get(i - 1);
                    Vector3d[] poses = new Vector3d[]{new Vector3d(x, prevState.getPositions()[0].getY() - prevState.getVelocities()[0].getY(), 0)};
                    Vector3d[] veles = new Vector3d[]{new Vector3d(0, prevState.getVelocities()[0].getY() + 1, 0)};
                    generatedStates.add(new State(poses, veles, i));
                    y = poses[0].getY();
                }
                StateInterface[] states = generatedStates.toArray(new StateInterface[0]);
                System.out.println(states.length);
                TitanView titanView = new TitanView(states);
                Stage stage = new Stage();
                stage.setScene(new Scene(titanView.getParent(), 1200, 800));
                stage.show();
                titanView.start();

                stage.setOnCloseRequest(event2 -> {
                    titanView.stop();
                    stage.close();
                });

        }
    }

    public void runPolySim(Vector3d pos, Vector3d vel, double stepSize, double duration) {
        //Vector3dInterface probe_relative_position = new Vector3d(6371e3, 0, 0);
        //Vector3dInterface probe_relative_velocity = new Vector3d(52500.0, -27000.0, 0);// 12.0 months

        ArrayList<StateInterface[]> simStates = new ArrayList<>();
        ArrayList<String> probeNames = new ArrayList<>();

        if (eulerOption.isSelected()) {
//            ProbeSim eulerSim = new ProbeSim(new Solver(new EulerSolver()));
            ProbeSimulator eulerSim = new ProbeSimulator();
            eulerSim.setSolver(new Solver(new EulerSolver()));

            //eulerSim.trajectory(probe_relative_position, probe_relative_velocity, YEAR, DAY);
            eulerSim.trajectory(pos, vel, duration, stepSize);
            simStates.add(eulerSim.getStates());
            probeNames.add("Euler");
        }

        if (verletOption.isSelected()) {
            //ProbeSim verletSim = new ProbeSim(new Solver(new VerletSolver()));
            ProbeSimulator verletSim = new ProbeSimulator();
            verletSim.setSolver(new Solver(new VerletSolver()));

            //verletSim.trajectory(probe_relative_position, probe_relative_velocity, YEAR, DAY);
            verletSim.trajectory(pos, vel, duration, stepSize);
            simStates.add(verletSim.getStates());
            probeNames.add("Verlet");

//            ProbeSim v2Sim = new ProbeSim(new Solver(VerletSolver.getV2()));
//            //v2Sim.trajectory(probe_relative_position, probe_relative_velocity, YEAR, DAY);
//            v2Sim.trajectory(pos, vel, duration, stepSize);
//            simStates.add(v2Sim.getStates());
//            probeNames.add("V2");
        }

        if (rkClassicOption.isSelected()) {
//            ProbeSim rkSim = new ProbeSim(new Solver(new RKSolver()));
            ProbeSimulator rkSim = new ProbeSimulator();
            rkSim.setSolver(new Solver(new RKSolver()));

            //rkSim.trajectory(probe_relative_position, probe_relative_velocity, YEAR, DAY);
            rkSim.trajectory(pos, vel, duration, stepSize);
            simStates.add((rkSim.getStates()));
            probeNames.add("RK Classic");
        }

        if (rkOriginalOption.isSelected()) {
//            ProbeSim rk2 = new ProbeSim(new Solver(RKSolver.getRK2()));
            ProbeSimulator rk2 = new ProbeSimulator();
            rk2.setSolver(new Solver(RKSolver.getRK2()));

            //rk2.trajectory(probe_relative_position, probe_relative_velocity, YEAR, DAY);
            rk2.trajectory(pos, vel, duration, stepSize);
            simStates.add((rk2.getStates()));
            probeNames.add("RK Original");
        }

        PolySim polySim = new PolySim(simStates);
        polySim.setProbeNames(probeNames.toArray(new String[0]));

        Stage stage = new Stage();
        stage.setScene(new Scene(polySim.getParent(), 1200, 800));
        stage.show();
        polySim.start();

        stage.setOnCloseRequest(event -> {
            polySim.stop();
            stage.close();
        });
    }
}
