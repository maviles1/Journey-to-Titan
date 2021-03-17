package titan;

public class ProbeSimulator implements ProbeSimulatorInterface{
    private ODEFunction function;
    private State state;
    /*
     * Simulate the solar system, including a probe fired from Earth at 00:00h on 1 April 2020.
     *
     * @param   p0      the starting position of the probe, relative to the earth's position.
     * @param   v0      the starting velocity of the probe, relative to the earth's velocity.
     * @param   ts      the times at which the states should be output, with ts[0] being the initial time.
     * @return  an array of size ts.length giving the position of the probe at each time stated,
     *          taken relative to the Solar System barycentre.
     */
    public ProbeSimulator(ODEFunctionInterface f, StateInterface y){
        function=(ODEFunction) f;
        state= (State) y;
    }

    public Vector3dInterface[] trajectory(Vector3dInterface p0, Vector3dInterface v0, double[] ts) {
        Vector3d[] v = new Vector3d[ts.length];

        for(int i=0;i<ts.length;i++){

        }
       // StateInterface[] s = solve(ODEFunctionInterface f, StateInterface y0, ts);



        return v;
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
        //TODO or just cast to int? does it give the correct arraysize???
        int size = (int) Math.round((tf/h)+1);
        Vector3d[] v = new Vector3d[size];

        for(int i=0;i<size;i++){

        }



        return v;
    }
}
