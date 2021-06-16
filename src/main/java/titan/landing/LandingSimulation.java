package titan.landing;

import javafx.scene.Scene;
import javafx.stage.Stage;
import titan.flight.Solver;
import titan.flight.Vector3d;
import titan.interfaces.StateInterface;
import titan.ui.TitanView;

public class LandingSimulation {

    public LandingSimulation() {
        //shuttle pos, vel
        //starting wind  will be created after method run is called
        //distance to titan (surface)

    }

    public void run() {
        //Run landing simulation
        //choose controller
        //create states

        //default dimensions of canvas is 150000m x 150000m (scaled down to 3000px x 3000px)
        double y = 150000; //150km
        int x = (150000) / 2; //center
        LandingState y0 = new LandingState(new Vector3d(x, y, 0), new Vector3d(0, 0, 0), new Vector3d(0,1,0), new Vector3d(1,0,0), 0);

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
}
