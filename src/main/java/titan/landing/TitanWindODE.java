package titan.landing;

import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class TitanWindODE implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {

        LandingState state = (LandingState) y;
        double altitute = state.getPosition().getY();

        //using windmodel
        WindModel wm = new WindModel();
        Vector3d newwindvec = wm.createNewWindVector(state.getPrevWindVector(), altitute);

        Vector3d forceVector = wm.getForceVector(newwindvec);

        return new LandingRate(state.getVelocity(), forceVector, state.getShuttle_direction(), state.getWind_direction(), newwindvec, 0);

    }
}
