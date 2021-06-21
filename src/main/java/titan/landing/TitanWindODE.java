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

        Vector3d newwindvec = new Vector3d(0, 0, 0);
        Vector3d forceVector = new Vector3d (0, 0, 0);

        //Wind only applies every 5 states --works really well
        if( t % 5 == 0)
        {
            newwindvec = wm.createNewWindVector(state.getPrevWindVector(), altitute);
            forceVector = wm.getForceVector(newwindvec);
        }


        return new LandingRate(state.getVelocity(), forceVector, state.getShuttle_direction(), state.getWind_direction(), newwindvec, 0);

    }
}
