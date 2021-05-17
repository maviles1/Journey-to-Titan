package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class RKSolver implements StepInterface {
    /**
     * classical 4th order Runge-Kutta implementation
     * @param f
     * @param t
     * @param y
     * @param h
     * @return
     */
    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        State state = (State) y; //w
        Rate stateRate = new Rate(state.getPosition(), state.getVelocities()); //also w

        Rate r1 = ((Rate) f.call(t, y)).mul(h); //k1=h*f(t,w);

        State nexState = new State(r1.mul(0.5).add(stateRate).getRatePosition(), r1.mul(0.5).add(stateRate).getRateVelocity(), t + 0.5*h); //w+k1/2
        Rate r2 = ((Rate) f.call(t + 0.5 * h, nexState)).mul(h);        //k2=h*f(t+h/2,w+k1/2);

        State nexState2 = new State(r2.mul(0.5).add(stateRate).getRatePosition(), r2.mul(0.5).add(stateRate).getRateVelocity(), t + 0.5*h); //w+k2/2
        Rate r3 = ((Rate) f.call(t + 0.5 * h, nexState2)).mul(h);         //k3 = h*f(t+h/2,w+k2/2);

        State nexState3 = new State(r3.add(stateRate).getRatePosition(), r3.add(stateRate).getRateVelocity(), t + h); //w+k3
        Rate r4 = ((Rate) f.call(t + h, nexState3)).mul(h); //k4=h*f(t+h,w+k3);

        Rate rs = r1.add(r2.mul(2)).add(r3.mul(2)).add(r4); //k1+2*k2+2*k3+k4)
        Rate w = stateRate.add(rs.mul(1/6.0));        //w=w+(k1+2*k2+2*k3+k4)/6,

        return new State(w.getRatePosition(), w.getRateVelocity(), t + h);
        //return new State(w.getRatePosition(), w.getRateVelocity(), t + h, state.probeFuelMass);
    }

    public static StepInterface getRK2() {
        return new RK2();
    }
}

class RK2 implements StepInterface{

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        State state = (State) y;
        Rate stateRate = new Rate(state.getPosition(), state.getVelocities()); //w

        Rate r1 = ((Rate) f.call(t, y)).mul(h); //k1=hf(t,w);

        State nexState = new State(r1.mul(1.0/3.0).add(stateRate).getRatePosition(), r1.mul(1.0/3.0).add(stateRate).getRateVelocity(), t + h/3.0); //w+k1/3
        Rate r2 = ((Rate) f.call(t + h/3.0, nexState)).mul(h);        //k2=hf(t+h/3,w+k1/3);

        State nexState2 = new State(r1.mul(-1.0/3.0).add(r2).add(stateRate).getRatePosition(), r1.mul(-1.0/3.0).add(r2).add(stateRate).getRateVelocity(), t + 2*h/3.0); //w-k1/3+k2
        Rate r3 = ((Rate) f.call(t + 2 * h/3.0, nexState2)).mul(h);         //k3 = hf(t+2h/3,w-k1/3+k2);

        State nexState3 = new State(r1.add(r2.mul(-1)).add(r3).add(stateRate).getRatePosition(), r1.add(r2.mul(-1)).add(r3).add(stateRate).getRateVelocity(), t + h); //w+k1-k2+k3
        Rate r4 = ((Rate) f.call(t + h, nexState3)).mul(h); //k4=hf(t+h,w+k1-k2+k3);

        Rate rs = r1.add(r2.mul(3)).add(r3.mul(3)).add(r4); //k1+3k2+3k3+k4)
        Rate w = stateRate.add(rs.mul(0.125));        //w=w+(k1+2k2+2*k3+k4)/6,

        return new State(w.getRatePosition(), w.getRateVelocity(), t + h);
        //return new State(w.getRatePosition(), w.getRateVelocity(), t + h, state.probeFuelMass);
    }
}