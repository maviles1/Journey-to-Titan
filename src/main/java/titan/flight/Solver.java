package titan.flight;
import java.util.Random;

import titan.interfaces.*;

public class Solver implements ODESolverInterface {

    private StepInterface stepFunction;

    Probe probe;
    double [] probeMass;

    public Solver() {
        stepFunction = new RKSolver();
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
    //s
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
        probe = new Probe("S",State.mass[11], y0.getPositions()[y0.getVelocities().length - 1],  new Vector3d(0,0,0));
        probe.setFuelMass(2350);
        Random rand = new Random();
        int randomIndex = 10 - rand.nextInt(20);
        double randomThrust = (0.25) * rand.nextDouble();
        Vector3d v1 = new Vector3d(8.490797981937179E11,-1.2257695631660947E12,-1.3000611491879347E10).sub(s[0].getPositions()[11]);
        Vector3d initialThrust = new Vector3d(26775.57124009934,-57470.04719025249,-472.36369747524566);
        s[0].getVelocities()[11] = new Vector3d(0,0,0);
        double t = 0;
        thrust(probe,s[0], initialThrust);
        double stateToReach = 7200;
        for (int i = 1; i < size - 1; i++) {
            t += h; //TODO: i think we should be incrementing t at the end of the loop
            s[i] = step(f, t, s[i - 1], h);
            probe.setPosition(s[i].getPositions()[11]);
            probe.setVelocity(s[i].getVelocities()[11]);
            probeMass[i] = probe.getFuelMass();
            Random r = new Random();
            if (i == 5300){
                Vector3d v = new Vector3d(8.377236873346893E11,-1.2355819685819001E12,-1.1914403064430511E10).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(v.mul((1.0/(stateToReach - i))/3600)));
            }
            if (i == 7137){
                Vector3d v = new Vector3d(8.377236873346893E11,-1.2355819685819001E12,-1.1914403064430511E10).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(v.mul((1.0/(stateToReach - i))/3600)));
            }

            if (i == stateToReach){
                thrust(probe,s[i],probe.getVelocity().mul(-1).mul(0.98));
            }
            if (i == 7550){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 14000){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 10000){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 13000){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 14400){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 14487){
                Vector3d toEarth = new Vector3d(6.376278079466113E10,1.330882728657392E11,1.791658414687115E7).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(toEarth.mul((1.0/(14500 - i))/3600)));
            }
            if (i == 14500){
                System.out.println("Earth: "  + s[i].getPositions()[3]);
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

        System.out.println(tf-t);
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
        State.mass[11] = probe.getMass();
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

    public static int findClosestPoint(StateInterface [] states){
        int index = 0;
        double min = states[0].getPositions()[11].dist(states[0].getPositions()[8]);
        for (int i = 0; i < states.length; i++){
            if (states[i].getPositions()[11].dist(states[i].getPositions()[8]) < min){
                index = i;
                min = states[i].getPositions()[11].dist(states[i].getPositions()[8]);
            }
        }
        return index;
    }

    public static int findClosestEarthPoint(StateInterface [] states){
        int index =0 ;
        double min = states[7000].getPositions()[11].dist(states[0].getPositions()[3]);
        for (int i = 7000; i < states.length; i++){
            if (states[i].getPositions()[11].dist(states[i].getPositions()[3]) < min){
                index = i;
                min = states[i].getPositions()[11].dist(states[i].getPositions()[3]);
            }
        }
        return index;
    }
}
