package titan.flight;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class ODEFunction implements ODEFunctionInterface {
    @Override
    public RateInterface call(double t, StateInterface y) {

        State state = (State) y;

        int size=state.getPositions().length;
        double dt = t - state.getTime();

        double G = 6.67430E-11;

        Vector3d[] aRates = new Vector3d[size];
        Vector3d[] vRates = new Vector3d[size];

        for (int i = 0; i < size; i++) {
            Vector3d a = new Vector3d(0,0,0);

            for (int j = 0; j < size; j++) {
                if (i != j) {
                    double p = (G * State.mass[j])/Math.pow(state.getPositions()[j].dist(state.getPositions()[i]),3);
                    a = a.addMul(p, state.getPositions()[j].sub(state.getPositions()[i]));
                }
            }

            //Vector3d v = state.getVelocities()[i].addMul(dt,a);
            Vector3d v = state.getVelocities()[i];

            aRates[i] = a;
            vRates[i] = v;
        }

        return new Rate(vRates, aRates);
    }


}
