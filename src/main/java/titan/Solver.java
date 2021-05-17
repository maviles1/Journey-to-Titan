package titan;

import titan.interfaces.*;

public class Solver implements ODESolverInterface {

    private StepInterface stepFunction;

    public Solver() {
//        stepFunction = new VerletSolver();
        stepFunction = new EulerSolver();
        //stepFunction = new RKSolver();
    }

    public Solver(StepInterface stepFunction) {
        this.stepFunction = stepFunction;
    }

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

        //((State) y0).getVelocities()[11] = new Vector3d(5.427193405797901e+03, -2.931056622265021e+04, 6.575428158157592e-01);
        //initial thrust
        //thrust(new Vector3d(4028.92995 *2, -4157.09400 * 4, -58.73099*3), (State)y0);
//        thrust(new Vector3d(100,100,100), (State)y0);

        double t = 0;
        for (int i = 1; i < size - 1; i++) {
            s[i] = step(f, t, s[i - 1], h);
            t += h;
        }

        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        return stepFunction.step(f, t, y, h);
    }

//    public void thrust(Vector3d thrustVector, State state) {
//        //Total mass
//        //double totalmass = State.mass[11] + state.probeFuelMass;
//        Vector3d oldvel = state.getVelocities()[11];
//        Vector3d newvel = oldvel.add(thrustVector);
//
//        double finalForce = totalmass * Math.abs(newvel.norm() - oldvel.norm());
//
////        System.out.println("TotalMass: " + totalmass);
////        System.out.println("Fuelmass: " + state.probeFuelMass);
////        System.out.println("probemass: " + State.mass[11]);
//        //        System.out.println("newvel: " + newvel.magnitude());
//        //        System.out.println("oldvel: " + oldvel.magnitude());
//        //        System.out.println("vectormag: " + thrustVector.magnitude());
//        //        System.out.println("before-> F: " + FinalForce);
//        double usedmass = finalForce / newvel.norm();
//                //System.out.println("after-> m: " + usedmass);
//
//        state.probeFuelMass -= usedmass;
//        //System.out.println("PROBEFUELMASS " + state.probeFuelMass);
//
//        state.getVelocities()[11] = state.getVelocities()[11].add(thrustVector);
//    }

}
