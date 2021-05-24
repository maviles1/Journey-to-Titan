package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class SimpleODE implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {

        State state = (State) y;
        double g = 9.80665;

        double verticalVelocity = -g * t;
        double horizontalVelocity = state.getVelocities()[0].getX();

        double verticalAccel = -g;
        double horizontalAccel = 0;

        Vector3d v = new Vector3d(horizontalVelocity, verticalVelocity, 0);
        Vector3d a = new Vector3d(horizontalAccel, verticalAccel, 0);

        return new Rate(new Vector3d[]{v}, new Vector3d[]{a});
        //return new Rate(vel, acc);
    }
}
