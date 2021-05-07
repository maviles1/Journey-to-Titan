package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class ODEFunction implements ODEFunctionInterface {

    final double G = 6.67430E-11; //gravitational constant
    /**
     * evaluates f(ti, yi)
     * @param  y the time at which to evaluate the function
     * @param  t the state at which to evaluate the function
     * @return average rate-of-change over the time-step with dimensions of [state]/[time].
     */
    @Override
    public RateInterface call(double t, StateInterface y) {

        State state = (State) y;

        int size = state.getPosition().length;
        double step = t - state.getTime(); //the time step
        // TODO
        // System.out.println(dt);

        Vector3d[] aRates = new Vector3d[size];
        Vector3d[] vRates = new Vector3d[size];

        for (int i = 0; i < size; i++) {
            Vector3d a = new Vector3d(0,0,0);

            for (int j = 0; j < size; j++) {
                if (i != j) {
                    //change in velocity for each element
                    double force = (G * State.mass[j])/Math.pow(state.getPosition()[j].dist(state.getPosition()[i]),3);
                    if(i==3){System.out.println(force);}
                    a = a.addMul(force, state.getPosition()[j].sub(state.getPosition()[i]));
                }
            }


                Vector3d v = state.getVelocities()[i].addMul(step,a);

            aRates[i] = a;
            vRates[i] = v;
        }

        return new Rate(vRates, aRates);
    }
}
