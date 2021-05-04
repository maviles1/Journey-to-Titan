package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;


public class EulerSolver implements StepInterface {

    /**
     * @param   f   the function defining the differential equation dy/dt=f(t,y)
     * @param   t   the time
     * @param   y   the current state
     * @param   h   the step size
     * @return  the new state after taking one step
     */
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        RateInterface r = f.call(t, y);
        return y.addMul(h, r); //y(t+1) = y(t) + h*f(t,y)
    }
}
