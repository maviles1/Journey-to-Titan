package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class EulerSolver implements StepInterface {
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        RateInterface r = f.call(t, y);
        return y.addMul(h, r); //h*f(t,y)
    }
}
