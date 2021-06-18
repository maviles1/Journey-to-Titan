package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.landing.*;

public class TitanWindODE implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {

        LandingState state = (LandingState) y;
        double altitute = state.getPosition().getY();

        //Sams old vector
        double x = Math.random();
        x *= Math.random() < 0.5 ? 1 : -1;

        Vector3d windVector = new Vector3d(x,0,0);

        Vector3d a = windVector;

        //using windmodel
        WindModel wm = new WindModel();
        Vector3d newwindvec = new Vector3d();
        System.out.println(altitute);

        newwindvec = wm.CreateNewWindVector(state.getPrevWindVector(), altitute);

        Vector3d forceVector = wm.getForceVector(newwindvec, altitute);
        System.out.println("Wind vec:" + newwindvec);

        return new LandingRate(state.getVelocity(), forceVector, state.getShuttle_direction(), state.getWind_direction(), newwindvec, 0);

    }
}
