package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.interfaces.Controller;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

import java.util.Arrays;

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

        Rate gravityRate = (Rate) gravityODE.call(t, y);
        //State afterGravity = (State) y.addMul(1, gravityRate);

        Rate windRate = (Rate) windODE.call(t, y);
        //State afterWind = (State) afterGravity.addMul(1, windRate);

        Rate thrustRate = (Rate) controller.thrust(windRate, y);

        Rate all = gravityRate.add(windRate).add(thrustRate);

        return all;
    }
}
