package titan.landing;

import javafx.scene.Scene;
import javafx.stage.Stage;
import titan.flight.Solver;
import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.StateInterface;
import titan.ui.TitanView;

public class LandingSimulation {

    private static Vector3d landing_location;
    private static double step_size = 1;

    public LandingSimulation() {
        //shuttle pos, vel
        //starting wind  will be created after method run is called
        //distance to titan (surface)
        createLandingPosition();
    }

    public void run() {
        //Run landing simulation
        //choose controller
        //create states

        //default dimensions of canvas is 150000m x 150000m (scaled down to 3000px x 3000px)
        double y = 122200; //150km
        int x = (150000) / 2; //center
        WindModel wm = new WindModel(); //9806980
        //Vector3d iwv = new Vector3d(-0.01, -2, 0);
        Vector3d iwv = wm.getStartingWindVector(y);
        LandingState y0 = new LandingState(new Vector3d(x, y, 0),  new Vector3d(294.947, -134.15 ,0 ), new Vector3d(0,1,0), new Vector3d(1,0,0), iwv, Math.toRadians(0), 0, 0);

        Solver solver = new Solver(new LandingVerlet());
        StateInterface[] states = solver.solve(new PhysicsEngine(new OpenLoopController(), new TitanGravityODE(), new TitanWindODE()), y0, 1000, 1, true);

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

    public TitanView getTitanView(Controller cont){
        double y = 122200; //150km
        int x = (150000) / 2; //center
//        double y = 120000; //150km
//        int x = (150000) / 2; //center
        WindModel wm = new WindModel();
        LandingState y0 = new LandingState(new Vector3d(x, y, 0),  new Vector3d(294.947, -134.15 ,0 ), new Vector3d(0,1,0), new Vector3d(1,0,0), wm.getStartingWindVector(y), Math.toRadians(0), 0, 0);

        Solver solver = new Solver(new LandingVerlet());
        StateInterface[] states = solver.solve(new PhysicsEngine(cont, new TitanGravityODE(), new TitanWindODE()), y0, 3600, 1, true);

        TitanView titanView = new TitanView(states);
        return titanView;
    }

    private void createLandingPosition(){
        double x = 50000;
        double y = 0;
        double z = 0;
        landing_location = new Vector3d(x, y, z);
    }

    public static Vector3d getLandingPosition(){
        return landing_location;
    }

    public static double getStepSize() {
        return step_size;
    }
}
