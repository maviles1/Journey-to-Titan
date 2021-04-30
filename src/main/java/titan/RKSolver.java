package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;
import titan.interfaces.StepInterface;

public class RKSolver implements StepInterface {
    /**
     * 4th order Runge-Kutta implementation
     * Potential RQ: does choice of constants for RK solver affect accuracy?
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
    }

    public StateInterface xstep(ODEFunctionInterface f, double t, StateInterface y, double h) {

        State s = ((State) y).copy();
        //     System.out.println("s; " + s.toString());

        Rate r1 = ((Rate) f.call(t,((State) y).copy())).mul(h); //h might not always be an int here
        //     System.out.println("r1; " + r1.toString());

        Rate r2 = ((Rate) f.call(t+(1/3.0*h) ,
                ((State) y).copy().addMul(1/3.0,(RateInterface) r1))).mul(h);
        //     System.out.println("r2: " + r2.toString());

        Rate r3 = ((Rate) f.call(t+((2/3.0*h)),
                ( ((State) y).copy().addMul(-1/3.0,(RateInterface) r1) )
                        .addMul(1.0, (r2)) ) ).mul(h);
        //     System.out.println("r3: " + r3.toString());

        Rate r4 = ((Rate) f.call(t+h,
                ((State) y).copy().addMul(1.0,(RateInterface) r1)
                        .addMul(-1.0, r2)
                        .addMul(1.0, r3))).mul(h);
        //     System.out.println("r4: " + r4.toString());
        //    System.out.println("y: " + y.addMul( 0.125, ( (r1.add(r2.mul(3.0))).add(r3.mul(3.0)) ).add(r4) ));

        return y.addMul( 0.125, ( (r1.add(r2.mul(3.0))).add(r3.mul(3.0)) ).add(r4) );
        //r1.add(r2.mul(3).add(r3.mul(3).add(r4))).mul((double)1/8)); //f(t,y) + h*state

///        RateInterface r = f.call(t, y);
///        return y.addMul(h, r);
    }
}
