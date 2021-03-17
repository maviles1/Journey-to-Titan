package titan;

public class ODEFunction implements ODEFunctionInterface{
    @Override
    public RateInterface call(double t, StateInterface y) {
        State state = (State) y;
        double dt = t - state.getTime();
        double G = 6.67430E-11;

        Vector3d[] aRates = new Vector3d[state.velocities.length];
        Vector3d[] vRates = new Vector3d[state.velocities.length];

        for (int i = 0; i < state.positions.length; i++) {
            Vector3d a = new Vector3d(0,0,0);
            Vector3d v = new Vector3d(0,0,0);

            for (int j = 0; j < state.positions.length; j++) {
                if (i != j) {

                    double p = (G * State.mass[j])/Math.pow(state.positions[j].dist(state.positions[i]),3);
                    a = a.addMul(p, state.positions[j].sub(state.positions[i]));

                }
            }
            v=state.velocities[i].addMul(dt,a);

            aRates[i] = a;
            vRates[i] = v;
        }

        Rate rate = new Rate(vRates, aRates);

        return rate;
    }


}


/*  State state = (State) y;
        double dt = t - state.getTime();
        double G = 6.67430E-11;

        Vector3d[] aRates = new Vector3d[state.velocities.length];
        Vector3d[] vRates = new Vector3d[state.velocities.length];
        Vector3d[] xExpect = new Vector3d[state.velocities.length];

        for (int i = 0; i < state.positions.length; i++) {
            Vector3d a = new Vector3d(0,0,0);

            for (int j = 0; j < state.positions.length; j++) {
                if (i != j) {

                    a.addMul((G * State.mass[i]), state.positions[j].sub(state.positions[i]));
                    double p = Math.pow(Math.pow(state.positions[i].getX() - state.positions[j].getX(), 2)
                                    + Math.pow(state.positions[i].getY() - state.positions[j].getY(), 2)
                                    + Math.pow(state.positions[i].getZ() - state.positions[j].getZ(), 2), 1.5);
                    a.mul(1/p);

                }
            }

            Vector3d v = a.addMul(dt, state.velocities[i]);
            Vector3d x = v.addMul(dt, state.positions[i]);

            aRates[i] = a;
            vRates[i] = v;
            xExpect[i] = x;
        }

        Rate rate = new Rate(xExpect, vRates);

        return rate;
        */