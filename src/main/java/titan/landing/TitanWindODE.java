package titan.landing;

import titan.flight.Rate;
import titan.flight.Vector3d;
import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class TitanWindODE implements ODEFunctionInterface {

    @Override
    public RateInterface call(double t, StateInterface y) {

        double altitute = y.getPositions()[0].getY();

        double x = Math.random();
        x *= Math.random() < 0.5 ? 1 : -1;

        Vector3d windVector = new Vector3d(x,0,0);

        Vector3d[] a = new Vector3d[1];
        a[0] = windVector;

        return new Rate(y.getVelocities(), a);

    }
}
