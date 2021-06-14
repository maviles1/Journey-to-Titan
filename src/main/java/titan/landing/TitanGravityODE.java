package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class TitanGravityODE implements ODEFunctionInterface {

    static final double GRAVITY = -1.352;

    @Override
    public RateInterface call(double t, StateInterface y) {
        Vector3d[] a = new Vector3d[1];
        a[0] = new Vector3d(0, GRAVITY, 0);
        return new Rate(y.getVelocities(), a);
    }
}
