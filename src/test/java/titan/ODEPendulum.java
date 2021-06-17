package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class ODEPendulum implements ODEFunctionInterface {
    @Override
    public RateInterface call(double t, StateInterface y) {
        State state = (State) y;
        double g = 9.80665;
        double l = 1;
        double b = 0.05; // damping factor
        double m = 1; //mass of pendulum

        double angularPos = state.getVelocities()[0].getY();
        double angularVel = -(b/m) * angularPos - (g/l) * Math.sin(state.getPositions()[0].getY());

        Vector3d v = new Vector3d(t, angularPos, 0);
        Vector3d a = new Vector3d(t, angularVel, 0);
        //System.out.println(angularVel);
        //System.out.println(a);

        return new Rate(new Vector3d[]{v}, new Vector3d[]{a});
    }
}
