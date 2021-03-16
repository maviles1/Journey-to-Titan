package titan;

public class ODEFunction implements ODEFunctionInterface{
    @Override
    public RateInterface call(double t, StateInterface y) {
        // x'(t) = v(t); v'(t) = f(t, x(t)) = y'(t)
        // (aj)x = sum( (g*mi)(xi - xj) / ((xi -xj)^2 +
        State state = (State) y;
        double dt = t - state.getTime();
        double G = 6.67430E-11;

        Vector3d[] aRates = new Vector3d[state.velocities.length];
        Vector3d[] vRates = new Vector3d[state.velocities.length];
        Vector3d[] xExpect = new Vector3d[state.velocities.length];

        for (int i = 0; i < state.positions.length; i++) {
            double ax = 0;
            double ay = 0;
            double az = 0;
            for (int j = 0; j < state.positions.length; j++) {
                if (i != j) {
                    ax += (G * State.mass[i] * (state.positions[i].getX() - state.positions[j].getX())) /
                            Math.pow(Math.pow(state.positions[i].getX() - state.positions[j].getX(), 2)
                                    + Math.pow(state.positions[i].getY() - state.positions[j].getY(), 2)
                                    + Math.pow(state.positions[i].getZ() - state.positions[j].getZ(), 2), 1.5);

                    ay += (G * State.mass[i] * (state.positions[i].getY() - state.positions[j].getY())) /
                            Math.pow(Math.pow(state.positions[i].getX() - state.positions[j].getX(), 2)
                                    + Math.pow(state.positions[i].getY() - state.positions[j].getY(), 2)
                                    + Math.pow(state.positions[i].getZ() - state.positions[j].getZ(), 2), 1.5);

                    az += (G * State.mass[i] * (state.positions[i].getZ() - state.positions[j].getZ())) /
                            Math.pow(Math.pow(state.positions[i].getX() - state.positions[j].getX(), 2)
                                    + Math.pow(state.positions[i].getY() - state.positions[j].getY(), 2)
                                    + Math.pow(state.positions[i].getZ() - state.positions[j].getZ(), 2), 1.5);
                }
            }

            Vector3d a = new Vector3d(ax, ay, az);
            Vector3d v = a.addMul(dt, state.velocities[i]);
            Vector3d x = v.addMul(dt, state.positions[i]);

            aRates[i] = a;
            vRates[i] = v;
            xExpect[i] = x;
        }

        Rate rate = new Rate(xExpect, vRates);

        return rate;
    }


}
