package titan;
import java.util.Random;

import titan.interfaces.*;

public class Solver implements ODESolverInterface {

    private StepInterface stepFunction;

    Probe probe;
    double [] probeMass;

    public Solver() {
        stepFunction = new VerletSolver();
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
        double [] probeMass = new double[size];
        s[0] = y0;
        probe = new Probe("S",15, y0.getPositions()[y0.getVelocities().length - 1],  new Vector3d(0,0,0));
        Vector3d initialThrust = s[0].getVelocities()[11];
        s[0].getVelocities()[11] = new Vector3d(0,0,0);
        //probe.setFuelMass(1000);
        double t = 0;
        //initial thrust
        thrust(probe,s[0], initialThrust);
        for (int i = 1; i < size - 1; i++) {
            t += h; //TODO: i think we should be incrementing t at the end of the loop
            s[i] = step(f, t, s[i - 1], h);
            probe.setPosition(s[i].getPositions()[11]);
            probe.setVelocity(s[i].getVelocities()[11]);
            probeMass[i] = probe.getFuelMass();
            for (int d = 0; d < 11; d++){
//                System.out.println(d + ": " + isInOrbit(s[i].getVelocities()[d], s[i].getVelocities()[0], State.mass[d],State.mass[0], s[i].getPositions()[d],s[i].getPositions()[0], h ));
            }
        }
        this.probeMass = probeMass;

        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
    }

    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double tf, double h, boolean test) {
        //check if t should be the total time
        int size = (int) Math.round((tf / h) + 1.5);
        StateInterface[] s = new StateInterface[size];
        s[0] = y0;
        double t = 0;
        for (int i = 1; i < size - 1; i++) {
            s[i] = step(f, t, s[i - 1], h);
            t += h; //TODO: i think we should be incrementing t at the end of the loop
        }

        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
    }

    public double [] getProbeMass(){
        return probeMass;
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {
        return stepFunction.step(f, t, y, h);
    }


    public void thrust(Probe probe, StateInterface state, Vector3d toThrust){
        probe.thrust(toThrust);
        state.getVelocities()[11] = probe.getVelocity();
    }

    public Vector3d randomV(){
        Random rand = new Random();
        return new Vector3d(rand.nextDouble()*100, rand.nextDouble()*100, rand.nextDouble()*100);
    }

    public boolean randomAdd(){
        Random rand = new Random();
        int r = rand.nextInt()*100;
        return r < 10;
    }

    public boolean isInOrbit(Vector3d vel1, Vector3d vel2, double m1, double m2, Vector3d p1, Vector3d p2, double t){
        double dist = p1.dist(p2);
        double sqrDst = Math.sqrt(p2.sub(p1).norm());
        Vector3d forceDir = (p2.sub(p1)).normalize();
        Vector3d force = forceDir.mul(6.67430E-11 * m1 * m2/sqrDst);
//        Vector3d acceleration = force.mul(1/getMass());
        boolean isPerpendicular = Math.abs(forceDir.dot(vel1)) <= 50;
        if (!isPerpendicular){
            System.out.println( Math.abs(force.dot(vel1)));
            return false;
        }
        else{
//            System.out.println("velocity: " + vel1.norm());
//            System.out.println("calculations: " + Math.sqrt(6.67430E-11*m2/dist));
            return Math.abs(vel1.norm() - Math.sqrt(6.67430E-11*m2/dist)) < 100;
        }
    }
}
