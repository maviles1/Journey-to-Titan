package titan.flight;
import java.util.ArrayList;
import java.util.Random;

import javafx.css.converter.DeriveColorConverter;
import titan.interfaces.*;

public class Solver implements ODESolverInterface {

    private StepInterface stepFunction;

    Probe probe;
    double [] probeMass;
    int landingIndex;
    Vector3d landingVector;
    final double G = 6.67430E-11;

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
        int size = (int) Math.round((tf / h) + 1.5);
        StateInterface[] s = new StateInterface[size];
        double [] probeMass = new double[size];
        s[0] = y0;
        probe = new Probe("S",State.mass[11], y0.getPositions()[y0.getVelocities().length - 1],  new Vector3d(0,0,0));
        probe.setFuelMass(2350);
        State.mass[8] = 1.34553e23;
        Random rand = new Random();
        int randomIndex = 10 - rand.nextInt(20);
        double randomThrust = (0.25) * rand.nextDouble();
        Vector3d v1 = new Vector3d(8.490797981937179E11,-1.2257695631660947E12,-1.3000611491879347E10).sub(s[0].getPositions()[11]);
        Vector3d initialThrust = new Vector3d(26775.57124009934,-57470.04719025249,-472.36369747524566);
        s[0].getVelocities()[11] = new Vector3d(0,0,0);
        double t = 0;
        thrust(probe,s[0], initialThrust);
        double stateToReach = 12132;
        double bufferStates = 4000;
        Vector3d rotationPoint = new Vector3d(9.642882419816824E11,-1.1329380605841162E12,-1.836751345662014E10).sub(new Vector3d(9.642882419816824E11,-1.1329380605841162E12,-1.836751345662014E10));
        Vector3d prevPoint = new Vector3d(9.64284008578408E11,-1.1329238606616946E12,-1.8369471931435688E10);
        for (int i = 1; i < size - 1; i++){
            t += h; //TODO: i think we should be incrementing t at the end of the loop
            s[i] = step(f, t, s[i - 1], h);
            probe.setPosition(s[i].getPositions()[11]);
            probe.setVelocity(s[i].getVelocities()[11]);
            probeMass[i] = probe.getFuelMass();
            Random r = new Random();
            //(8.406606672917942E11,-1.2317056568007727E12,-1.1944251219022985E10)
            //1st (8.377236873346893E11,-1.2355819685819001E12,-1.1914403064430511E10)
            Vector3d v = new Vector3d(9.642882419816824E11,-1.1329380605841162E12,-1.836751345662014E10);
//            System.out.println(v.dist(new Vector3d(9.094440054697218E11,-1.1776829849786262E12,-1.6206644915693071E10))/1000);
            v = v.add(new Vector3d(-1e10 -2.1201e10,1e10 + 4.0e9,0));

            if (i < stateToReach - bufferStates && i % 500 == 0){
                Vector3d ve = v.sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(ve.mul((1.0/(stateToReach - bufferStates - i))/3600)));
            }

            if (i >= stateToReach - bufferStates && i < stateToReach && (stateToReach - i)%4 == 0){
                Vector3d ve = new Vector3d(9.64288419816824E11,-1.1329380605841162E12,-1.836751345662014E10).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(ve.mul((1.0/(stateToReach - i))/3600)));
            }
            if (i == 12132){
                System.out.println(probe.getVelocity());
            }

//            if (i >= stateToReach){
//                Vector3d probePosition = s[i].getPositions()[11];
//                Vector3d titanPosition = s[i].getPositions()[8];
//                rotationPoint =  probePosition.sub(titanPosition);
//                Vector3d nextRotationPoint = rotationPoint.rotate(20,0,0);
//                nextRotationPoint = nextRotationPoint.add(s[i].getPositions()[8]);
//                if (probePosition.dist(titanPosition)/1000 < 2800 && probePosition.dist(titanPosition)/1000 > 2700 ){
//                    System.out.println(i + " " + (probePosition.dist(titanPosition)/1000) + " " + probe.getVelocity());
//                }
//                StateInterface next = step(f,t,s[i],h);
//                Vector3d nextTitan = next.getPositions()[8].sub(titanPosition);
//                Vector3d thrustVector = nextRotationPoint.sub(s[i].getPositions()[11]).add(nextTitan);
//                thrust(probe,s[i],probe.getVelocity().mul(-1).add(thrustVector.mul(1.0/3600)));
//            }
        }
        this.probeMass = probeMass;

        s[size - 1] = step(f, tf, s[size - 2], tf - t);

        return s;
}

    public StateInterface[] optimize(ODEFunctionInterface f, StateInterface y0, double tf, double h, double bufferStates, int frequency, Vector3d vec){
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
        double stateToReach = 10000;
        for (int i = 1; i < size - 1; i++) {
            t += h; //TODO: i think we should be incrementing t at the end of the loop
            s[i] = step(f, t, s[i - 1], h);
            probe.setPosition(s[i].getPositions()[11]);
            probe.setVelocity(s[i].getVelocities()[11]);
            probeMass[i] = probe.getFuelMass();
            Random r = new Random();
            //(8.406606672917942E11,-1.2317056568007727E12,-1.1944251219022985E10)
            //1st (8.377236873346893E11,-1.2355819685819001E12,-1.1914403064430511E10)
            Vector3d v = new Vector3d(9.094459151959227E11,-1.1776811924820583E12,-1.6206528943657473E10);
            v = v.add(vec);

            if (i < stateToReach - bufferStates && i % 500 == 0){
                Vector3d ve = v.sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(ve.mul((1.0/(stateToReach - bufferStates - i))/3600)));
            }

            if (i >= stateToReach - bufferStates && i < stateToReach && (stateToReach - i) % frequency == 0){
                Vector3d ve = new Vector3d(9.094459151959227E11,-1.1776811924820583E12,-1.6206528943657473E10).sub(s[i].getPositions()[11]);
                thrust(probe,s[i],probe.getVelocity().mul(-1).add(ve.mul((1.0/(stateToReach - i))/3600)));
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

    public Vector3d findOrbitPoint(Vector3d titanPos){
        Random rand = new Random();
        double rangeMin = 0;
        double rangeMax = 1e9;
        double v1 = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        double x = v1;
        return new Vector3d(0,0,0);
    }

    public void land(Probe probe, StateInterface state, Vector3d toLand){
        probe.land(toLand);
        this.landingVector = toLand.copy();
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

    public static int findClosestPoint(StateInterface [] states){
        int index = 0;
        double min = states[0].getPositions()[11].dist(states[0].getPositions()[8]);
        for (int i = 0; i < states.length; i++){
            if (states[i].getPositions()[11].dist(states[i].getPositions()[8]) < min){
                index = i;
                min = states[i].getPositions()[11].dist(states[i].getPositions()[8]);
            }
//            if (Math.abs(states[i].getPositions()[8].sub(states[i].getPositions()[11]).dot(states[i].getVelocities()[11])) < 100){
//                index = i;
//            }
        }
        return index;
    }

    public static int findClosestEarthPoint(StateInterface [] states){
        int index = 0;
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
