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



    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        RateInterface r = f.call(t, y);
        System.out.println(((State) y).getVelocities()[3]);
        Rate rate = (Rate) r;
        int size = rate.getRatePosition().length;
        Vector3d[] acc = new Vector3d[size];
        Vector3d[] vel = new Vector3d[size];
        for(int i=0;i<size;i++){
            vel[i] = rate.getRatePosition()[i].addMul((double) (h*0.5) ,rate.getRateVelocity()[i]); //change in position == velocity
        }

        // computing the new acceleration : newAcc = function(newPosition)
        State newY = ((State) y).copy();
        newY.setPositions(h,vel);
        RateInterface newR = f.call(t,newY);
        Rate newRate = (Rate) newR;
        Vector3d[] newAcc = newRate.getRatePosition();

        for(int i=0;i<size;i++){
            acc[i] = ( newAcc[i].add(rate.getRateVelocity()[i]) ).mul((double) 0.5); //change in velocity == acceleration
        }

        RateInterface verletRate = new Rate(vel,acc);

        return y.addMul(h, verletRate);
    }



    public StateInterface xstep(ODEFunctionInterface f, double t, StateInterface y, double h) {

        State s = ((State) y).copy();
        //     System.out.println("s; " + s.toString());

        Rate r1 = ((Rate) f.call(t,((State) y).copy())).mul(h); //h might not always be an int here
        //     System.out.println("r1; " + r1.toString());

        Rate r2 = ((Rate) f.call(t+(0.33333333333333333333333333333*h) ,
                ((State) y).copy().addMul(0.333333333333333333333333333,(RateInterface) r1))).mul(h);
        //     System.out.println("r2: " + r2.toString());

        Rate r3 = ((Rate) f.call(t+((0.666666666666666666666666*h)),
                ( ((State) y).copy().addMul(-0.33333333333333333333333333,(RateInterface) r1) )
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
