package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class TitanWindODE implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {

        LandingState state = (LandingState) y;
        double altitute = state.getPosition().getY();

        double x = Math.random();
        x *= Math.random() < 0.5 ? 1 : -1;

        Vector3d windVector = new Vector3d(x,0,0);

        Vector3d a = windVector;

        return new LandingRate(state.getVelocity(), a, state.getShuttle_direction(), state.getWind_direction(), 0);

    }
}
