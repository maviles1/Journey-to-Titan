package titan;

import titan.interfaces.ODEFunctionInterface;
import titan.interfaces.ODESolverInterface;
import titan.interfaces.RateInterface;
import titan.interfaces.StateInterface;

public class Solver implements ODESolverInterface {
    /*
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
        //TODO
        int stepSize = 1000;
        StateInterface[] s = new StateInterface[ts.length];
        s[0] = step(f, ts[0], y0, ts[0]);
        for (int i = 1; i < ts.length; i++) {
            s[i] = step(f, ts[i], s[i - 1], stepSize);
        }
        return s;
    }

    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h) {
        //check if t should be the total time
        int size = (int) Math.round((tf / h) + 1.5);
        StateInterface[] s = new StateInterface[size];
        s[0] = y0;

        double t = 0;
        for (int i = 1; i < size - 1; i++) {
            t += h;
            s[i] = step(f, t, s[i - 1], h);
        }

        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
    }



    //@Override
    //marie & lou's vivacious verlet
    public StateInterface ystep(ODEFunctionInterface f, double t, StateInterface y, double h) {
        RateInterface r = f.call(t, y);
        System.out.println(((State) y).getVelocities()[3]);
        Rate rate = (Rate) r;
        int size = rate.getRatePosition().length;
        Vector3d[] acc = new Vector3d[size];
        Vector3d[] vel = new Vector3d[size];
        for(int i=0;i<size;i++){
            vel[i] = rate.getRatePosition()[i].addMul((h*0.5) ,rate.getRateVelocity()[i]); //change in position == velocity
        }

        //computing the new acceleration : newAcc = function(newPosition)
        State newY = ((State) y).copy();
        //the new positions
        newY.setPositions(h,vel);
        //calculates the new rates to retrieve the acceleration
        RateInterface newR = f.call(t,newY);
        Rate newRate = (Rate) newR;
        //the acceleration at time t+h
        Vector3d[] newAcc = newRate.getRateVelocity();

        for(int i=0;i<size;i++){
            acc[i] = newAcc[i].add(rate.getRateVelocity()[i]).mul(0.5); //change in velocity == acceleration
        }

        RateInterface verletRate = new Rate(vel,acc);

        return y.addMul(h, verletRate);
    }

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
    public StateInterface pstep(ODEFunctionInterface f, double t, StateInterface y, double h) {
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
