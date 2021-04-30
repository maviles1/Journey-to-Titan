package titan;
import java.util.Random;

public class Solver implements ODESolverInterface {
    /*
     * Solve the differential equation by taking multiple steps.
     *
     * @param   f       the function defining the differential equation dy/dt=f(t,y)
     * @param   y0      the starting state
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time
     * @return  an array of size ts.length with all intermediate states along the path
     */
    Probe probe;
    double [] probeMass;
    @Override
    public StateInterface[] solve(ODEFunctionInterface f, StateInterface y0, double[] ts) {
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
        int size = (int) Math.round(tf / h + 1);
        StateInterface[] s = new StateInterface[size];
        double [] probeMass = new double[size];
        s[0] = y0;
        probe = new Probe("S",15000, y0.getPositions()[y0.getVelocities().length - 1],  y0.getVelocities()[y0.getVelocities().length - 1]);
        double t = 0;
        for (int i = 1; i < size - 1; i++) {
            t += h;
            s[i] = step(f, t, s[i - 1], h);
            probe.setPosition(s[i].getPositions()[11]);
            probe.setVelocity(s[i].getVelocities()[11]);
            if (randomAdd()){
                thrust(probe, s[i], randomV());
                s[i].getVelocities()[11] = probe.getVelocity();
            }
            probeMass[i] = probe.getFuelMass();
            this.probeMass = probeMass;
        }
        //tf or tf/h
        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
    }

    public double [] getProbeMass(){
        return probeMass;
    }

    @Override
    public StateInterface step(ODEFunctionInterface f, double t, StateInterface y, double h) {

        RateInterface r = f.call(t, y);
        return y.addMul(h, r); //h*f(t,y)
    }

    public void thrust(Probe probe, StateInterface state, Vector3d toThrust){
        probe.thrust(toThrust);
        state.getVelocities()[11] = probe.getVelocity();
    }

    public Vector3d randomV(){
        Random rand = new Random();
        Vector3d v = new Vector3d(rand.nextDouble()*10000 - 5000, rand.nextDouble()*10000 - 5000, rand.nextDouble()*10000 - 5000);
        return v;
    }

    public boolean randomAdd(){
        Random rand = new Random();
        int r = rand.nextInt()*100;
        if (r < 10){
            return true;
        }
        return false;
    }
}
