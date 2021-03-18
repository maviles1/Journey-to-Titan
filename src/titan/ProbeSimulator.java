package titan;

import java.util.ArrayList;

public class ProbeSimulator implements ProbeSimulatorInterface{

    private ArrayList<SpaceObject> spaceObjects;
    private StateInterface[] states;

    /**
     * @param space contains a List of all planets
     */
    public ProbeSimulator(ArrayList<SpaceObject> space){
        spaceObjects=space;
    }

    /*
     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1 April 2020.
     *
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     * @return  an array of size ts.length giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre.
     */
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        //conversion of the input relative to the Solar System barycentre
        Vector3d v = (Vector3d) v0.add(spaceObjects.get(3).getVelocity());
        Vector3d p = (Vector3d) p0.add(spaceObjects.get(3).getPosition());

        //create probe
        Probe probe = new Probe("Probe", 15000, p, v);
        spaceObjects.add(probe);
        //create State with planets and probe
        State universe = initUn();

        //creates all the states of the simulation
        Solver solver = new Solver();
        StateInterface[] s = solver.solve(new ODEFunction(), universe, ts);
        states=s;
        Vector3d[] vector = new Vector3d[s.length];
        for(int i=0;i<s.length;i++){
            State ph = (State) s[i];
            vector[i] = ph.getPosition()[ph.getPosition().length-1];
        }
        return vector;
    }

    /*
     * Simulate the solar system with steps of an equal size.
     * The final step may have a smaller size, if the step-size does not exactly divide the solution time range.
     *
     * @param   tf      the final time of the evolution.
     * @param   h       the size of step to be taken
     * @return  an array of size round(tf/h)+1 giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre
     */
    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double tf, double h) {
        //conversion
        Vector3d v = (Vector3d) v0.add(spaceObjects.get(3).getVelocity());
        Vector3d p = (Vector3d) p0.add(spaceObjects.get(3).getPosition());

        //create probe
        Probe probe = new Probe("Probe", 15000, p, v);

        spaceObjects.add(probe);
        //create State with planets and probe
        State universe = initUn();
        //creates all the states of the simulation
        Solver solver = new Solver();
        StateInterface[] s = solver.solve(new ODEFunction(), universe, tf, h);
        states=s;

        Vector3d[] vector = new Vector3d[s.length];
        for(int i=0;i<s.length;i++){
            State ph = (State) s[i];
            vector[i] = ph.getPosition()[ph.getPosition().length-1];
        }
     //  System.out.println(s[s.length-1]);

        return vector;
    }

    public StateInterface[] getStates() {
        return states;
    }



    public State initUn() {
        //create positions and velocities arrays to represent the state
        Vector3d[] positions = new Vector3d[spaceObjects.size()];
        Vector3d[] velocities = new Vector3d[spaceObjects.size()];
        double[] mass = new double[spaceObjects.size()];
        int i = 0;
        for (SpaceObject spaceObject : spaceObjects) {
            positions[i] = spaceObject.getPosition();
            velocities[i] = spaceObject.getVelocity();
            mass[i++] = spaceObject.getMass();
        }

        //create the initial state
        State state = new State(positions, velocities, 0);
        state.setMass(mass);
        state.setNames();
        return state;
    }

    public StateInterface[] simulation(){
        return states;
    }

}
