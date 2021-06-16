package titan.landing;

import titan.flight.Rate;
import titan.flight.State;
import titan.flight.Vector3d;
import titan.interfaces.Controller;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class OpenLoopController implements Controller {

    @Override
    public RateInterface thrust(RateInterface windRate, StateInterface y) {
        //return predetermined thrust
        LandingState state = (LandingState) y;
        double altitude = state.getPosition().getY();
        double thru = 1.352 - altitude / 150000.0;
        double o = 0.1 / (altitude / 150000.0);
        //System.out.println(thru);

        if (altitude <= 0) {
            o = 0;
        }

        Vector3d thrust = new Vector3d(0, o, 0);

        return new LandingRate(state.getVelocity(), thrust, state.getShuttle_direction(), state.getWind_direction());
    }
}
