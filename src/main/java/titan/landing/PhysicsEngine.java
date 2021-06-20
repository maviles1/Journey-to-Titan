package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class PhysicsEngine implements ODEFunctionInterface {

    Controller controller;
    TitanGravityODE gravityODE;
    TitanWindODE windODE;

    public PhysicsEngine(Controller controller, TitanGravityODE gravityODE, TitanWindODE windODE) {
        this.controller = controller;
        this.gravityODE = gravityODE;
        this.windODE = windODE;
    }

    @Override
    public RateInterface call(double t, StateInterface y) {

        LandingRate gravityRate = (LandingRate) gravityODE.call(t, y);

        LandingRate windRate = (LandingRate) windODE.call(t, y);

        LandingRate thrustRate = (LandingRate) controller.thrust(windRate, y);
        //LandingRate thrustRate = (LandingRate) controller.thrust(gravityRate, y);

        //LandingRate all = gravityRate.add(windRate).add(thrustRate);
        LandingRate all = gravityRate.add(thrustRate);
        return all;
    }

    @Override
    public PhysicsEngine clone() {
        return new PhysicsEngine(controller.clone(), new TitanGravityODE(), new TitanWindODE());
    }
}
