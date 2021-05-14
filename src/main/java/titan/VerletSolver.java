package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class VerletSolver implements StepInterface {

    /**
     * This method is also based on the velocity verlet algorithm and implements
     * the velocity verlet but in a different way. Not sure what the differences between
     * this one and the above one are, they could be logically equivalent
     * @param f
     * @param t
     * @param y
     * @param h
     * @return
     */
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        Rate rate = (Rate) f.call(t, y);
        int size = rate.getRatePosition().length;
        State state  = (State) y;

        Vector3d[] pos = new Vector3d[size];
        for(int i = 0 ; i < size; i++){
            //x(t + ∆t) = x(t) * ∆t + 1/2 * a(t) * ∆t^2
            pos[i] = state.getPosition()[i].addMul(h, state.getVelocities()[i]).addMul(0.5 * h * h, rate.getRateVelocity()[i]);
        }

        State nextState = new State(pos, state.getVelocities(), t + h);
        Rate newRate = (Rate)f.call(t + h, nextState);
        Vector3d[] accel = newRate.getRateVelocity(); //a(t + ∆t)

        Vector3d[] vel = new Vector3d[size];
        for (int i = 0; i < size; i++) {
            //v(t + ∆t) = v(t) + 1/2*(a(t) + a(t + ∆t))*∆t
            vel[i] = state.getVelocities()[i].addMul(0.5 * h, rate.getRateVelocity()[i].add(accel[i]));
        }

        return new State(pos, vel, t + h);
    }

    public static StepInterface getV2() {
        return new V2();
    }

}

class V2 implements StepInterface {

    /**
     * This method implements the half-step(?) velocity verlet algorithm
     * https://en.wikipedia.org/wiki/Verlet_integration#Velocity_Verlet
     * implements steps 1-4 of the "standard implementation"
     * @param f
     * @param t
     * @param y
     * @param h
     * @return next state
     */
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        Rate rate = (Rate) f.call(t, y);    //to get the acceleration
        int size = rate.getRatePosition().length;
        State state  = (State) y;

        Vector3d[] vel = new Vector3d[size];
        for (int i = 0; i < size; i++){
            //v(t + 1/2*∆t) = v(t) + 1/2 * a(t) * ∆t
            vel[i] = state.getVelocities()[i].addMul((h*0.5), rate.getRateVelocity()[i]); //calculate half step velocity
        }

        Vector3d[] pos = new Vector3d[size];
        for (int i = 0; i < size; i++) {
            //x(t + ∆t) = x(t) + v(t + 1/2*∆t) * ∆t
            pos[i] = state.getPosition()[i].addMul(h, vel[i]);  //calculate new position
        }

        State nextState = new State(pos, state.getVelocities(), t + h); //to get the new acceleration

        Rate newRate = (Rate) f.call(t + h, nextState); //find new acceleration
        Vector3d[] accel = newRate.getRateVelocity(); //a(t + ∆t)

        Vector3d[] newV = new Vector3d[size];
        for (int i = 0; i < size; i++) {
            //v(t + ∆t) = v(t + 1/2*∆t) + 1/2 * a(t + ∆t) * ∆t
            newV[i] = vel[i].addMul(0.5 * h, accel[i]); //calculate full-step velocity
        }

        return new State(pos, newV, t + h);
    }
}
